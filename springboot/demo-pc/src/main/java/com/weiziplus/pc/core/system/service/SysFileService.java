package com.weiziplus.pc.core.system.service;

import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.config.GlobalConfig;
import com.weiziplus.common.util.HttpRequestUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.common.util.ToolUtils;
import com.weiziplus.common.util.token.AdminTokenUtils;
import com.weiziplus.pc.common.config.MyGlobalConfig;
import com.weiziplus.pc.core.system.vo.LogFileVo;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/06/05 08/35
 */
@Slf4j
@Service
public class SysFileService extends BaseService {

    /**
     * 系统日志存放路径
     */
    @Value("${global.log-path.pc}")
    private String pcLogPath = "";

    /**
     * web日志存放路径
     */
    @Value("${global.log-path.web:}")
    private String webLogPath = "";

    /**
     * 获取日志文件
     *
     * @param type
     * @return
     */
    public ResultUtils<List<LogFileVo>> getLogFile(String type) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Integer userIdByHttpServletRequest = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(userIdByHttpServletRequest)) {
            return ResultUtils.errorRole("只有超级管理员才可以查看日志文件");
        }
        if (null == type) {
            return ResultUtils.error("类型不能为空");
        }
        type = type.toUpperCase();
        //日志路径
        String logPath;
        switch (type) {
            case "PC": {
                logPath = pcLogPath;
            }
            break;
            case "WEB": {
                logPath = webLogPath;
            }
            break;
            default: {
                return ResultUtils.error("类型错误");
            }
        }
        //日志文件夹
        File baseFile = new File(logPath);
        //获取文件夹下所有文件
        File[] fileList = baseFile.listFiles();
        if (null == fileList) {
            return ResultUtils.error("请检查日志文件路径设置");
        }
        List<LogFileVo> dirList = new ArrayList<>(ToolUtils.initialCapacity(fileList.length));
        for (File dir : fileList) {
            File[] files = dir.listFiles();
            //不是目录
            if (null == files) {
                continue;
            }
            List<LogFileVo> childrenList = new ArrayList<>(ToolUtils.initialCapacity(files.length));
            for (File file : files) {
                String fileName = file.getName();
                LogFileVo logFileVo = new LogFileVo()
                        .setName(fileName)
                        .setLength(file.length());
                childrenList.add(logFileVo);
            }
            LogFileVo fileVo = new LogFileVo()
                    .setName(dir.getName())
                    .setFileNum(childrenList.size())
                    .setChildren(childrenList);
            dirList.add(fileVo);
        }
        return ResultUtils.success(dirList);
    }

    /**
     * 下载日志文件
     *
     * @param dir
     * @param name
     * @param type
     */
    public void downLogFile(String dir, String name, String type) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        Integer userIdByHttpServletRequest = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(userIdByHttpServletRequest)) {
            HttpRequestUtils.handleErrorResponse(
                    response, ResultUtils.errorRole("只有超级管理员才可以导出日志文件"), "非超级管理员导出日志文件");
            return;
        }
        if (ToolUtils.isBlank(dir)) {
            HttpRequestUtils.handleErrorResponse(
                    response, ResultUtils.error("目录错误"), "下载日志文件目录错误");
            return;
        }
        if (ToolUtils.isBlank(name)) {
            HttpRequestUtils.handleErrorResponse(
                    response, ResultUtils.error("文件名错误"), "下载日志文件文件名错误");
            return;
        }
        if (null == type) {
            HttpRequestUtils.handleErrorResponse(
                    response, ResultUtils.error("类型错误"), "下载日志文件类型错误");
            return;
        }
        type = type.toUpperCase();
        //日志路径
        String logPath;
        switch (type) {
            case "PC": {
                logPath = pcLogPath;
            }
            break;
            case "WEB": {
                logPath = webLogPath;
            }
            break;
            default: {
                HttpRequestUtils.handleErrorResponse(
                        response, ResultUtils.error("类型错误"), "下载日志文件类型错误");
                return;
            }
        }
        //获取当前目录
        File dirFile = new File(logPath + File.separator + dir);
        if (!dirFile.exists()) {
            HttpRequestUtils.handleErrorResponse(
                    response, ResultUtils.error("目录错误"), "下载日志文件目录错误");
            return;
        }
        File[] files = dirFile.listFiles();
        if (null == files || 0 >= files.length) {
            HttpRequestUtils.handleErrorResponse(
                    response, ResultUtils.error("文件名错误"), "下载日志文件文件名错误");
            return;
        }
        //是否有当前日志文件
        boolean haveFileFlag = false;
        for (File file : files) {
            if (name.equals(file.getName())) {
                haveFileFlag = true;
                break;
            }
        }
        if (!haveFileFlag) {
            HttpRequestUtils.handleErrorResponse(
                    response, ResultUtils.error("文件名错误"), "下载日志文件文件名错误");
            return;
        }
        //当前文件
        File file = new File(logPath + File.separator + dir + File.separator + name);
        try {
            @Cleanup InputStream inputStream = new FileInputStream(file);
            @Cleanup ServletOutputStream servletOutputStream = response.getOutputStream();
            IOUtils.copy(inputStream, servletOutputStream);
            response.flushBuffer();
        } catch (Exception e) {
            log.warn("下载日志文件出错，详情:" + e);
            HttpRequestUtils.handleErrorResponse(
                    response, ResultUtils.error("系统错误，请重试。" + e), "下载日志文件出错");
        }
    }

    /**
     * 获取常用文件
     *
     * @return
     */
    public ResultUtils<List<LogFileVo>> getFile() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Integer userIdByHttpServletRequest = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(userIdByHttpServletRequest)) {
            return ResultUtils.errorRole("只有超级管理员才可以查看常用文件");
        }
        //常用文件存放名字
        String mkdirName = "file";
        //日志文件夹
        File baseFile = new File(GlobalConfig.getBaseFilePath() + File.separator + mkdirName);
        //获取文件夹下所有文件
        File[] fileList = baseFile.listFiles();
        if (null == fileList) {
            return ResultUtils.error("请检查常用文件路径设置");
        }
        List<LogFileVo> resultList = new ArrayList<>(ToolUtils.initialCapacity(fileList.length));
        String baseUrl = GlobalConfig.getMybatisFilePathPrefix() + File.separator + mkdirName + File.separator;
        for (File file : fileList) {
            String name = file.getName();
            LogFileVo logFileVo = new LogFileVo()
                    .setName(name)
                    .setLength(file.length())
                    .setUrl(file.isDirectory() ? "" : baseUrl + name)
                    .setChildren(getChildrenList(file.listFiles(), baseUrl + name + File.separator));
            resultList.add(logFileVo);
        }
        return ResultUtils.success(resultList);
    }

    /**
     * 获取子级文件列表
     *
     * @param fileList
     * @param parentUrl
     * @return
     */
    private List<LogFileVo> getChildrenList(File[] fileList, String parentUrl) {
        if (null == fileList) {
            return new ArrayList<>(ToolUtils.initialCapacity(2));
        }
        List<LogFileVo> resultList = new ArrayList<>(fileList.length);
        for (File file : fileList) {
            String name = file.getName();
            LogFileVo logFileVo = new LogFileVo()
                    .setName(name)
                    .setLength(file.length())
                    .setUrl(file.isDirectory() ? "" : parentUrl + name)
                    .setChildren(getChildrenList(file.listFiles(), parentUrl + name + File.separator));
            resultList.add(logFileVo);
        }
        return resultList;
    }

    /**
     * 更新常用文件
     *
     * @param file
     * @param url
     * @return
     */
    public ResultUtils uploadFile(MultipartFile file, String url) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Integer userIdByHttpServletRequest = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(userIdByHttpServletRequest)) {
            return ResultUtils.errorRole("只有超级管理员才可以更新常用文件");
        }
        if (ToolUtils.isBlank(url)) {
            return ResultUtils.error("url不能为空");
        }
        if (null == file) {
            return ResultUtils.error("文件不能为空");
        }
        int index = url.indexOf(GlobalConfig.getMybatisFilePathPrefix());
        //如果不是当前项目路径开头url
        if (0 != index) {
            return ResultUtils.error("url错误");
        }
        String path = url.substring(index + GlobalConfig.getMybatisFilePathPrefix().length());
        File oldFile = new File(GlobalConfig.getBaseFilePath() + path);
        //如果是目录，不进行操作
        if (oldFile.isDirectory()) {
            return ResultUtils.error("当前为目录");
        }
        //如果文件不存在
        if (!oldFile.isFile() || !oldFile.exists()) {
            return ResultUtils.error("文件不存在，请检查路径是否正确或者联系管理员手动更新文件");
        }
        boolean delete = oldFile.delete();
        if (!delete) {
            return ResultUtils.error("文件更新失败，请重试");
        }
        File newFile = new File(GlobalConfig.getBaseFilePath() + path);
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            log.warn("文件更新失败" + e);
            return ResultUtils.error("文件更新失败，请联系管理员手动更新文件");
        }
        return ResultUtils.success();
    }

}