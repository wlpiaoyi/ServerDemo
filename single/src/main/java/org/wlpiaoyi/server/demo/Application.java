package org.wlpiaoyi.server.demo;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.NotNull;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.wlpiaoyi.server.demo.common.datasource.domain.entity.BaseEntity;

import java.util.TimeZone;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/9/14 17:40
 * {@code @version:}:       1.0
 */
//Dspring.config.location=/data/config/application-sms.yml
@SpringBootApplication(scanBasePackages = {
        "org.wlpiaoyi.server.demo",
})
@ComponentScan(basePackages = {"org.wlpiaoyi.server.demo"})
@MapperScan("org.wlpiaoyi.server.demo")
@EnableKnife4j
@OpenAPIDefinition(info = @Info(
        title = "Demo系统",
        description = "Demo系统样例",
        termsOfService = "默认没有API服务条款",
        contact = @Contact(
                name = "wlpiaoyi",
                email = "wlpiaoyi@gmail.com"
        ),
        version = "1.0.0"
))
public class Application implements ApplicationContextAware, BeanFactoryPostProcessor {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone(BaseEntity.ZONE));
    }

    public static void main(String[] args) {
//        org.yaml.snakeyaml.inspector.TagInspector
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ApplicationInitializer.SpringUtilsBuilder.build().setBeanFactory(beanFactory);
    }
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        ApplicationInitializer.SpringUtilsBuilder.build().setApplicationContext(applicationContext);
    }

}
