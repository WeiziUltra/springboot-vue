package com.weiziplus.common.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * MD5工具类
 *
 * @author wanglongwei
 * @date 2020/05/27 15/20
 */
public class Md5Utils {
    /**
     * MD5加密盐
     */
    private static final String SALT = "WEIZIPLUS";

    /**
     * 32位
     *
     * @param str
     * @return
     */
    public static String encode(String str) {
        //在字符串基础上添加加密盐
        str = SALT + str;
        return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8)).toUpperCase();
    }

    /**
     * 16位
     *
     * @param str
     * @return
     */
    public static String encode16(String str) {
        String encode = encode(str);
        return encode.substring(8, 24);
    }

    /**
     * 32位没有加密盐
     *
     * @param str
     * @return
     */
    public static String encodeNotSalt(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8)).toUpperCase();
    }

}