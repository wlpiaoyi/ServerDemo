package org.wlpiaoyi.server.demo.utils.loader;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.wlpiaoyi.server.demo.utils.web.annotation.Decrypt;
import org.wlpiaoyi.server.demo.utils.web.annotation.Encrypt;
import org.wlpiaoyi.server.demo.utils.web.annotation.Idempotence;
import org.wlpiaoyi.server.demo.utils.web.support.impl.encrypt.DecryptUriSet;
import org.wlpiaoyi.server.demo.utils.web.support.impl.encrypt.EncryptUriSet;
import org.wlpiaoyi.server.demo.utils.web.support.impl.idempotence.IdempotenceUriSet;

import java.lang.reflect.Method;

/**
 * <p><b>{@code @author:}</b> wlpia</p>
 * <p><b>{@code @description:}</b> </p>
 * <p><b>{@code @date:}</b> 2024-10-13 09:23:27</p>
 * <p><b>{@code @version:}:</b> 1.0</p>
 */
public class DecryptEncryptLoader {


    public static void load(ApplicationContext applicationContext, DecryptUriSet decryptUriSet, EncryptUriSet encryptUriSet) throws BeansException {
        AnnotationPathUtils.iteratorAllPath1(applicationContext, (value, path1) -> {
            path1 = AnnotationPathUtils.checkMappingValue(path1);
            Method[] methods = value.getClass().getMethods();
            if(decryptUriSet != null){
                AnnotationPathUtils.iteratorAllPath2(methods, path1,
                        method -> AnnotationUtils.findAnnotation(method, Decrypt.class),
                        (path, decrypt) -> {
                            if(decryptUriSet.contains(path)){
                                return 0;
                            }
                            decryptUriSet.add(path);
                            return 0;
                        });
            }
            if(encryptUriSet != null){
                AnnotationPathUtils.iteratorAllPath2(methods, path1,
                        method -> AnnotationUtils.findAnnotation(method, Encrypt.class),
                        (path, decrypt) -> {
                            if(encryptUriSet.contains(path)){
                                return 0;
                            }
                            encryptUriSet.add(path);
                            return 0;
                        });
            }

        });
    }

}
