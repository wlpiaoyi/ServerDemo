package org.wlpiaoyi.server.demo.utils.web.annotation;

import java.lang.annotation.*;

/**
 * <p><b>{@code @author:}</b>         wlpiaoyi</p>
 * <p><b>{@code @description:}</b>
 * 解密数据
 * </p>
 * <p><b>{@code @date:}</b>           2024/10/13 9:08</p>
 * <p><b>{@code @version:}</b>       1.0</p>
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Decrypt {
}
