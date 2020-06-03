package com.weiziplus.common.util.token;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weiziplus.common.config.GlobalConfig;
import com.weiziplus.common.util.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author wanglongwei
 * @date 2020/05/27 15/41
 */
public class JwtTokenUtils {

    /**
     * 发行人
     */
    private static final String ISSUER = "WEIZIPLUS";
    /**
     * 加密方式
     */
    private static final SignatureAlgorithm HS512 = SignatureAlgorithm.HS512;
    /**
     * 秘钥
     */
    private static final String SECRET = "WEIZIPLUS";
    /**
     * 过期时间--30天过期
     */
    private static final long EXPIRATION = 1000L * 60 * 60 * 24 * 30;

    /**
     * 根据用户id和用户类型创建token
     *
     * @param userId
     * @return
     */
    protected static String createToken(String userId, String audience, HttpServletRequest request, ExpandModel expand) {
        return Jwts.builder()
                //用户id
                .setId(Base64Utils.encode(userId))
                .setIssuer(createIssuer(request))
                //用户类型，admin还是web
                .setAudience(Base64Utils.encode(audience))
                .signWith(HS512, SECRET)
                //存放自定义内容
                .setSubject(Base64Utils.encode(JSON.toJSONString(expand)))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .setIssuedAt(new Date())
                .compact();
    }

    /**
     * 创建issuer
     * ip地址和浏览器
     *
     * @param request
     * @return
     */
    public static String createIssuer(HttpServletRequest request) {
        String ipAddress = HttpRequestUtils.getIpAddress(request);
        String borderName = UserAgentUtils.getBorderName(request);
        return Md5Utils.encode16(ipAddress + borderName);
    }

    /**
     * 获取token中的issuer
     *
     * @param token
     * @return
     */
    public static String getIssuer(String token) {
        return getTokenBody(token).getIssuer();
    }

    /**
     * 根据token判断是否失效
     *
     * @param token
     * @return
     */
    public static Boolean isExpiration(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }

    /**
     * 根据token获取token中的信息
     *
     * @param token
     * @return
     */
    protected static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 根据token获取用户Audience
     *
     * @param token
     * @return
     */
    public static String getUserAudienceByToken(String token) {
        return Base64Utils.decode(getTokenBody(token).getAudience());
    }

    /**
     * 获取自定义内容
     *
     * @param token
     * @return
     */
    public static ExpandModel getExpandModel(String token) {
        return JSONObject.parseObject(Base64Utils.decode(getTokenBody(token).getSubject()), ExpandModel.class);
    }

    /**
     * 获取自定义内容
     *
     * @param request
     * @return
     */
    public static ExpandModel getExpandModel(HttpServletRequest request) {
        String token = request.getHeader(GlobalConfig.TOKEN);
        return JSONObject.parseObject(Base64Utils.decode(getTokenBody(token).getSubject()), ExpandModel.class);
    }

    /**
     * 获取id
     *
     * @param token
     * @return
     */
    protected static String getId(String token) {
        return Base64Utils.decode(getTokenBody(token).getId());
    }

}