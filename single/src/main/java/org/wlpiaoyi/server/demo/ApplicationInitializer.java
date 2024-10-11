package org.wlpiaoyi.server.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.wlpiaoyi.server.demo.utils.SpringUtils;


@Slf4j
class ApplicationInitializer {

    static class SpringUtilsBuilder extends SpringUtils {
        private SpringUtilsBuilder(){}
        static SpringUtilsBuilder build(){
            return new SpringUtilsBuilder();
        }
        SpringUtilsBuilder setBeanFactory(ConfigurableListableBeanFactory beanFactory){
            SpringUtils.beanFactory = beanFactory;
            return this;
        }
        SpringUtilsBuilder setApplicationContext(ApplicationContext applicationContext){
            SpringUtils.applicationContext = applicationContext;
            return this;
        }
    }

}
