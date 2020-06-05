package com.weiziplus.pc.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.weiziplus.common.async.LogAsync;
import com.weiziplus.common.async.UserAsync;
import com.weiziplus.common.util.*;
import com.weiziplus.common.util.redis.StringRedisUtils;
import com.weiziplus.common.util.token.AdminTokenUtils;
import com.weiziplus.common.util.token.JwtTokenUtils;
import com.weiziplus.pc.common.config.MyGlobalConfig;
import com.weiziplus.pc.core.system.service.SysRoleFunctionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Console;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author wanglongwei
 * @date 2020/05/28 08/30
 */
@Slf4j
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    UserAsync userAsync;

    @Autowired
    LogAsync logAsync;

    @Autowired
    SysRoleFunctionService sysRoleFunctionService;

    /**
     * 是否检验时间戳
     */
    @Value("${global.check-timeStamp:true}")
    private Boolean checkTimestamp = true;

    /**
     * 配置忽略的url
     */
    private final Set<String> IGNORE_URL = new HashSet<>(Arrays.asList("/dingTalk/callback"));

    /**
     * 处理请求的时间戳
     * 时间戳错误返回false
     *
     * @param request
     */
    private boolean handleTimeStamp(HttpServletRequest request) {
        String referer = request.getHeader(HttpHeaders.REFERER);
        //如果不是生产环境，并且请求的是swagger-ui，不检测时间戳
        if (!MyGlobalConfig.isSpringProfilesPro()
                && !ToolUtils.isBlank(referer)
                && referer.contains("/doc.html")) {
            return true;
        }
        String timeStamp = request.getParameter("__t");
        if (null == timeStamp || 0 >= timeStamp.length()) {
            return false;
        }
        long timeMillis = System.currentTimeMillis();
        Long timeStampLong = Long.valueOf(timeStamp);
        int allowTime = 180000;
        //如果请求时间戳和服务器当前时间相差超过3分钟，本次请求失败
        return allowTime > Math.abs(timeMillis - timeStampLong);
    }

    /**
     * 判断token注解
     *
     * @param object
     * @return
     */
    private boolean handleTokenAnnotation(Object object) {
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //如果有忽略token注解
        if (null != handlerMethod.getBeanType().getAnnotation(AuthTokenIgnore.class)
                || null != method.getAnnotation(AuthTokenIgnore.class)) {
            return true;
        }
        //检查class或者方法是否有@AdminAuthToken和@WebAuthToken，没有的话跳过拦截
        AdminAuthToken adminAuthTokenClass = handlerMethod.getBeanType().getAnnotation(AdminAuthToken.class);
        AdminAuthToken adminAuthTokenMethod = method.getAnnotation(AdminAuthToken.class);
        return null == adminAuthTokenClass && null == adminAuthTokenMethod;
    }

    /**
     * 判断token是否过期
     *
     * @param request
     * @return
     */
    private boolean handleJwtTokenNotExpiration(HttpServletRequest request) {
        //获取头部的token
        String token = request.getHeader(MyGlobalConfig.TOKEN);
        if (ToolUtils.isBlank(token)) {
            return false;
        }
        try {
            //判断jwtToken是否过期
            if (JwtTokenUtils.isExpiration(token)) {
                return false;
            }
        } catch (Exception e) {
            log.warn("拦截器判断jwtToken是否过期出错，详情:" + e);
            return false;
        }
        //获取token中存放的issuer
        String issuer = JwtTokenUtils.getIssuer(token);
        return JwtTokenUtils.createIssuer(request).equals(issuer);
    }

    /**
     * 请求之前拦截
     *
     * @param request
     * @param response
     * @param object
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        //springboot中tomcat内部处理404请求,放过
        String errorPath = "/error";
        if (errorPath.equals(request.getRequestURI())) {
            return true;
        }
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        //获取当前访问的url
        String requestUrl = request.getRequestURI();
        //如果有统一的请求前缀
        String servletContextPath = MyGlobalConfig.getServletContextPath();
        if (!ToolUtils.isBlank(MyGlobalConfig.getServletContextPath())) {
            int index = requestUrl.indexOf(servletContextPath);
            //并且是请求前缀开头的
            if (0 == index) {
                requestUrl = requestUrl.substring(servletContextPath.length());
            }
        }
        //如果是忽略的
        if (IGNORE_URL.contains(requestUrl)) {
            return true;
        }
        //开启时间戳校验
        if (checkTimestamp) {
            //判断时间戳有效
            if (!handleTimeStamp(request)) {
                handleResponse(response, ResultUtils.error("时间戳错误"));
                return false;
            }
        }
        //判断是否存在token注解
        if (handleTokenAnnotation(object)) {
            return true;
        }
        //判断请求头中token是否失效
        if (!handleJwtTokenNotExpiration(request)) {
            handleResponse(response, ResultUtils.errorToken("token失效"));
            return false;
        }
        //获取token
        String token = request.getHeader(MyGlobalConfig.TOKEN);
        //获取当前角色
        String tokenAudience = JwtTokenUtils.getUserAudienceByToken(token);
        //角色不是admin
        if (!AdminTokenUtils.AUDIENCE.equals(tokenAudience)) {
            handleResponse(response, ResultUtils.errorToken("token不存在"));
            return false;
        }
        //保存用户操作，校验用户权限等
        if (!handleAdminToken(request, response, object)) {
            return false;
        }
        //校验签名
        return handleSign(request, response, object);
    }

    /**
     * 处理admin的token
     *
     * @param request
     * @param response
     * @param object
     * @return
     */
    private boolean handleAdminToken(HttpServletRequest request, HttpServletResponse response, Object object) {
        //获取token
        String token = request.getHeader(MyGlobalConfig.TOKEN);
        //获取用户id
        Integer userId = AdminTokenUtils.getUserIdByToken(token);
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //判断当前注解是否和当前角色匹配
        if (null == handlerMethod.getBeanType().getAnnotation(AdminAuthToken.class)
                && null == method.getAnnotation(AdminAuthToken.class)) {
            handleResponse(response, ResultUtils.errorToken("token失效"));
            return false;
        }
        //查看redis是否过期
        if (!StringRedisUtils.hasKye(AdminTokenUtils.getAudienceRedisKey(userId))) {
            handleResponse(response, ResultUtils.errorToken("token失效"));
            return false;
        }
        //查看redis中token是否是当前token
        if (!StringRedisUtils.get(AdminTokenUtils.getAudienceRedisKey(userId)).equals(token)) {
            handleResponse(response, ResultUtils.errorToken("token失效"));
            return false;
        }
        ////////////token验证成功
        //查看是否有日志注解，有的话将日志信息放入数据库
        SysUserLog systemLog = method.getAnnotation(SysUserLog.class);
        if (null != systemLog) {
            //查看是否存在忽略参数
            String paramIgnore = systemLog.paramIgnore();
            Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
            //使用迭代器的remove()方法删除元素
            parameterMap.keySet().removeIf(paramIgnore::contains);
            com.weiziplus.common.models.SysUserLog log = new com.weiziplus.common.models.SysUserLog()
                    .setUserId(userId)
                    .setUrl(request.getRequestURI())
                    .setParam(JSON.toJSONString(parameterMap))
                    .setType(systemLog.type())
                    .setDescription(systemLog.description())
                    .setIpAddress(HttpRequestUtils.getIpAddress(request))
                    .setBorderName(UserAgentUtils.getBorderName(request))
                    .setOsName(UserAgentUtils.getOsName(request));
            //将日志异步放入数据库
            logAsync.saveSysUserLog(log);
        }
        //异步更新用户最后活跃时间
        userAsync.updateAdminUserLastActiveTime(userId, HttpRequestUtils.getIpAddress(request));
        List<Integer> roleIdList = JwtTokenUtils.getExpandModel(request).getRoleIdList();
        //如果当前是超级管理员---直接放过
        if (MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(userId)
                || roleIdList.contains(MyGlobalConfig.SYS_ROLE_SUPER_ADMIN_ID)) {
            //更新token过期时间
            AdminTokenUtils.updateExpireTime(userId);
            return true;
        }
        //获取当前访问的url
        String requestUrl = request.getRequestURI();
        //如果有统一的请求前缀
        String servletContextPath = MyGlobalConfig.getServletContextPath();
        if (!ToolUtils.isBlank(MyGlobalConfig.getServletContextPath())) {
            int index = requestUrl.indexOf(servletContextPath);
            //并且是请求前缀开头的
            if (0 == index) {
                requestUrl = requestUrl.substring(servletContextPath.length());
            }
        }
        Set<String> allFunContainApi = sysRoleFunctionService.getAllFunContainApi();
        //如果限制的功能api不包含当前url---直接放过
        if (null == allFunContainApi || !allFunContainApi.contains(requestUrl)) {
            //更新token过期时间
            AdminTokenUtils.updateExpireTime(userId);
            return true;
        }
        //获取当前角色拥有的方法url
        Set<String> funContainApiListByRoleIdList = sysRoleFunctionService.getFunContainApiListByRoleIdList(roleIdList);
        if (null != funContainApiListByRoleIdList && funContainApiListByRoleIdList.contains(requestUrl)) {
            //更新token过期时间
            AdminTokenUtils.updateExpireTime(userId);
            return true;
        }
        handleResponse(response, ResultUtils.errorRole("您没有权限"));
        return false;
    }

    /**
     * 是否需要签名处理
     * 所有请求参数（排除__sign）需要按照ascii排序，然后进行MD5加密
     *
     * @param request
     * @param response
     * @param object
     * @return
     */
    private boolean handleSign(HttpServletRequest request, HttpServletResponse response, Object object) {
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //如果没有签名注解直接返回
        if (null == handlerMethod.getBeanType().getAnnotation(SignValidation.class)
                && null == method.getAnnotation(SignValidation.class)) {
            return true;
        }
        TreeMap<String, String[]> stringTreeMap = new TreeMap<>(request.getParameterMap());
        Set<String> keySet = stringTreeMap.keySet();
        String signParam = "__sign";
        if (!keySet.contains(signParam)) {
            handleResponse(response, ResultUtils.error("签名错误"));
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : keySet) {
            //跳过__sign参数
            if (signParam.equals(key)) {
                continue;
            }
            stringBuilder.append(key).append("=").append(stringTreeMap.get(key)[0]).append("&");
        }
        String s = Md5Utils.encodeNotSalt(stringBuilder.substring(0, stringBuilder.length() - 1));
        if (stringTreeMap.get(signParam)[0].equals(s)) {
            return true;
        }
        handleResponse(response, ResultUtils.error("签名错误"));
        return false;
    }

    /**
     * 将token出错信息输入到前端页面
     *
     * @param response
     * @param errResult
     */
    private void handleResponse(HttpServletResponse response, ResultUtils errResult) {
        HttpRequestUtils.handleErrorResponse(response, errResult, "token出错");
    }

}