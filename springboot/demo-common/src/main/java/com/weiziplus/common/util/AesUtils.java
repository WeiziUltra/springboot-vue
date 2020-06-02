package com.weiziplus.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * aes加密
 *
 * @author wanglongwei
 * @date 2020/05/09 11/43
 */
@Slf4j
public class AesUtils {

    /**
     * 标识AES加密算法名字常量
     */
    private final static String AES = "AES";

    /**
     * 生成密码器
     *
     * @param rule
     * @return
     * @throws Exception
     */
    private static SecretKey createSecretKeySpec(String rule) throws Exception {
        //1.构造密钥生成器，指定为AES算法,不区分大小写
        KeyGenerator keygen = KeyGenerator.getInstance(AES);
        //2.根据规格初始化秘钥生成器
        //生成一个128位的随机源,根据传入的字节数组
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(rule.getBytes(StandardCharsets.UTF_8));
        keygen.init(128, secureRandom);
        //3.产生原始对称密钥
        SecretKey secretKey = keygen.generateKey();
        //4.获得原始对称秘钥的字节数组
        byte[] raw = secretKey.getEncoded();
        //5.根据字节数组生成AES密钥
        return new SecretKeySpec(raw, AES);
    }

    /**
     * 加密
     *
     * @param rule
     * @param content
     * @return
     */
    private static String baseEncode(String rule, String content) {
        try {
            //5.根据字节数组生成AES密钥
            SecretKey key = createSecretKeySpec(rule);
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(AES);
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            //9.根据密码器的初始化方式--加密：将数据加密
            byte[] byteAes = cipher.doFinal(contentBytes);
            return new String(Base64.encodeBase64(byteAes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("aes加密出错，详情:" + e);
            return null;
        }
    }

    /**
     * 解密
     *
     * @param rule
     * @param content
     * @return
     */
    private static String baseDecode(String rule, String content) {
        try {
            //5.根据字节数组生成AES密钥
            SecretKey key = createSecretKeySpec(rule);
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(AES);
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] contentBytes = Base64.decodeBase64(content);
            byte[] bytes = cipher.doFinal(contentBytes);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("aes解密出错，详情:" + e);
            return null;
        }
    }

}