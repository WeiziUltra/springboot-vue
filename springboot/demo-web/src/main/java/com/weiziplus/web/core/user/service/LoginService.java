package com.weiziplus.web.core.user.service;

import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.models.User;
import com.weiziplus.common.util.Md5Utils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.common.util.ToolUtils;
import com.weiziplus.common.util.token.ExpandModel;
import com.weiziplus.common.util.token.WebTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wanglongwei
 * @date 2020/06/01 08/53
 */
@Slf4j
@Service
public class LoginService extends BaseService {

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    public ResultUtils login(String username, String password) {
        if (ToolUtils.isBlank(username)) {
            return ResultUtils.error("用户名不能为空");
        }
        if (ToolUtils.isBlank(password)) {
            return ResultUtils.error("密码不能为空");
        }
        User user = baseFindOneDataByClassAndColumnAndValue(
                User.class, User.COLUMN_USERNAME, username);
        if (null == user) {
            return ResultUtils.error("用户不存在");
        }
        if (!user.getPassword().equals(Md5Utils.encode(password))) {
            return ResultUtils.error("用户名或密码错误");
        }
        if (User.Status.DISABLE.getValue().equals(user.getStatus())) {
            return ResultUtils.error("账户被停用，如有疑问请联系管理员");
        }
        ExpandModel expandModel = new ExpandModel();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = WebTokenUtils.createToken(user.getId(), request, expandModel);
        if (null == token) {
            return ResultUtils.error("登录失败，请重试");
        }
        user.setPassword(null);
        Map<String, Object> map = new HashMap<>(ToolUtils.initialCapacity(2));
        map.put("token", token);
        map.put("userInfo", user);
        return ResultUtils.success(map);
    }

    /**
     * 用户退出登录
     *
     * @return
     */
    public ResultUtils logout() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Long userIdByHttpServletRequest = WebTokenUtils.getUserIdByHttpServletRequest(request);
        WebTokenUtils.deleteToken(userIdByHttpServletRequest);
        return ResultUtils.success();
    }

}