package org.wlpiaoyi.server.demo.cloud.gateway;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.TimeZone;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/9/14 17:40
 * {@code @version:}:       1.0
 */
//Dspring.config.location=/data/config/application-sms.yml
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class Application implements ApplicationContextAware, BeanFactoryPostProcessor {

    @PostConstruct
    void started() {
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ApplicationInitializer.SpringUtilsBuilder.build().setBeanFactory(beanFactory);
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationInitializer.SpringUtilsBuilder.build().setApplicationContext(applicationContext);
    }

}
