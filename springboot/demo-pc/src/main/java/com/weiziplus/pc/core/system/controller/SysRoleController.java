package com.weiziplus.pc.core.system.controller;

import com.weiziplus.common.models.SysRole;
import com.weiziplus.common.util.PageUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.pc.common.interceptor.AdminAuthToken;
import com.weiziplus.pc.common.interceptor.SysUserLog;
import com.weiziplus.pc.core.system.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/05/28 09/59
 */
@RestController
@AdminAuthToken
@RequestMapping("/sysRole")
@Api(tags = "系统角色")
public class SysRoleController {

    @Autowired
    SysRoleService service;

    @ApiOperation(value = "获取分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "search", value = "搜索", dataType = "String", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLog(description = "获取系统角色分页数据")
    public ResultUtils<PageUtils<SysRole>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String search) {
        return service.getPageList(pageNum, pageSize, search);
    }

    @ApiOperation(value = "获取列表数据")
    @GetMapping("/getList")
    @SysUserLog(description = "获取系统角色列表数据")
    public ResultUtils<List<SysRole>> getList() {
        return service.getList();
    }

    @ApiOperation(value = "获取角色拥有的功能列表")
    @GetMapping("/getFunctionList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "query"),
    })
    @SysUserLog(description = "获取角色拥有的功能列表")
    public ResultUtils<List<Integer>> getFunctionList(Integer id) {
        return service.getFunctionList(id);
    }

    @ApiOperation(value = "新增角色")
    @PostMapping("/add")
    @SysUserLog(description = "新增角色", type = SysUserLog.TYPE_INSERT)
    public ResultUtils add(SysRole sysRole) {
        return service.add(sysRole);
    }

    @ApiOperation(value = "编辑角色")
    @PostMapping("/update")
    @SysUserLog(description = "编辑角色", type = SysUserLog.TYPE_UPDATE)
    public ResultUtils update(SysRole sysRole) {
        return service.update(sysRole);
    }

    @ApiOperation(value = "删除角色")
    @PostMapping("/delete")
    @SysUserLog(description = "删除角色", type = SysUserLog.TYPE_DELETE)
    public ResultUtils delete(Integer id) {
        return service.delete(id);
    }

    @ApiOperation(value = "修改角色功能")
    @PostMapping("/updateRoleFunction")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "functionIds", value = "功能ids", required = true, dataType = "Integer[]", paramType = "form"),
    })
    @SysUserLog(description = "修改角色功能", type = SysUserLog.TYPE_UPDATE)
    public ResultUtils updateRoleFunction(Integer roleId, Integer[] functionIds) {
        return service.updateRoleFunction(roleId, functionIds);
    }

    @ApiOperation(value = "修改角色状态")
    @PostMapping("/updateStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态", required = true, dataType = "Integer", paramType = "form"),
    })
    @SysUserLog(description = "修改角色状态", type = SysUserLog.TYPE_UPDATE)
    public ResultUtils updateStatus(Integer id, Integer status) {
        return service.updateStatus(id, status);
    }

}