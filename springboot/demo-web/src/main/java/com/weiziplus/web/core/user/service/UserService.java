package com.weiziplus.web.core.user.service;

import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.models.User;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.common.util.token.WebTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wanglongwei
 * @date 2020/06/01 09/09
 */
@Slf4j
@Service
public class UserService extends BaseService {

    /**
     * 获取用户信息
     *
     * @return
     */
    public ResultUtils<User> getInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Long userIdByHttpServletRequest = WebTokenUtils.getUserIdByHttpServletRequest(request);
        User user = baseFindByClassAndId(User.class, userIdByHttpServletRequest);
        return ResultUtils.success(user);
    }

}