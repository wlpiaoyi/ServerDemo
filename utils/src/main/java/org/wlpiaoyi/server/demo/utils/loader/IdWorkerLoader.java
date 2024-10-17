package org.wlpiaoyi.server.demo.utils.loader;

import org.springframework.context.ApplicationContext;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.snowflake.IdWorker;
import org.wlpiaoyi.server.demo.utils.IdUtils;
import org.wlpiaoyi.server.demo.utils.web.ConfigModel;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/2/18 18:47
 * {@code @version:}:       1.0
 */
public class IdWorkerLoader {

    private class IdUtilsInit extends IdUtils{

         static void setIdWork(IdWorker idWorker){
            IdUtils.idWorker = idWorker;
        }

    }

    public static void load(ApplicationContext applicationContext){
        ConfigModel configModel = applicationContext.getBean(ConfigModel.class);
        byte workerId = configModel.getWorkerId();
        byte datacenterId = configModel.getDatacenterId();
        if(workerId < 0 || datacenterId < 0){
            throw new BusinessException("机器Id必须介于0~31之间");
        }
        IdUtilsInit.setIdWork(new IdWorker(workerId, datacenterId, configModel.getTimerEpoch()));
    }
}
