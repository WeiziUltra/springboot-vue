package com.weiziplus.pc.core.system.controller;

import com.weiziplus.common.models.SysFunction;
import com.weiziplus.common.util.PageUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.pc.common.interceptor.AdminAuthToken;
import com.weiziplus.pc.common.interceptor.SysUserLog;
import com.weiziplus.pc.core.system.service.SysFunctionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/05/28 11/38
 */
@RestController
@AdminAuthToken
@RequestMapping("/sysFunction")
@Api(tags = "系统功能")
public class SysFunctionController {

    @Autowired
    SysFunctionService service;

    @ApiOperation(value = "获取分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLog(description = "获取系统功能分页数据")
    public ResultUtils<PageUtils<SysFunction>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return service.getPageList(pageNum, pageSize);
    }

    @ApiOperation(value = "获取树形结构")
    @GetMapping("/getTree")
    @SysUserLog(description = "获取系统功能树形结构")
    public ResultUtils<List<SysFunction>> getTree() {
        return service.getTree();
    }

    @ApiOperation(value = "新增功能")
    @PostMapping("/add")
    @SysUserLog(description = "新增功能", type = SysUserLog.TYPE_INSERT)
    public ResultUtils add(SysFunction sysFunction) {
        return service.add(sysFunction);
    }

    @ApiOperation(value = "修改功能")
    @PostMapping("/update")
    @SysUserLog(description = "修改功能", type = SysUserLog.TYPE_UPDATE)
    public ResultUtils update(SysFunction sysFunction) {
        return service.update(sysFunction);
    }

    @ApiOperation(value = "删除功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/delete")
    @SysUserLog(description = "删除功能", type = SysUserLog.TYPE_DELETE)
    public ResultUtils delete(Integer id) {
        return service.delete(id);
    }

}
