package com.weiziplus.common.base;

import java.lang.annotation.*;

/**
 * 自定义注解 数据库表名
 *
 * @author wanglongwei
 * @date 2020/05/27 16/20
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BaseTable {
    String value();
}
