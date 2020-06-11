package com.weiziplus.pc.core.system.controller;

import com.weiziplus.common.util.PageUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.pc.common.interceptor.AdminAuthToken;
import com.weiziplus.pc.common.interceptor.SysUserLog;
import com.weiziplus.pc.core.system.dto.SysUserDto;
import com.weiziplus.pc.core.system.service.SysUserService;
import com.weiziplus.pc.core.system.vo.SysUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wanglongwei
 * @date 2020/05/28 09/25
 */
@RestController
@AdminAuthToken
@RequestMapping("/sysUser")
@Api(tags = "系统用户")
public class SysUserController {

    @Autowired
    SysUserService service;

    @ApiOperation(value = "获取分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleId", value = "角色", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "lastActiveTime", value = "最后活跃时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "createTime", value = "创建时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "lastActiveTimeSort", value = "最后活跃时间排序", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "createTimeSort", value = "创建时间排序", dataType = "String", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLog(description = "获取系统用户分页数据")
    public ResultUtils<PageUtils<SysUserVo>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String username, Integer roleId, Integer status, String lastActiveTime, String createTime,
            @RequestParam(defaultValue = "DESC") String lastActiveTimeSort,
            @RequestParam(defaultValue = "DESC") String createTimeSort) {
        return service.getPageList(pageNum, pageSize,
                username, roleId, status, lastActiveTime, createTime,
                lastActiveTimeSort, createTimeSort);
    }

    @ApiOperation(value = "新增用户")
    @PostMapping("/add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleIds", value = "角色ids", dataType = "Integer[]", paramType = "form"),
    })
    @SysUserLog(description = "新增用户", type = SysUserLog.TYPE_INSERT, paramIgnore = "__t,password")
    public ResultUtils add(SysUserDto sysUserDto, Integer[] roleIds) {
        return service.add(sysUserDto, roleIds);
    }

    @ApiOperation(value = "修改用户手机号")
    @PostMapping("/updatePhone")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "form"),
    })
    @SysUserLog(description = "修改用户手机号", type = SysUserLog.TYPE_UPDATE)
    public ResultUtils updatePhone(Integer id, String phone) {
        return service.updatePhone(id, phone);
    }

    @ApiOperation(value = "修改用户状态")
    @PostMapping("/updateStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态", required = true, dataType = "Integer", paramType = "form"),
    })
    @SysUserLog(description = "修改用户状态", type = SysUserLog.TYPE_UPDATE)
    public ResultUtils updateStatus(Integer id, Integer status) {
        return service.updateStatus(id, status);
    }

    @ApiOperation(value = "修改用户角色")
    @PostMapping("/updateRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "roleIds", value = "角色ids", dataType = "Integer[]", paramType = "form"),
    })
    @SysUserLog(description = "修改用户角色", type = SysUserLog.TYPE_UPDATE)
    public ResultUtils updateRole(Integer id, Integer[] roleIds) {
        return service.updateRole(id, roleIds);
    }

    @ApiOperation(value = "删除用户")
    @PostMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
    })
    @SysUserLog(description = "删除用户", type = SysUserLog.TYPE_DELETE)
    public ResultUtils delete(Integer id) {
        return service.delete(id);
    }

    @ApiOperation(value = "修改头像")
    @PostMapping("/updateIcon")
    @SysUserLog(description = "修改头像", type = SysUserLog.TYPE_UPDATE)
    public ResultUtils updateIcon(MultipartFile file) {
        return service.updateIcon(file);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/updatePwd")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPwd", value = "旧密码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "newPwd", value = "新密码", required = true, dataType = "String", paramType = "form"),
    })
    @SysUserLog(description = "修改密码", type = SysUserLog.TYPE_UPDATE, paramIgnore = "__t,oldPwd,newPwd")
    public ResultUtils updatePwd(String oldPwd, String newPwd) {
        return service.updatePwd(oldPwd, newPwd);
    }

    @ApiOperation(value = "重置密码")
    @PostMapping("/resetPwd")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "新密码", required = true, dataType = "String", paramType = "form"),
    })
    @SysUserLog(description = "重置密码", type = SysUserLog.TYPE_UPDATE, paramIgnore = "__t,password")
    public ResultUtils resetPwd(Integer id, String password) {
        return service.resetPwd(id, password);
    }

}
