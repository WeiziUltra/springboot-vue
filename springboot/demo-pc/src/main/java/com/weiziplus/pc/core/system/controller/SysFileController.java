package com.weiziplus.pc.core.system.controller;

import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.pc.common.interceptor.AdminAuthToken;
import com.weiziplus.pc.common.interceptor.SysUserLog;
import com.weiziplus.pc.core.system.service.SysFileService;
import com.weiziplus.pc.core.system.vo.LogFileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/06/05 08/35
 */
@RestController
@AdminAuthToken
@RequestMapping("/sysFile")
@Api(tags = "系统文件")
public class SysFileController {

    @Autowired
    SysFileService service;

    @ApiOperation(value = "获取日志文件")
    @GetMapping("/getLogFile")
    @SysUserLog(description = "获取日志文件")
    public ResultUtils<List<LogFileVo>> getLogFile() {
        return service.getLogFile();
    }

    @ApiOperation(value = "下载日志文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dir", value = "目录", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "文件名", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/downLogFile")
    @SysUserLog(description = "下载日志文件")
    public void downLogFile(String dir, String name) {
        service.downLogFile(dir, name);
    }

}
