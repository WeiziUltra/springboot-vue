package com.weiziplus.common.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加密解密
 *
 * @author wanglongwei
 * @date 2020/06/03 18/09
 */
@Slf4j
@Getter
public class RsaUtils {

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    private RsaUtils(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * 标识RSA加密算法名字常量
     */
    private final static String RSA = "RSA";

    /**
     * 获取公钥私钥对
     *
     * @return
     */
    public static RsaUtils getKeyPair() {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance(RSA);
        } catch (NoSuchAlgorithmException e) {
            log.warn("RSA生成公钥私钥对出错，详情:" + e);
            return null;
        }
        // 初始化密钥对生成器
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //得到公钥字符串
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        // 得到私钥字符串
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        return new RsaUtils(publicKeyString, privateKeyString);
    }

    /**
     * 私钥解密
     *
     * @param privateKey
     * @param content
     * @return
     */
    public static String privateDecrypt(String privateKey, String content) {
        try {
            //64位解码加密后的字符串
            byte[] inputByte = Base64.getDecoder().decode(content);
            //base64编码的私钥
            byte[] decoded = Base64.getDecoder().decode(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(RSA).generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            return new String(cipher.doFinal(inputByte), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("RSA私钥解密出错，详情:" + e);
            return null;
        }
    }

    /**
     * 公钥加密
     *
     * @param publicKey
     * @param content
     * @return
     */
    public static String publicEncrypt(String publicKey, String content) {
        try {
            //base64编码的公钥
            byte[] decoded = Base64.getDecoder().decode(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(RSA).generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.warn("RSA公钥加密出错，详情:" + e);
            return null;
        }
    }

}
