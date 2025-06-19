package org.wlpiaoyi.server.demo.common.tools.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.wlpiaoyi.framework.utils.PatternUtils;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.server.demo.common.tools.web.domain.AuthRole;
import org.wlpiaoyi.server.demo.common.tools.web.domain.AuthUser;

import java.util.*;

/**
 * spring工具类 方便在非spring管理环境中获取bean
 *
 * @author ruoyi
 */
@Slf4j
public class SpringUtils {

    @Getter
    /** Spring Bean工厂 */
    protected static ConfigurableListableBeanFactory beanFactory;
    @Getter
    /** Spring应用上下文环境 */
    protected static ApplicationContext applicationContext;
    @Getter
    protected static AuthDomainContext authDomainContext;

    /**
     * 认证上下文环境
     * @param <U>
     * @param <R>
     */
    public interface AuthDomainContext<U extends AuthUser,R extends AuthRole>{

        U getSpringUtilsAuthUser();
        R getSpringUtilsAuthRole();

    }


    /**
     * 获取对象
     *
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     *
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException
    {
        return (T) beanFactory.getBean(name);
    }

    /**
     * 获取类型为requiredType的对象
     *
     * @param clz
     * @return
     * @throws BeansException
     *
     */
    public static <T> T getBean(Class<T> clz) throws BeansException
    {
        T result = (T) beanFactory.getBean(clz);
        return result;
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name)
    {
        return beanFactory.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name
     * @return boolean
     * @throws NoSuchBeanDefinitionException
     *
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException
    {
        return beanFactory.isSingleton(name);
    }

    /**
     * @param name
     * @return Class 注册对象的类型
     * @throws NoSuchBeanDefinitionException
     *
     */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException
    {
        return beanFactory.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     *
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException
    {
        return beanFactory.getAliases(name);
    }
    /**
     * <p><b>{@code @description:}</b>
     * 动态解析yml的值
     * </p>
     *
     * <p><b>{@code @param}</b> <b>key</b>
     * {@link String}
     * ${}格式
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/13 13:25</p>
     * <p><b>{@code @return:}</b>
     * {@link String}
     * 若是解析失败或者未查找到，均返回null
     * </p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public static String resolve(String key) {
        try {
            if(key.startsWith("{")){
                key = "$" + key;
            }else if(!key.startsWith("${")){
                key = "${" + key;
            }
            if(!key.endsWith("}")){
                key = key + "}";
            }
            String value = beanFactory.resolveEmbeddedValue(key);
            if(ValueUtils.isBlank(value)){
                return null;
            }
            if(value.equals(key)){
                return null;
            }
            return value;
        }catch (Exception e){
            log.warn("resolve error: {}", e.getMessage());
        }
        return null;
    }
    /**
     * <p><b>{@code @description:}</b>
     * 动态解析yml的值
     * </p>
     *
     * <p><b>@param</b> <b>key</b>
     * {@link String}
     * </p>
     *
     * <p><b>@param</b> <b>regex</b>
     * {@link String}
     * </p>
     *
     * <p><b>@param</b> <b>tClass</b>
     * {@link Class<T>}
     * </p>
     *
     * <p><b>@param</b> <b>vDefault</b>
     * {@link T}
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/1/31 15:26</p>
     * <p><b>{@code @return:}</b>{@link Set<T>}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public static <T> Set<T> resolveSet(String key, String regex, Class<T> tClass, T vDefault) {
        try {
            String value = resolve(key);
            if(ValueUtils.isBlank(value)){
                return null;
            }
            if(value.equals(key)){
                return null;
            }
            if(tClass.isAssignableFrom(List.class)){
                String[] vs = value.split(regex);
                Set<T> res = new HashSet<>(vs.length);
                for(String v : vs){
                    if(v.isEmpty()){
                        continue;
                    }
                    T re = parseResolveValue(value, tClass, vDefault);
                    if(re == null){
                        continue;
                    }
                    res.add(re);
                }
                return res;
            }
        }catch (Exception e){
            log.error("resolve error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * <p><b>{@code @description:}</b>
     * 动态解析yml的值
     * </p>
     *
     * <p><b>@param</b> <b>key</b>
     * {@link String}
     * ${}格式
     * </p>
     *
     * <p><b>@param</b> <b>regex</b>
     * {@link String}
     * </p>
     *
     * <p><b>@param</b> <b>tClass</b>
     * {@link Class<T>}
     * </p>
     *
     * <p><b>@param</b> <b>vDefault</b>
     * {@link T}
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/1/31 12:55</p>
     * <p><b>{@code @return:}</b>{@link List<T>}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public static <T> List<T> resolveList(String key, String regex, Class<T> tClass, T vDefault) {
        try {
            String value = resolve(key);
            if(ValueUtils.isBlank(value)){
                return null;
            }
            if(value.equals(key)){
                return null;
            }
            if(tClass.isAssignableFrom(List.class)){
                String[] vs = value.split(regex);
                List<T> res = new ArrayList<>(vs.length);
                for(String v : vs){
                    if(v.isEmpty()){
                        continue;
                    }
                    T re = parseResolveValue(value, tClass, vDefault);
                    if(re == null){
                        continue;
                    }
                    res.add(re);
                }
                return res;
            }
        }catch (Exception e){
            log.error("resolve error: {}", e.getMessage());
        }
        return null;
    }
    /**
     * <p><b>{@code @description:}</b>
     * 动态解析yml的值
     * </p>
     *
     * <p><b>@param</b> <b>key</b>
     * {@link String}
     * ${}格式
     * </p>
     *
     * <p><b>@param</b> <b>tClass</b>
     * {@link Class<T>}
     * </p>
     *
     * <p><b>@param</b> <b>vDefault</b>
     * {@link T}
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/1/31 12:57</p>
     * <p><b>{@code @return:}</b>{@link T}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public static <T> T resolve(String key, Class<T> tClass, T vDefault) {
        try {
            String value = resolve(key);
            if(ValueUtils.isBlank(value)){
                return vDefault;
            }
            if(value.equals(key)){
                return vDefault;
            }
            return parseResolveValue(value, tClass, vDefault);
        }catch (Exception e){
            log.warn("resolve error: {}", e.getMessage());
        }
        return vDefault;
    }
    /**
     * <p><b>{@code @description:}</b>
     * 动态解析yml的值
     * </p>
     *
     * <p><b>@param</b> <b>key</b>
     * {@link String}
     * ${}格式
     * </p>
     *
     * <p><b>@param</b> <b>vDefault</b>
     * {@link T}
     * </p>
     *
     * <p><b>{@code @date:}</b>2025/1/31 13:05</p>
     * <p><b>{@code @return:}</b>{@link T}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public static <T> T resolve(String key, T vDefault) {
        try {
            String value = resolve(key);
            if(ValueUtils.isBlank(value)){
                return vDefault;
            }
            if(value.equals(key)){
                return vDefault;
            }
            return parseResolveValue(value, (Class<T>) vDefault.getClass(), vDefault);
        }catch (Exception e){
            log.warn("resolve error: {}", e.getMessage());
        }
        return vDefault;
    }
    private static <T> T parseResolveValue(String value, Class<T> tClass, T vDefault) {
        try {
            if(tClass == String.class){
                return (T) value;
            }
            if(tClass == Boolean.class){
                if(PatternUtils.isNumber(value) || "true".equals(value.toUpperCase()) || "false".equals(value.toUpperCase())){
                    return (T) Boolean.valueOf(value);
                }
                return vDefault;
            }
            if(tClass == Byte.class){
                if(PatternUtils.isNumber(value)){
                    return (T) Byte.valueOf(value);
                }
                if(PatternUtils.isHexadecimal(value)){
                    return (T) Byte.valueOf((byte)((Character.digit(value.charAt(0), 16) << 4) + Character.digit(value.charAt(1), 16)));
                }
                return vDefault;
            }
            if(tClass == Integer.class){
                if(PatternUtils.isNumber(value)){
                    return (T) Integer.valueOf(value);
                }
                if(PatternUtils.isHexadecimal(value)){
                    return (T) Integer.valueOf((int) ValueUtils.toLong(ValueUtils.hexToBytes(value)));
                }
                return vDefault;
            }
            if(tClass == Short.class){
                if(PatternUtils.isNumber(value)){
                    return (T) Short.valueOf(value);
                }
                if(PatternUtils.isHexadecimal(value)){
                    return (T) Short.valueOf((short) ValueUtils.toLong(ValueUtils.hexToBytes(value)));
                }
                return vDefault;
            }
            if(tClass == Long.class){
                if(PatternUtils.isNumber(value)){
                    return (T) Long.valueOf(value);
                }
                if(PatternUtils.isHexadecimal(value)){
                    return (T) Long.valueOf(ValueUtils.toLong(ValueUtils.hexToBytes(value)));
                }
                return vDefault;
            }
            if(tClass == Float.class){
                if(PatternUtils.isNumber(value)){
                    return (T) Float.valueOf(value);
                }
                if(PatternUtils.isFloat(value)){
                    return (T) Float.valueOf(value);
                }
                return vDefault;
            }
            if(tClass == Double.class){
                if(PatternUtils.isNumber(value)){
                    return (T) Double.valueOf(value);
                }
                if(PatternUtils.isFloat(value)){
                    return (T) Double.valueOf(value);
                }
                return vDefault;
            }
        }catch (Exception e){
            log.warn("resolve error: {}", e.getMessage());
        }
        return vDefault;
    }

    /**
     * 获取aop代理对象
     *
     * @param invoker
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAopProxy(T invoker)
    {
        return (T) AopContext.currentProxy();
    }

    /**
     * 获取当前的环境配置，无配置返回null
     *
     * @return 当前的环境配置
     */
    public static String[] getActiveProfiles()
    {
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 获取当前的环境配置，当有多个环境配置时，只获取第一个
     *
     * @return 当前的环境配置
     */
    public static String getActiveProfile()
    {
        final String[] activeProfiles = getActiveProfiles();
        return ValueUtils.isNotBlank(activeProfiles) ? activeProfiles[0] : null;
    }

    /**
     * 获取配置文件中的值
     *
     * @param key 配置文件的key
     * @return 当前的配置文件的值
     *
     */
    public static String getRequiredProperty(String key){
        return applicationContext.getEnvironment().getRequiredProperty(key);
    }

    /**
     * <p><b>{@code @description:}</b>
     * 获取当前登录用户信息
     * </p>
     *
     * <p><b>{@code @param}</b> <b></b>
     * {@link }
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/14 19:54</p>
     * <p><b>{@code @return:}</b>{@link Object}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public static <U extends AuthUser> U getAuthUser(){
        AuthUser res = authDomainContext.getSpringUtilsAuthUser();
        if(res == null){
            return null;
        }
        return (U) res;
    }

    /**
     * <p><b>{@code @description:}</b>
     * 获取当前用户角色
     * </p>
     *
     * <p><b>{@code @param}</b> <b></b>
     * {@link }
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/14 19:57</p>
     * <p><b>{@code @return:}</b>{@link R}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public static <R extends AuthRole> R getAuthRole(){
        AuthRole res = authDomainContext.getSpringUtilsAuthRole();
        if(res == null){
            return null;
        }
        return (R) res;
    }

}
