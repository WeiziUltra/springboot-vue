package com.weiziplus.pc.core.datadictionary;

import com.weiziplus.common.core.datadictionary.DataDictionaryIpManagerService;
import com.weiziplus.common.models.DataDictionaryValue;
import com.weiziplus.common.util.PageUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.common.util.token.AdminTokenUtils;
import com.weiziplus.pc.common.config.MyGlobalConfig;
import com.weiziplus.pc.common.interceptor.AdminAuthToken;
import com.weiziplus.pc.common.interceptor.SysUserLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wanglongwei
 * @date 2020/05/29 09/54
 */
@RestController
@AdminAuthToken
@RequestMapping("/ipFilter")
@Api(tags = "ip过滤")
public class DataDictionaryIpManagerController {

    @Autowired
    DataDictionaryIpManagerService service;

    @ApiOperation(value = "查看ip过滤规则")
    @GetMapping("/getIpFilterRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型", dataType = "String", paramType = "query")
    })
    @SysUserLog(description = "查看ip过滤规则")
    public ResultUtils<String> getIpFilterRole(String type) {
        return service.getIpFilterRole(type);
    }

    @ApiOperation(value = "更新ip规则")
    @PostMapping("/updateIpFilterRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "role", value = "规则", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "类型", dataType = "String", paramType = "form"),
    })
    @SysUserLog(description = "更新ip规则", type = SysUserLog.TYPE_UPDATE)
    public ResultUtils updateIpFilterRole(String role, String type) {
        //只有超级管理员才可以操作功能
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Integer userIdByHttpServletRequest = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(userIdByHttpServletRequest)) {
            return ResultUtils.errorRole("您没有权限");
        }
        return service.updateIpFilterRole(role, type);
    }

    @ApiOperation(value = "查看ip过滤名单")
    @GetMapping("/getIpFilterList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "terminal", value = "终端", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "search", value = "搜索", dataType = "String", paramType = "query"),
    })
    @SysUserLog(description = "查看ip过滤名单")
    public ResultUtils<PageUtils<DataDictionaryValue>> getIpFilterList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String terminal, String search) {
        return service.getIpFilterList(pageNum, pageSize, terminal, search);
    }

    @ApiOperation(value = "新增ip过滤名单")
    @PostMapping("/addIpFilterList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "terminal", value = "终端", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "类型", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "ipAddress", value = "ip地址", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "String", paramType = "form"),
    })
    @SysUserLog(description = "新增ip过滤名单", type = SysUserLog.TYPE_INSERT)
    public ResultUtils addIpFilterList(String terminal, String type, String ipAddress, String remark) {
        return service.addIpFilterList(terminal, type, ipAddress, remark);
    }

    @ApiOperation(value = "删除ip过滤名单")
    @PostMapping("/deleteIpFilterList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Integer", paramType = "form"),
    })
    @SysUserLog(description = "删除ip过滤名单", type = SysUserLog.TYPE_DELETE)
    public ResultUtils deleteIpFilterList(Integer id) {
        return service.deleteIpFilterList(id);
    }

}
