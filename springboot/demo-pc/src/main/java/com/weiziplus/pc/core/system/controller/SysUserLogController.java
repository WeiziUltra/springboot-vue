package com.weiziplus.pc.core.system.controller;

import com.weiziplus.common.util.PageUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.pc.common.interceptor.AdminAuthToken;
import com.weiziplus.pc.common.interceptor.SysUserLog;
import com.weiziplus.pc.core.system.service.SysUserLogService;
import com.weiziplus.pc.core.system.vo.SysUserLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/05/29 15/41
 */
@RestController
@AdminAuthToken
@RequestMapping("/sysUserLog")
@Api(tags = "系统用户日志")
public class SysUserLogController {

    @Autowired
    SysUserLogService service;

    @ApiOperation(value = "获取分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "realName", value = "真实姓名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "操作", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ipAddress", value = "ip地址", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "borderName", value = "浏览器", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "osName", value = "操作系统", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "起始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "截止时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "createTimeSort", value = "创建时间排序", dataType = "String", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLog(description = "获取系统用户日志分页数据")
    public ResultUtils<PageUtils<List<SysUserLogVo>>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String username, String realName, Integer type, String description, String ipAddress,
            String borderName, String osName, String startTime, String endTime,
            @RequestParam(defaultValue = "DESC") String createTimeSort) {
        return service.getPageList(pageNum, pageSize,
                username, realName, type, description, ipAddress, borderName, osName, startTime, endTime,
                createTimeSort);
    }

    @ApiOperation(value = "导出excel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "截止时间", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/exportExcel")
    @SysUserLog(description = "导出系统用户日志excel")
    public void exportExcel(
            @RequestParam(defaultValue = "1990-01-01 00:00:00") String startTime,
            @RequestParam(defaultValue = "2200-12-31 23:59:59") String endTime) {
        service.exportExcel(startTime, endTime);
    }

}
