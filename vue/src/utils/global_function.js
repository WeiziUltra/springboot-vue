/**引入element-ui组件*/
import {Message, MessageBox, prompt} from 'element-ui';
/*引入AES加密*/
import CryptoJs from "crypto-js";
/*引入jsencrypt*/
import JsEncrypt from 'jsencrypt';

//某种程度上加混淆
let {enc, mode, pad} = CryptoJs;
let A_E_S = CryptoJs['AES'];

/**
 * 判断是否为空
 * @param str
 * @returns {boolean}
 */
function isBlank(str) {
    if (null == str) {
        return true;
    }
    return '' === ('' + str).replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 判断不是手机号
 * @param phone
 * @returns {boolean}
 */
function notPhone(phone) {
    return !(/^1(3|4|5|6|7|8|9)\d{9}$/.test(phone));
}

/**
 * 错误警告
 * @param msg
 * @param time
 * @param dangerouslyUseHTMLString
 */
function errorMsg(msg = '失败', time = 3000, dangerouslyUseHTMLString = false) {
    Message({
        message: msg,
        type: 'error',
        dangerouslyUseHTMLString,
        duration: time
    });
}

/**
 * 成功提示
 * @param msg
 * @param time
 */
function successMsg(msg = '成功', time = 3000) {
    Message({
        message: msg,
        type: 'success',
        duration: time
    });
}

/////////////////////////////////
//ASE秘钥

function getA_E_S() {
    return {
        _k: 'WEIZI',
        _v: 'PLUS'
    }
}

let {_k, _v} = getA_E_S();

/**
 * 对话框
 * @param title  标题
 * @param message  内容
 * @param type  类型
 * @param center  居中布局
 * @param dangerouslyUseHTMLString  是否为html片段
 * @param showCancelButton  是否显示取消按钮
 * @param cancelButtonText  取消按钮文字
 * @param confirmButtonText  确定按钮文字
 * @param confirm 确定回调
 * @param cancel 取消回调
 * @returns {Promise<MessageBoxData>}
 */
function messageBox({
                        title = '警告',
                        message = '',
                        type = 'warning',
                        center = false,
                        dangerouslyUseHTMLString = false,
                        showCancelButton = true,
                        cancelButtonText = '取消',
                        confirmButtonText = '确定',
                        confirm = function () {

                        },
                        cancel = function () {

                        }
                    } = {}) {
    return MessageBox({
        title,
        message,
        type,
        center,
        dangerouslyUseHTMLString,
        showCancelButton,
        cancelButtonText,
        confirmButtonText,
        callback(action, instance) {
            if (action === 'confirm') {
                confirm(instance);
            } else {
                cancel(instance);
            }
        }
    });
}

/**
 * 对话框input输入框-----确定后需要调用done()关闭对话框
 * @param title  标题
 * @param message  内容
 * @param type  类型
 * @param center  居中布局
 * @param dangerouslyUseHTMLString  是否为html片段
 * @param showCancelButton  是否显示取消按钮
 * @param cancelButtonText  取消按钮文字
 * @param confirmButtonText  确定按钮文字
 * @param inputType  输入框类型
 * @param confirm 确定回调
 * @returns {Promise<MessageBoxData>}
 */
function messageBoxInput({
                             title = '警告',
                             message = '',
                             type = 'warning',
                             center = false,
                             dangerouslyUseHTMLString = false,
                             showCancelButton = true,
                             cancelButtonText = '取消',
                             confirmButtonText = '确定',
                             inputType = 'primary',
                             confirm = function () {

                             }
                         } = {}) {
    return MessageBox({
        title,
        message,
        type,
        center,
        dangerouslyUseHTMLString,
        showCancelButton,
        cancelButtonText,
        confirmButtonText,
        showInput: true,
        inputType,
        beforeClose(action, instance, done) {
            if (action === 'cancel') {
                done();
                return;
            }
            confirm(instance.inputValue, done);
        },
        callback(action, instance) {
        }
    });
}

/**
 * 生成uuid
 * @returns {string}
 */
function createUUID() {
    let s = [];
    let hexDigits = "0123456789abcdef";
    for (let i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    // bits 12-15 of the time_hi_and_version field to 0010
    s[14] = "4";
    // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
    s[8] = s[13] = s[18] = s[23] = "";
    return s.join("").toUpperCase();
}

/**
 * 将错误信息以console.table打印出来
 * @param msg
 * @param object
 */
function consoleWarnTable(msg, object = {}) {
    console.warn(msg);
    try {
        if (object instanceof Object) {
            console.table(object);
        } else {
            console.log(object);
        }
    } catch (e) {
        console.log('此浏览器不支持console.table()', e, '---错误详情:', object);
    }
    console.warn('↑↑以上为错误详情↑↑↑↑↑');
}

/**
 * 字符串反转
 * @param value
 */
function reverse(value) {
    if (null == value) {
        return null;
    }
    return value.split('').reverse().join('');
}

/**
 * 格式化时间戳
 * @param timestamp
 * @param format
 * @returns {string}
 */
function timestampFormat(timestamp, format = 'yyyy-MM-dd') {
    if (null == timestamp || 0 > timestamp) {
        return '';
    }
    let date = new Date(parseInt(timestamp));
    let o = {
        "M+": date.getMonth() + 1, // 月份
        "d+": date.getDate(), // 日
        "h+": date.getHours(), // 小时
        "m+": date.getMinutes(), // 分
        "s+": date.getSeconds(), // 秒
        "q+": Math.floor((date.getMonth() + 3) / 3), // 季度
        "S": date.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (date.getFullYear() + ""));
    for (let k in o)
        if (new RegExp("(" + k + ")").test(format)) format = format.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return format;
}

/**
 * 获取今天日期
 * @returns {string}
 */
function getNowDate() {
    let date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    month = month > 9 ? month : '0' + month;
    let day = date.getDate();
    day = day > 9 ? day : '0' + day;
    return `${year}-${month}-${day}`;
}

/**
 * AES加密
 * @param word
 * @returns {string}
 */
function a_e_sEncrypt(word) {
    if (null == word) {
        return null;
    }
    let {Utf8} = enc;
    // aes加密，秘钥和偏移
    const KEY = Utf8.parse(_k);
    const IV = Utf8.parse(_v);
    let srcs = Utf8.parse(word);
    let encrypted = A_E_S.encrypt(srcs, KEY, {
        iv: IV,
        mode: mode.CBC,
        padding: pad.Pkcs7
    });
    return encrypted.ciphertext.toString().toUpperCase();
}

/**
 * AES解密
 */
function a_e_sDecrypt(word) {
    if (null == word) {
        return null;
    }
    let {Utf8, Base64} = enc;
    // aes加密，秘钥和偏移
    const KEY = Utf8.parse(_k);
    const IV = Utf8.parse(_v);
    let encryptedHexStr = enc.Hex.parse(word);
    let srcs = Base64.stringify(encryptedHexStr);
    let decrypt = A_E_S.decrypt(srcs, KEY, {
        iv: IV,
        mode: mode.CBC,
        padding: pad.Pkcs7
    });
    let decryptedStr = decrypt.toString(Utf8);
    return decryptedStr.toString();
}

/**
 * base64加密
 * @param word
 * @returns {string}
 */
function base64Encrypt(word) {
    if (null == word) {
        return null;
    }
    let wordArray = CryptoJs.enc.Utf8.parse(word);
    return CryptoJs.enc.Base64.stringify(wordArray);
}

/**
 * base64解密
 * @param wordArray
 * @returns {*}
 */
function base64Decrypt(wordArray) {
    if (null == wordArray) {
        return null;
    }
    let parsedWordArray = CryptoJs.enc.Base64.parse(wordArray);
    return parsedWordArray.toString(CryptoJs.enc.Utf8);
}

/**
 * md5加密
 * @param str
 * @returns {*}
 */
function md5(str) {
    if (null == str) {
        return null;
    }
    str = `weiziplus-${str}`;
    return CryptoJs.MD5(str).toString().toUpperCase();
}

/**
 * md5加密没有加密盐
 * @param str
 * @returns {*}
 */
function md5NoSalt(str) {
    if (null == str) {
        return null;
    }
    return CryptoJs.MD5(str).toString().toUpperCase();
}

/**
 * 获取session存储的数据
 * @param key
 * @returns {*}
 */
function getSessionStorage(key) {
    if (null == key) {
        return null;
    }
    key = `weiziplus-${key}`;
    return JSON.parse(sessionStorage.getItem(key));
}

/**
 * 将数据保存到session中
 * @param key
 * @param value
 */
function setSessionStorage(key, value = '') {
    if (null == key) {
        return;
    }
    key = `weiziplus-${key}`;
    sessionStorage.setItem(key, JSON.stringify(value));
}

/**
 * 获取location存储的数据
 * @param key
 * @returns {*}
 */
function getLocationStorage(key) {
    if (null == key) {
        return null;
    }
    key = `weiziplus-${key}`;
    return JSON.parse(localStorage.getItem(key));
}

/**
 * 将数据保存到location中
 * @param key
 * @param value
 */
function setLocationStorage(key, value = '') {
    if (null == key) {
        return;
    }
    key = `weiziplus-${key}`;
    localStorage.setItem(key, JSON.stringify(value));
}

/**
 * 对象进行ascii排序
 * @param obj
 * @returns {*}
 */
function sortAscii(obj) {
    if (null == obj) {
        return null;
    }
    let arr = [];
    let index = 0;
    for (let key in obj) {
        arr[index] = key;
        index++;
    }
    let str = '';
    arr.sort().forEach(value => {
        str += `${value}=${('' + obj[value]).replace(/(^\s*)|(\s*$)/g, "")}&`;
    });
    return str.substr(0, str.length - 1);
}

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
    isBlank,
    notPhone,
    errorMsg,
    successMsg,
    messageBox,
    messageBoxInput,
    createUUID,
    consoleWarnTable,
    reverse,
    timestampFormat,
    getNowDate,
    a_e_sEncrypt,
    a_e_sDecrypt,
    base64Encrypt,
    base64Decrypt,
    md5,
    md5NoSalt,
    getSessionStorage,
    setSessionStorage,
    getLocationStorage,
    setLocationStorage,
    sortAscii,
    rsaPublicEncrypt
};