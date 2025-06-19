package org.wlpiaoyi.server.demo.cloud.admin;

import cn.dev33.satoken.SaManager;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.wlpiaoyi.server.demo.common.tools.loader.ToolsLoader;
import org.wlpiaoyi.server.demo.common.tools.utils.SpringUtils;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/9/14 17:40
 * {@code @version:}:       1.0
 */
//
@ComponentScan(basePackages = {"org.wlpiaoyi.server.demo"})
@SpringBootApplication(scanBasePackages = {
        "org.wlpiaoyi.server.demo",
},exclude = {DataSourceAutoConfiguration.class })
public class Application implements ApplicationContextAware, BeanFactoryPostProcessor{

    @PostConstruct
    void started() {
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ApplicationListens.beanFactory = beanFactory;
        if(ApplicationListens.beanFactory != null && ApplicationListens.applicationContext != null){
            ToolsLoader.setBeanFactory(ApplicationListens.beanFactory);
            ToolsLoader.setApplicationContext(ApplicationListens.applicationContext);
            ToolsLoader.setAuthDomainContext(SpringUtils.getBean(SpringUtils.AuthDomainContext.class));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationListens.applicationContext = applicationContext;
        if(ApplicationListens.beanFactory != null && ApplicationListens.applicationContext != null){
            ToolsLoader.setBeanFactory(ApplicationListens.beanFactory);
            ToolsLoader.setApplicationContext(ApplicationListens.applicationContext);
            ToolsLoader.setAuthDomainContext(SpringUtils.getBean(SpringUtils.AuthDomainContext.class));
        }
    }
}
