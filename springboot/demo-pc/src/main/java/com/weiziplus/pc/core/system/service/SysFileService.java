package com.weiziplus.pc.core.system.service;

import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.util.HttpRequestUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.common.util.ToolUtils;
import com.weiziplus.common.util.token.AdminTokenUtils;
import com.weiziplus.pc.common.config.MyGlobalConfig;
import com.weiziplus.pc.core.system.vo.LogFileVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
     * 日志存放路径
     */
    @Value("${global.log-path}")
    private String logPath = "";

    /**
     * 获取日志文件
     *
     * @return
     */
    public ResultUtils<List<LogFileVo>> getLogFile() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Integer userIdByHttpServletRequest = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(userIdByHttpServletRequest)) {
            return ResultUtils.errorRole("只要超级管理员才可以查看日志文件");
        }
        //日志文件夹
        File baseFile = new File(logPath);
        //获取文件夹下所有文件
        File[] fileList = baseFile.listFiles();
        if (null == fileList) {
            return ResultUtils.success();
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
     */
    public void downLogFile(String dir, String name) {
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
        InputStream inputStream = null;
        ServletOutputStream servletOutputStream = null;
        try {
            inputStream = new FileInputStream(file);
            servletOutputStream = response.getOutputStream();
            IOUtils.copy(inputStream, servletOutputStream);
            response.flushBuffer();
        } catch (Exception e) {
            log.warn("下载日志文件出错，详情:" + e);
            HttpRequestUtils.handleErrorResponse(
                    response, ResultUtils.error("系统错误，请重试。" + e), "下载日志文件出错");
        } finally {
            try {
                if (null != servletOutputStream) {
                    servletOutputStream.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.warn("下载日志文件关闭流出错，详情:" + e);
            }
        }
    }

}