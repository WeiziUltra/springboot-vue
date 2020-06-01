package com.weiziplus.common.util.token;

import com.weiziplus.common.config.GlobalConfig;
import com.weiziplus.common.util.ToolUtils;
import com.weiziplus.common.util.redis.StringRedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统用户token配置
 *
 * @author wanglongwei
 * @date 2019/5/7 9:53
 */
@Component
public class AdminTokenUtils extends JwtTokenUtils {

    /**
     * 系统用户
     */
    public final static String AUDIENCE = "admin";

    /**
     * 系统用户redis过期时间--3小时过期
     */
    private static Long EXPIRE_TIME = 60L * 60 * 3;

    @Value("${global.token.expire-time.admin:10800}")
    private void setExpireTime(String expireTime) {
        AdminTokenUtils.EXPIRE_TIME = ToolUtils.valueOfLong(expireTime);
    }

    /**
     * 根据系统用户id获取系统用户Redis的key值
     *
     * @param userId
     * @return
     */
    public static String getAudienceRedisKey(Integer userId) {
        return TokenUtils.getAudienceRedisKey(AUDIENCE, String.valueOf(userId));
    }

    /**
     * 根据系统用户id创建token
     *
     * @param userId
     * @return
     */
    public static String createToken(Integer userId, HttpServletRequest request, ExpandModel expandModel) {
        return TokenUtils.createToken(AUDIENCE, String.valueOf(userId), EXPIRE_TIME, request, expandModel);
    }

    /**
     * 根据系统用户id更新token过期时间
     *
     * @param userId
     * @return
     */
    public static Boolean updateExpireTime(Integer userId) {
        return StringRedisUtils.expire(getAudienceRedisKey(userId), EXPIRE_TIME);
    }

    /**
     * 根据系统用户id删除token
     *
     * @param userId
     * @return
     */
    public static Boolean deleteToken(Integer userId) {
        return StringRedisUtils.delete(getAudienceRedisKey(userId));
    }

    /**
     * 根据token获取用户id
     *
     * @param token
     * @return
     */
    public static Integer getUserIdByToken(String token) {
        return Integer.valueOf(getId(token));
    }

    /**
     * 根据request获取用户id
     *
     * @param request
     * @return
     */
    public static Integer getUserIdByHttpServletRequest(HttpServletRequest request) {
        return Integer.valueOf(getId(request.getHeader(GlobalConfig.TOKEN)));
    }

}