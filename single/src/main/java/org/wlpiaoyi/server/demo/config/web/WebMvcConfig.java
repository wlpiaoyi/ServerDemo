package org.wlpiaoyi.server.demo.config.web;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.wlpiaoyi.framework.utils.encrypt.aes.Aes;
import org.wlpiaoyi.framework.utils.security.RsaCipher;
import org.wlpiaoyi.server.demo.config.web.interceptor.Interceptor;


@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

//    @SneakyThrows
//    @Bean("encrypt.aes")
//    public Aes initAes(@Value("${wlpiaoyi.ee.aes.key}") String key, @Value("${wlpiaoyi.ee.aes.iv}") String iv) {
//        return Aes.create().setKey(key).setIV(iv).load();
//    }
    @SneakyThrows
    @Bean("encrypt.rsae")
    public RsaCipher initRsaEncrypt(@Value("${wlpiaoyi.ee.rsa.privateKey}") String privateKey) {
        return RsaCipher.build(0).setPrivateKey(privateKey).loadConfig();
    }
    @SneakyThrows
    @Bean("encrypt.rsad")
    public RsaCipher initRsaDecrypt(@Value("${wlpiaoyi.ee.rsa.privateKey}") String privateKey) {
        return RsaCipher.build(1).setPrivateKey(privateKey).loadConfig();
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/4.18.2/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    /**
     * 重写addCorsMappings()解决跨域问题
     * 配置：允许http请求进行跨域访问
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

//    	 设置允许多个域名请求
//        String[] allowDomains = {"http://www.toheart.xin","http://192.168.11.213:8080","http://localhost:8080"};
//        指哪些接口URL需要增加跨域设置
        registry.addMapping("/**")
                //.allowedOrigins("*")//指的是前端哪些域名被允许跨域
                .allowedOriginPatterns("*")
                //需要带cookie等凭证时，设置为true，就会把cookie的相关信息带上
                .allowCredentials(true)
                //指的是允许哪些方法
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                //cookie的失效时间，单位为秒（s），若设置为-1，则关闭浏览器就失效
                .maxAge(3600);
    }




    /**
     * 重写addInterceptors()实现拦截器
     * 配置：要拦截的路径以及不拦截的路径
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册Interceptor拦截器(Interceptor这个类是我们自己写的拦截器类)
        InterceptorRegistration registration = registry.addInterceptor(new Interceptor());
        //addPathPatterns()方法添加需要拦截的路径
        //所有路径都被拦截
        registration.addPathPatterns("/**");
        //excludePathPatterns()方法添加不拦截的路径
        String[] excludePatterns = new String[]{
                "/error","/error.html",
                "/swagger-resources/**", "/webjars/**",
                "/v2/**", "/swagger-ui.html/**",
                "/api", "/api-docs",
                "/api-docs/**", "/doc.html/**",
                "/api/file/*"
        };
        //添加不拦截路径
        registration.excludePathPatterns(excludePatterns);
    }
}
