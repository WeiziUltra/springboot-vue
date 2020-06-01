package com.weiziplus.pc.core.system.service;

import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.models.SysFunction;
import com.weiziplus.common.models.SysUser;
import com.weiziplus.common.models.SysUserRole;
import com.weiziplus.common.util.*;
import com.weiziplus.common.util.redis.RedisUtils;
import com.weiziplus.common.util.token.AdminTokenUtils;
import com.weiziplus.common.util.token.ExpandModel;
import com.weiziplus.pc.common.config.MyGlobalConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author wanglongwei
 * @date 2019/5/9 11:08
 */
@Slf4j
@Service
public class LoginService extends BaseService {

    @Autowired
    SysRoleFunctionService sysRoleFunctionService;

    @Autowired
    SysFunctionService sysFunctionService;

    /**
     * 唯一redis前缀
     */
    private final String REDIS_KEY = createOnlyRedisKeyPrefix();

    /**
     * 生成图片验证码
     *
     * @param uuid
     * @return
     */
    public void getValidateCode(String uuid) {
        if (ToolUtils.isBlank(uuid)) {
            return;
        }
        ImageValidateCodeUtils.ValidateCode validateCode = ImageValidateCodeUtils.getValidateCode();
        String redisKey = REDIS_KEY + uuid;
        RedisUtils.set(redisKey, validateCode.getRandom(), 60L);
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        //设置相应类型,告诉浏览器输出的内容为图片
        response.setContentType("image/jpeg");
        //设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        try {
            ImageIO.write(validateCode.getBufferedImage(), "JPEG", response.getOutputStream());
        } catch (IOException e) {
            log.warn("登录时生成图片验证码失败,详情:" + e);
            HttpRequestUtils.handleErrorResponse(
                    response, ResultUtils.error("登录时生成图片验证码失败，详情:" + e), "登录时生成图片验证码失败");
        }
    }

    /**
     * 系统用户登录
     *
     * @param username
     * @param password
     * @param code
     * @param uuid
     * @return
     */
    public ResultUtils login(String username, String password, String code, String uuid) {
        if (ToolUtils.isBlank(username) || ToolUtils.isBlank(password)) {
            return ResultUtils.error("用户名或密码为空");
        }
        if (ToolUtils.isBlank(code) || ImageValidateCodeUtils.RANDOM_NUM != code.length()) {
            return ResultUtils.error("验证码错误");
        }
        if (ToolUtils.isBlank(uuid)) {
            return ResultUtils.error("uuid为空");
        }
        String redisKey = REDIS_KEY + uuid;
        String randomCode = ToolUtils.valueOfString(RedisUtils.get(redisKey));
        if (null == randomCode) {
            return ResultUtils.error("验证码已过期");
        }
        if (!code.equalsIgnoreCase(randomCode)) {
            RedisUtils.delete(redisKey);
            return ResultUtils.error("验证码错误");
        }
        SysUser sysUser = baseFindOneDataByClassAndColumnAndValue(SysUser.class, SysUser.COLUMN_USERNAME, username);
        if (null == sysUser || !sysUser.getPassword().equals(Md5Utils.encode(password))) {
            return ResultUtils.error("用户名或密码错误");
        }
        if (SysUser.Status.DISABLE.getValue().equals(sysUser.getStatus())) {
            return ResultUtils.error("账号被禁用，如有疑问请联系管理员");
        }
        List<SysUserRole> sysUserRoleList = baseFindListByClassAndColumnAndValue(SysUserRole.class, SysUserRole.COLUMN_USER_ID, sysUser.getId());
        if (null == sysUserRoleList || 0 >= sysUserRoleList.size()) {
            return ResultUtils.error("您还没有角色，请联系管理员添加");
        }
        List<Integer> roleIdList = new ArrayList<>(ToolUtils.initialCapacity(sysUserRoleList.size()));
        for (SysUserRole sysUserRole : sysUserRoleList) {
            roleIdList.add(sysUserRole.getRoleId());
        }
        ExpandModel expandModel = new ExpandModel()
                .setUserName(sysUser.getUsername())
                .setRoleIdList(roleIdList);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = AdminTokenUtils.createToken(sysUser.getId(), request, expandModel);
        if (null == token) {
            return ResultUtils.error("登录失败，请重试");
        }
        sysUser.setPassword(null)
                .setIcon(MyGlobalConfig.getMybatisFilePathPrefix() + sysUser.getIcon());
        List<SysFunction> menuTreeByRoleIdList;
        Set<String> buttonNameListByRoleIdList;
        //如果是超级管理员，展示所有
        if (MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(sysUser.getId())) {
            menuTreeByRoleIdList = sysFunctionService.getAllMenuTree();
            List<SysFunction> sysFunctionList = baseFindListByClassAndColumnAndValue(
                    SysFunction.class, SysFunction.COLUMN_TYPE, SysFunction.Type.BUTTON.getValue());
            buttonNameListByRoleIdList = new HashSet<>(ToolUtils.initialCapacity(sysFunctionList.size()));
            for (SysFunction sysFunction : sysFunctionList) {
                buttonNameListByRoleIdList.add(sysFunction.getName());
            }
        } else {
            menuTreeByRoleIdList = sysRoleFunctionService.getMenuTreeByRoleIdList(roleIdList);
            buttonNameListByRoleIdList = sysRoleFunctionService.getButtonNameListByRoleIdList(roleIdList);
        }
        Map<String, Object> map = new HashMap<>(ToolUtils.initialCapacity(5));
        map.put("token", token);
        map.put("userInfo", sysUser);
        map.put("roleIdList", roleIdList);
        map.put("menuTree", menuTreeByRoleIdList);
        map.put("buttonList", buttonNameListByRoleIdList);
        return ResultUtils.success(map);
    }

    /**
     * 退出登录
     *
     * @return
     */
    public ResultUtils logout() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Integer userId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        AdminTokenUtils.deleteToken(userId);
        return ResultUtils.success();
    }

}
