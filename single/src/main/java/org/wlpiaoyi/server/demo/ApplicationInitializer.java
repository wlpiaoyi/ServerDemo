package org.wlpiaoyi.server.demo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.wlpiaoyi.server.demo.cache.UserCachesService;
import org.wlpiaoyi.server.demo.utils.SpringUtils;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;


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
//            UserCachesService userCachesService = applicationContext.getBean(UserCachesService.class);
            SpringUtils.utilsExpand =  new SpringUtilsExpand() {
                private UserCachesService userCachesService;
                UserCachesService getUserCachesService(){
                    if(this.userCachesService == null){
                        this.userCachesService = SpringUtils.getBean(UserCachesService.class);
                    }
                    return this.userCachesService;
                }
                @Override
                public Object getSpringUtilsAuthUser() {
                    HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
                    String token = request.getHeader(WebUtils.HEADER_TOKEN_KEY);
                    return this.getUserCachesService().getSpringUtilsAuthUser(token);
                }

                @Override
                public Object getSpringUtilsAuthRole() {
                    HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
                    String token = request.getHeader(WebUtils.HEADER_TOKEN_KEY);
                    return this.getUserCachesService().getSpringUtilsAuthRole(token);
                }
            };
            return this;
        }
    }

}
