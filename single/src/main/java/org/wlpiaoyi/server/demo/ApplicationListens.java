package org.wlpiaoyi.server.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.server.demo.config.web.support.AccessSupport;
import org.wlpiaoyi.server.demo.config.web.support.DecryptSupport;
import org.wlpiaoyi.server.demo.config.web.support.EncryptSupport;
import org.wlpiaoyi.server.demo.sys.service.IAccessService;
import org.wlpiaoyi.server.demo.utils.SpringUtils;
import org.wlpiaoyi.server.demo.utils.loader.AccessLoader;
import org.wlpiaoyi.server.demo.utils.loader.DecryptEncryptLoader;
import org.wlpiaoyi.server.demo.utils.loader.IdWorkerLoader;
import org.wlpiaoyi.server.demo.utils.loader.IdempotenceLoader;

import static org.wlpiaoyi.server.demo.config.web.support.IdempotenceSupport.IdempotenceUriSetObj;

@Slf4j
@Component
public class ApplicationListens implements CommandLineRunner, DisposableBean {



    //应用启动成功后的回调
    @Override
    public void run(String... args) throws Exception {
        log.info("应用启动成功，预相关加载数据");
        DecryptEncryptLoader.load(SpringUtils.getApplicationContext(), DecryptSupport.DECRYPT_URI_SET, EncryptSupport.ENCRYPT_URI_SET);
        IdWorkerLoader.load(SpringUtils.getApplicationContext());
        AccessLoader.load(SpringUtils.getApplicationContext(), AccessSupport.ACCESS_URI_SET);
//        IdWorkerLoader.load(SpringUtils.getApplicationContext(), System.currentTimeMillis());
        IdempotenceLoader.load(SpringUtils.getApplicationContext(), IdempotenceUriSetObj);
        IAccessService accessService = SpringUtils.getBean(IAccessService.class);
        accessService.mergeAll();

    }

    //应用启动关闭前的回调
    @Override
    public void destroy() throws Exception {
        log.info("应用正在关闭，清理相关数据");
    }
}
