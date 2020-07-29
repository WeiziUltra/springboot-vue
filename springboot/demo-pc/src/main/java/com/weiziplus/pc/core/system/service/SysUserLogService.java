package com.weiziplus.pc.core.system.service;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.base.BaseWhere;
import com.weiziplus.common.base.BaseWhereEnum;
import com.weiziplus.common.base.BaseWhereModel;
import com.weiziplus.common.models.SysUserLog;
import com.weiziplus.common.util.HttpRequestUtils;
import com.weiziplus.common.util.PageUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.common.util.ToolUtils;
import com.weiziplus.common.util.token.AdminTokenUtils;
import com.weiziplus.pc.common.config.MyGlobalConfig;
import com.weiziplus.pc.core.system.excel.SysUserLogExcelModel;
import com.weiziplus.pc.core.system.mapper.SysUserLogMapper;
import com.weiziplus.pc.core.system.vo.SysUserLogVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    public ResultUtils<PageUtils<SysUserLogVo>> getPageList(Integer pageNum, Integer pageSize
            , String username, String realName, Integer type, String description, String ipAddress
            , String borderName, String osName, String startTime, String endTime
            , String createTimeSort) {
        if (!OrderByType.contains(createTimeSort)) {
            return ResultUtils.error("排序字段错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<SysUserLogVo> pageUtil = PageUtils.pageInfo(mapper.getListVo(
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
        BaseWhere<SysUserLog> baseWhere = new BaseWhere<>(SysUserLog.class)
                .where(new ArrayList<BaseWhereModel>(ToolUtils.initialCapacity(2)) {{
                    add(new BaseWhereModel(SysUserLog.COLUMN_CREATE_TIME, BaseWhereEnum.MORE_THAN, startTime));
                    add(new BaseWhereModel(SysUserLog.COLUMN_CREATE_TIME, BaseWhereEnum.LESS_THAN_EQUAL, endTime));
                }});
        List<SysUserLog> sysUserLogList = baseFindList(baseWhere);
        try {
            //该方法不需要关闭流
            EasyExcel.write(response.getOutputStream(), SysUserLogExcelModel.class)
                    .sheet("系统用户日志")
                    .doWrite(sysUserLogList);
            // 下载EXCEL
            response.setHeader("Content-Disposition", "attachment");
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            log.warn("系统用户日志导出excel出错，详情:" + e);
            HttpRequestUtils.handleErrorResponse(
                    response, ResultUtils.error("系统用户日志导出excel出错，详情:" + e), "系统用户日志导出excel出错");
        }
    }

}
