package com.weiziplus.web.core.user.controller;

import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.web.common.interceptor.UserLog;
import com.weiziplus.web.common.interceptor.WebAuthToken;
import com.weiziplus.web.core.user.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanglongwei
 * @date 2020/06/01 08/52
 */
@RestController
@RequestMapping()
@Api(tags = "登录")
public class LoginController {

    @Autowired
    LoginService service;

    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/login")
    public ResultUtils login(String username, String password) {
        return service.login(username, password);
    }

    @ApiOperation(value = "用户退出登录")
    @WebAuthToken
    @PostMapping("/logout")
    @UserLog(description = "用户退出登录", type = UserLog.TYPE_UPDATE)
    public ResultUtils logout() {
        return service.logout();
    }

}
