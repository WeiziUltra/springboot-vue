package com.weiziplus.web.core.user.controller;

import com.weiziplus.common.models.User;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.web.common.interceptor.UserLog;
import com.weiziplus.web.common.interceptor.WebAuthToken;
import com.weiziplus.web.core.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanglongwei
 * @date 2020/06/01 08/51
 */
@WebAuthToken
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {

    @Autowired
    UserService service;

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getInfo")
    @UserLog(description = "获取用户信息")
    public ResultUtils<User> getInfo() {
        return service.getInfo();
    }

}
