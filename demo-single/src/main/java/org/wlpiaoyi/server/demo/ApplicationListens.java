package org.wlpiaoyi.server.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.server.demo.common.tools.utils.SpringUtils;
import org.wlpiaoyi.server.demo.common.redis.service.RedisService;
import org.wlpiaoyi.server.demo.config.web.support.AccessSupport;
import org.wlpiaoyi.server.demo.config.web.support.DecryptSupport;
import org.wlpiaoyi.server.demo.config.web.support.EncryptSupport;
import org.wlpiaoyi.server.demo.common.core.loader.AccessLoader;
import org.wlpiaoyi.server.demo.common.core.loader.DecryptEncryptLoader;
import org.wlpiaoyi.server.demo.common.core.loader.IdempotenceLoader;

import java.util.ArrayList;

import static org.wlpiaoyi.server.demo.config.web.support.IdempotenceSupport.IdempotenceUriSetObj;

@Slf4j
@Component
public class ApplicationListens implements CommandLineRunner, DisposableBean {


    protected static ConfigurableListableBeanFactory beanFactory;
    protected static ApplicationContext applicationContext;

    //应用启动成功后的回调
    @Override
    public void run(String... args) throws Exception {
        log.info("应用启动成功，预相关加载数据");
        RedisService redisService = SpringUtils.getBean(RedisService.class);
//        User user = new User();
//        user.setId(6L);
//        user.setAccount("111");
//        user.setCreateTime(new Date());
//        redisService.setCacheObject("test", user);
//        User usere = redisService.getCacheObject("test");
//        redisService.setCacheList("test", new ArrayList(){{add(1); add(2);}});
//        Object res = redisService.getCacheList("test");
        DecryptEncryptLoader.load(SpringUtils.getApplicationContext(), DecryptSupport.DECRYPT_URI_SET, EncryptSupport.ENCRYPT_URI_SET);
        AccessLoader.load(SpringUtils.getApplicationContext(), AccessSupport.ACCESS_URI_SET);
        IdempotenceLoader.load(SpringUtils.getApplicationContext(), IdempotenceUriSetObj);
//        IAccessService accessService = SpringUtils.getBean(IAccessService.class);
//        accessService.mergeAll(AccessSupport.ACCESS_URI_SET);

    }

    //应用启动关闭前的回调
    @Override
    public void destroy() throws Exception {
        log.info("应用正在关闭，清理相关数据");
    }
}
