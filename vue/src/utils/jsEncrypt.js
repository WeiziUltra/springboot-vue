/*引入jsencrypt*/
import JsEncrypt from 'jsencrypt';

/**
 * rsa公钥加密
 * @param key
 * @param obj
 * @returns {PromiseLike<ArrayBuffer> | *}
 */
function rsaPublicEncrypt(key, obj) {
    let encrypt = new JsEncrypt();
    //设置加密公钥
    encrypt.setPublicKey(key);
    //返回通过encryptLong方法加密后的结果
    return encrypt.encrypt(obj);
}

/**
 * 将方法暴露出去
 */
export default {
    rsaPublicEncrypt
};