package org.wlpiaoyi.server.demo.cloud.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.server.demo.common.core.utils.SpringUtils;
import org.wlpiaoyi.server.demo.common.redis.service.RedisOpsValueService;

@Slf4j
@Component
public class ApplicationListens implements CommandLineRunner, DisposableBean {



    //应用启动成功后的回调
    @Override
    public void run(String... args) throws Exception {
        RedisOpsValueService redisOpsValueService = SpringUtils.getBean(RedisOpsValueService.class);
        log.info("应用启动成功，预相关加载数据");
    }

    //应用启动关闭前的回调
    @Override
    public void destroy() throws Exception {
        log.info("应用正在关闭，清理相关数据");
    }
}
