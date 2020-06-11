package com.weiziplus.pc.core.system.controller;

import com.weiziplus.common.models.SysError;
import com.weiziplus.common.util.PageUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.pc.common.interceptor.AdminAuthToken;
import com.weiziplus.pc.common.interceptor.SysUserLog;
import com.weiziplus.pc.core.system.service.SysErrorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wanglongwei
 * @date 2020/05/29 15/50
 */
@RestController
@AdminAuthToken
@RequestMapping("/sysError")
@Api(tags = "系统异常")
public class SysErrorController {

    @Autowired
    SysErrorService service;

    @ApiOperation(value = "获取分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "search", value = "搜索", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "createTimeSort", value = "创建时间排序", dataType = "String", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLog(description = "获取系统异常分页数据")
    public ResultUtils<PageUtils<SysError>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String search,
            @RequestParam(defaultValue = "DESC") String createTimeSort) {
        return service.getPageList(pageNum, pageSize, search, createTimeSort);
    }

}
