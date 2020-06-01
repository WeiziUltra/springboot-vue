package com.weiziplus.common.util;


import java.util.*;

/**
 * 常用方法
 *
 * @author wanglongwei
 * @date 2020/05/27 14/46
 */
public class ToolUtils {


    /**
     * 字符串为null或""或"undefined"或"null"
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        if (null == str) {
            return true;
        }
        str = str.trim();
        return 0 >= str.length();
    }

    /**
     * 字符串不是null或""或"undefined"或"null"
     *
     * @param str
     * @return
     */
    public static boolean notBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 生成uuid
     *
     * @return
     */
    public static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 对象转为字符串
     *
     * @param object
     * @return
     */
    public static String valueOfString(Object object) {
        if (null == object) {
            return null;
        }
        return String.valueOf(object);
    }

    /**
     * 字符串转Integer
     *
     * @param string
     * @return
     */
    public static Integer valueOfInteger(String string) {
        if (null == string) {
            return null;
        }
        string = string.trim();
        if (0 >= string.length()) {
            return null;
        }
        return Integer.valueOf(string);
    }

    /**
     * 字符串转Double
     *
     * @param string
     * @return
     */
    public static Double valueOfDouble(String string) {
        if (null == string) {
            return null;
        }
        string = string.trim();
        if (0 >= string.length()) {
            return null;
        }
        return Double.valueOf(string);
    }

    /**
     * 字符串转Long
     *
     * @param string
     * @return
     */
    public static Long valueOfLong(String string) {
        if (null == string) {
            return null;
        }
        string = string.trim();
        if (0 >= string.length()) {
            return null;
        }
        return Long.valueOf(string);
    }

    /**
     * 将object对象转为list
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> objectOfList(Object obj, Class<T> clazz) {
        if (null == obj || null == clazz) {
            return null;
        }
        List<T> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    /**
     * 将object对象转为set
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Set<T> objectOfSet(Object obj, Class<T> clazz) {
        if (null == obj || null == clazz) {
            return null;
        }
        Set<T> result = new HashSet<>();
        if (obj instanceof Set<?>) {
            for (Object o : (Set<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    /**
     * 对字符串进行反转
     *
     * @param string
     * @return
     */
    public static String reverse(String string) {
        return new StringBuffer(string).reverse().toString();
    }

    /**
     * 创建集合初始化容量
     *
     * @param needNum
     * @return
     */
    public static int initialCapacity(int needNum) {
        //负载因子
        float loaderFactor = 0.75F;
        return (int) ((float) needNum * loaderFactor + 1);
    }

}
