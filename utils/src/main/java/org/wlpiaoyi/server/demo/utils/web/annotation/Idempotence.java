package org.wlpiaoyi.server.demo.utils.web.annotation;

import java.lang.annotation.*;
/**
 * <p<b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>
 * 幂等性
 * </p>
 * <p><b>{@code @date:}</b>2023/2/16 18:18</p>
 * <p><b>{@code @version:}</b>1.0</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotence {

    /**
     * 5秒钟内访问超过5次
     */
    //=========================>
    int duriSecond() default 5;

    int count() default 5;
    //<=========================

    /**
     * 锁定10分钟
     */
    int deadLockMinutes() default 1;

}
