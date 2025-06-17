package org.wlpiaoyi.server.demo.common.tools.loader;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.snowflake.IdWorker;
import org.wlpiaoyi.server.demo.common.tools.utils.SpringUtils;
import org.wlpiaoyi.server.demo.common.tools.web.model.ConfigModel;
import org.wlpiaoyi.server.demo.common.tools.utils.IdUtils;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/2/18 18:47
 * {@code @version:}:       1.0
 */
public class ToolsLoader {

    private static ConfigurableListableBeanFactory beanFactory;
    private static ApplicationContext applicationContext;


    public static void setBeanFactory(ConfigurableListableBeanFactory beanFactory){
        SpringUtilsInit.setBeanFactory(beanFactory);
    }
    public static void setApplicationContext(ApplicationContext applicationContext){
        IdUtilsInit.setIdWork(applicationContext);
        SpringUtilsInit.setApplicationContext(applicationContext);
    }
    public static void setAuthDomainContext(SpringUtils.AuthDomainContext authDomainContext){
        SpringUtilsInit.setAuthDomainContext(authDomainContext);
    }

    private static class IdUtilsInit extends IdUtils{
         static void setIdWork(ApplicationContext applicationContext){
             ConfigModel configModel = applicationContext.getBean(ConfigModel.class);
             byte workerId = configModel.getWorkerId();
             byte datacenterId = configModel.getDatacenterId();
             if(workerId < 0 || datacenterId < 0){
                 throw new BusinessException("机器Id必须介于0~31之间");
             }
             IdUtils.idWorker = new IdWorker(workerId, datacenterId, configModel.getTimerEpoch());
        }
    }

    private static class SpringUtilsInit extends SpringUtils {
        static void setBeanFactory(ConfigurableListableBeanFactory beanFactory){
            SpringUtils.beanFactory = beanFactory;
        }
        static void setApplicationContext(ApplicationContext applicationContext){
            SpringUtils.applicationContext = applicationContext;
        }
        static void setAuthDomainContext(AuthDomainContext authDomainContext){
            SpringUtils.authDomainContext = authDomainContext;
        }
    }

}
