package org.wlpiaoyi.server.demo.utils.loader;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.server.demo.utils.web.annotation.Idempotence;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/2/16 21:09
 * {@code @version:}:       1.0
 */
class AnnotationPathUtils {


    public interface IteratorExecPath1{
        void run(Object value, String path1);
    }
    public interface IteratorExecPath2{
        int run(String path, Annotation annotation);
    }

    public interface IteratorSkipPath2{
        Annotation run(Method method);
    }

    public static void iteratorAllPath1(ApplicationContext applicationContext, IteratorExecPath1 exec){

        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(RestController.class);
        for (Map.Entry<String, Object> entry : controllers.entrySet()) {
            Object value = entry.getValue();
            RequestMapping rm = AnnotationUtils.findAnnotation(value.getClass(), RequestMapping.class);
            String[] paths1 = AnnotationPathUtils.requestMappingValue(rm);
            for (String path1 : paths1) {
                exec.run(value, path1);
            }
        }
    }

    public static void iteratorAllPath2(Method[] methods, String path1, IteratorSkipPath2 skip, IteratorExecPath2 exec){

        for (Method method : methods) {
            AnnotationPathUtils.iteratorAllPath2(method, path1, skip, exec);
        }
    }

    public static void iteratorAllPath2(Method method, String path1, IteratorSkipPath2 skip, IteratorExecPath2 exec){
        Annotation idempotence = null;
        if(skip != null && (idempotence = skip.run(method)) == null){
            return;
        }
        String[] paths2 = AnnotationPathUtils.requestMappingValue(
                AnnotationUtils.findAnnotation(method, GetMapping.class),
                AnnotationUtils.findAnnotation(method, PostMapping.class),
                AnnotationUtils.findAnnotation(method, PutMapping.class),
                AnnotationUtils.findAnnotation(method, DeleteMapping.class)
        );
        if(ValueUtils.isBlank(paths2)){
            return;
        }

        for (String path2 : paths2) {
            path2 = AnnotationPathUtils.checkMappingValue(path2);
            final String path = path1 + path2;
            if(exec.run(path, idempotence) == -1){
                break;
            }
        }
    }

    public static String[] requestMappingValue(RequestMapping rm){
        String[] values = null;
        if(rm != null){
            values = rm.value();
        }
        if(ValueUtils.isBlank(values)){
            values = new String[1];
            values[0] = "";
        }
        return values;
    }

    public static String[] requestMappingValue(GetMapping gm, PostMapping pm, PutMapping um, DeleteMapping dm){

        String[] valuesGm = null;
        String[] valuesPm = null;
        String[] valuesUm = null;
        String[] valuesDm = null;
        if(gm != null){
            valuesGm = gm.value();
        }
        if(pm != null){
            valuesPm = pm.value();
        }
        if(um != null){
            valuesUm = um.value();
        }
        if(dm != null){
            valuesDm = dm.value();
        }
        int vSize = 0;
        if(valuesGm != null){
            vSize += valuesGm.length;
        }
        if(valuesPm != null){
            vSize += valuesPm.length;
        }
        if(valuesUm != null){
            vSize += valuesUm.length;
        }
        if(valuesDm != null){
            vSize += valuesDm.length;
        }
        if(vSize <= 0){
            return null;
        }
        String[] values = new String[vSize];
        int index = 0;
        if(valuesGm != null){
            for (String value : valuesGm) {
                values[index] = value;
                index ++;
            }
        }
        if(valuesPm != null){
            for (String value : valuesPm) {
                values[index] = value;
                index ++;
            }
        }
        if(valuesUm != null){
            for (String value : valuesUm) {
                values[index] = value;
                index ++;
            }
        }
        if(valuesDm != null){
            for (String value : valuesDm) {
                values[index] = value;
                index ++;
            }
        }
        return values;
    }

    private static final String URI_SOLIDUS_FLAG = "/";

    public static String checkMappingValue(String value){
        if(!ValueUtils.isBlank(value) && !value.startsWith(URI_SOLIDUS_FLAG)){
            value = URI_SOLIDUS_FLAG + value;
        }
        return value;
    }

}
