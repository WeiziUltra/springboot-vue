package com.weiziplus.common.base;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * sql字段与条件和值
 *
 * @author wanglongwei
 * @date 2020/06/03 14/57
 */
@Getter
@Accessors(chain = true)
public class BaseWhere implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库字段
     */
    private String column;

    /**
     * where条件
     */
    private String where = "=";

    /**
     * 值
     */
    private Object value;

    public BaseWhere() {

    }

    /**
     * 创建where
     *
     * @param column
     * @param where
     * @param value
     */
    public BaseWhere(String column, String where, Object value) {
        this.column = column;
        this.where = where;
        this.value = value;
    }

    @Getter
    public enum Where {
        /**
         * sql条件
         */
        EQUAL("等于", "="),
        NOT_EQUAL("不等于", "<![CDATA[<>]]>"),
        MORE_THAN("大于", "<![CDATA[>]]>"),
        LESS_THAN("小于", "<![CDATA[<]]>"),
        MORE_THAN_EQUAL("大于等于", "<![CDATA[>=]]>"),
        LESS_THAN_EQUAL("小于等于", "<![CDATA[<=]]>"),
        IN("IN", "IN"),
        NOT_IN("NOT_IN", "NOT IN"),
        POSITION("POSITION", "POSITION");

        private String name;
        private String value;

        Where(String name, String value) {
            this.name = name;
            this.value = value;
        }

        /**
         * 是否存在
         *
         * @param value
         * @return
         */
        public static boolean contains(String value) {
            for (Where where : Where.values()) {
                if (where.getValue().equals(value)) {
                    return true;
                }
            }
            return false;
        }
    }

}