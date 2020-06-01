package com.weiziplus.pc.core.system.controller;

import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.pc.common.interceptor.AdminAuthToken;
import com.weiziplus.pc.common.interceptor.SysUserLog;
import com.weiziplus.pc.core.system.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanglongwei
 * @date 2019/5/9 11:07
 */
@RestController
@RequestMapping()
@Api(tags = "登录")
public class LoginController {

    @Autowired
    LoginService service;

    @ApiOperation(value = "获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "uuid", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping("/getValidateCode")
    public void getValidateCode(String uuid) {
        service.getValidateCode(uuid);
    }

    @ApiOperation(value = "系统用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "uuid", value = "uuid", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/login")
    public ResultUtils login(String username, String password, String code, String uuid) {
        return service.login(username, password, code, uuid);
    }

    @ApiOperation(value = "系统用户退出登录")
    @AdminAuthToken
    @PostMapping("/logout")
    @SysUserLog(description = "系统用户退出登录", type = SysUserLog.TYPE_UPDATE)
    public ResultUtils logout() {
        return service.logout();
    }

}
