package org.wlpiaoyi.server.demo.cloud.gateway;

import jakarta.annotation.PostConstruct;
//import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.wlpiaoyi.server.demo.common.tools.loader.ToolsLoader;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/9/14 17:40
 * {@code @version:}:       1.0
 */
//Dspring.config.location=/data/config/application-sms.yml
@SpringBootApplication(
        exclude = {DataSourceAutoConfiguration.class }
        , scanBasePackages = {"org.wlpiaoyi.server.demo"}
)
public class GatewayApplication implements ApplicationContextAware, BeanFactoryPostProcessor {

    @PostConstruct
    void started() {
        
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ApplicationListens.beanFactory = beanFactory;
        if(ApplicationListens.beanFactory != null && ApplicationListens.applicationContext != null){
            ToolsLoader.setBeanFactory(ApplicationListens.beanFactory);
            ToolsLoader.setApplicationContext(ApplicationListens.applicationContext);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationListens.applicationContext = applicationContext;
        if(ApplicationListens.beanFactory != null && ApplicationListens.applicationContext != null){
            ToolsLoader.setBeanFactory(ApplicationListens.beanFactory);
            ToolsLoader.setApplicationContext(ApplicationListens.applicationContext);
        }
    }

}
