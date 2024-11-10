package org.wlpiaoyi.server.demo.common.core.loader;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.wlpiaoyi.server.demo.common.tools.web.annotation.PreAuthorize;
import org.wlpiaoyi.server.demo.common.core.web.support.impl.access.AccessUriSet;

import java.lang.reflect.Method;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-10 13:55:37</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
public class AccessLoader {


    /**
     * <p><b>{@code @description:}</b>
     * TODO
     * </p>
     *
     * <p><b>{@code @param}</b> <b>applicationContext</b>
     * {@link ApplicationContext}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>accessUriSet</b>
     * {@link AccessUriSet}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/10 14:00</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public static void load(ApplicationContext applicationContext, AccessUriSet accessUriSet) throws BeansException {
        AnnotationPathUtils.iteratorAllPath1(applicationContext, (value, path1) -> {
            path1 = AnnotationPathUtils.checkMappingValue(path1);
            Method[] methods = value.getClass().getMethods();
            AnnotationPathUtils.iteratorAllPath2(methods, path1,
                    method -> AnnotationUtils.findAnnotation(method, PreAuthorize.class),
                    (path, preAuthorize) -> {
                        if(accessUriSet.contains(path)){
                            return 0;
                        }
                        accessUriSet.put(path, ((PreAuthorize) preAuthorize).value());
                        return 0;
                    });

        });

    }
}
