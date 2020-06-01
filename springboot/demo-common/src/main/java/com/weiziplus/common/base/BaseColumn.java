package com.weiziplus.common.base;

import java.lang.annotation.*;

/**
 * 自定义注解 数据库表的字段
 *
 * @author wanglongwei
 * @date 2020/05/27 16/19
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BaseColumn {
    String value();
}
