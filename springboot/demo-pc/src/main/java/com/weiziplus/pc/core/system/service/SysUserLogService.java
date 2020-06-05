package com.weiziplus.pc.core.system.service;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.github.pagehelper.PageHelper;
import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.base.BaseWhere;
import com.weiziplus.common.models.SysUserLog;
import com.weiziplus.common.util.HttpRequestUtils;
import com.weiziplus.common.util.PageUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.common.util.ToolUtils;
import com.weiziplus.common.util.token.AdminTokenUtils;
import com.weiziplus.pc.common.config.MyGlobalConfig;
import com.weiziplus.pc.core.system.mapper.SysUserLogMapper;
import com.weiziplus.pc.core.system.vo.SysUserLogVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/05/29 15/41
 */
@Slf4j
@Service
public class SysUserLogService extends BaseService {

    @Autowired
    SysUserLogMapper mapper;

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
     * @param username
     * @param realName
     * @param type
     * @param description
     * @param ipAddress
     * @param borderName
     * @param osName
     * @param startTime
     * @param endTime
     * @param createTimeSort
     * @return
     */
    public ResultUtils<PageUtils<List<SysUserLogVo>>> getPageList(Integer pageNum, Integer pageSize
            , String username, String realName, Integer type, String description, String ipAddress
            , String borderName, String osName, String startTime, String endTime
            , String createTimeSort) {
        if (!OrderByType.contains(createTimeSort)) {
            return ResultUtils.error("排序字段错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<List<SysUserLogVo>> pageUtil = PageUtils.pageInfo(mapper.getListVo(
                username, realName, type, description, ipAddress, borderName, osName, startTime, endTime,
                createTimeSort));
        return ResultUtils.success(pageUtil);
    }

    /**
     * 导出excel
     *
     * @param startTime
     * @param endTime
     */
    public void exportExcel(String startTime, String endTime) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        Integer userIdByHttpServletRequest = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(userIdByHttpServletRequest)) {
            HttpRequestUtils.handleErrorResponse(
                    response, ResultUtils.errorRole("只有超级管理员才可以导出系统日志"), "非超级管理员导出系统用户日志");
            return;
        }
        List<SysUserLog> sysUserLogList = baseFindListByClassAndBaseWhereList(SysUserLog.class, new ArrayList<BaseWhere>(ToolUtils.initialCapacity(2)) {{
            add(new BaseWhere(SysUserLog.COLUMN_CREATE_TIME, BaseWhere.Where.MORE_THAN.getValue(), startTime));
            add(new BaseWhere(SysUserLog.COLUMN_CREATE_TIME, BaseWhere.Where.LESS_THAN_EQUAL.getValue(), endTime));
        }});
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(outputStream, ExcelTypeEnum.XLSX);
            Sheet sheet = new Sheet(1, 0);
            sheet.setSheetName("系统用户日志");
            Table table = new Table(1);
            //表头
            List<List<String>> titleList = new ArrayList<>(ToolUtils.initialCapacity(7));
            titleList.add(Arrays.asList("请求路径"));
            titleList.add(Arrays.asList("请求参数"));
            titleList.add(Arrays.asList("请求类型"));
            titleList.add(Arrays.asList("操作描述"));
            titleList.add(Arrays.asList("浏览器"));
            titleList.add(Arrays.asList("操作系统"));
            titleList.add(Arrays.asList("创建时间"));
            table.setHead(titleList);
            //内容
            List<List<String>> dataList = new ArrayList<>();
            String[] typeArr = {null, "查询", "新增", "修改", "删除"};
            for (SysUserLog sysUserLog : sysUserLogList) {
                dataList.add(Arrays.asList(
                        sysUserLog.getUrl(),
                        sysUserLog.getParam(),
                        typeArr[sysUserLog.getType()],
                        sysUserLog.getDescription(),
                        sysUserLog.getBorderName(),
                        sysUserLog.getOsName(),
                        sysUserLog.getCreateTime()
                ));
            }
            writer.write0(dataList, sheet, table);
            // 下载EXCEL
            response.setHeader("Content-Disposition", "attachment");
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            writer.finish();
            outputStream.flush();
        } catch (IOException e) {
            log.warn("系统用户日志导出excel出错，详情:" + e);
            HttpRequestUtils.handleErrorResponse(
                    response, ResultUtils.error("系统用户日志导出excel出错，详情:" + e), "系统用户日志导出excel出错");
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    log.warn("系统用户日志导出excel关闭流出错，详情:" + e);
                }
            }
        }
    }

}
