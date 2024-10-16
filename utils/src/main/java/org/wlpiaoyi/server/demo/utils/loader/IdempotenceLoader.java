package org.wlpiaoyi.server.demo.utils.loader;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.wlpiaoyi.server.demo.utils.web.annotation.Idempotence;
import org.wlpiaoyi.server.demo.utils.web.support.impl.idempotence.IdempotenceUriSet;

import java.lang.reflect.Method;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/2/18 18:41
 * {@code @version:}:       1.0
 */
public class IdempotenceLoader {

    /**
     * 加载数据
     * 找出需要幂等的URI
     * @param applicationContext
     * @throws BeansException
     */
    public static void load(ApplicationContext applicationContext, IdempotenceUriSet idempotenceUriSet) throws BeansException {
        AnnotationPathUtils.iteratorAllPath1(applicationContext, (value, path1) -> {
            path1 = AnnotationPathUtils.checkMappingValue(path1);
            Method[] methods = value.getClass().getMethods();
            AnnotationPathUtils.iteratorAllPath2(methods, path1,
                    method -> AnnotationUtils.findAnnotation(method, Idempotence.class),
                    (path, idempotence) -> {
                        if(idempotenceUriSet.contains(path)){
                            return 0;
                        }
                        idempotenceUriSet.put(path, ((Idempotence)idempotence));
                        return 0;
                    });

        });
    }
}
