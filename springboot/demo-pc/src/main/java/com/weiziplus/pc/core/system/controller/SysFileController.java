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
import org.springframework.web.multipart.MultipartFile;

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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "String", paramType = "query"),
    })
    @SysUserLog(description = "获取日志文件")
    public ResultUtils<List<LogFileVo>> getLogFile(String type) {
        return service.getLogFile(type);
    }

    @ApiOperation(value = "下载日志文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dir", value = "目录", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "文件名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/downLogFile")
    @SysUserLog(description = "下载日志文件")
    public void downLogFile(String dir, String name, String type) {
        service.downLogFile(dir, name, type);
    }

    @ApiOperation(value = "获取常用文件")
    @GetMapping("/getFile")
    @SysUserLog(description = "获取常用文件")
    public ResultUtils<List<LogFileVo>> getFile() {
        return service.getFile();
    }

    @ApiOperation(value = "更新常用文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "文件url", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/uploadFile")
    public ResultUtils uploadFile(MultipartFile file, String url) {
        return service.uploadFile(file, url);
    }

}
