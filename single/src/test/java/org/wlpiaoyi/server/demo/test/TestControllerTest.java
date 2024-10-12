package org.wlpiaoyi.server.demo.test;

import lombok.SneakyThrows;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpHeaders;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wlpiaoyi.framework.utils.encrypt.aes.Aes;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.gson.GsonBuilder;
import org.wlpiaoyi.framework.utils.http.factory.CookieFactory;
import org.wlpiaoyi.framework.utils.http.request.Request;
import org.wlpiaoyi.framework.utils.http.response.Response;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * <p><b>{@code @author:}</b> wlpia</p>
 * <p><b>{@code @description:}</b> </p>
 * <p><b>{@code @date:}</b> 2024-10-12 10:15:30</p>
 * <p><b>{@code @version:}:</b> 1.0</p>
 */
public class TestControllerTest {


    @Before
    public void setUp() throws Exception {}

    private Aes aes;
    {
        try {
            aes = Aes.create().setKey("abcd567890ABCDEF1234567890ABCDEF").setIV("abcd567890123456").load();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
    @SneakyThrows
    @Test
    public void login() throws IOException {
        HttpClientContext context = HttpClientContext.create();
        Request<byte[]> request = new Request<>(context, "http://127.0.0.1:8180/test/auth/login", Request.Method.Post);
        request.setHeader(HttpHeaders.CONTENT_TYPE, WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + "application/json;charset=utf-8");
        request.setHeader(HttpHeaders.ACCEPT, WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + "application/json");
        request.setHeader("token", "wl12");
        Map body = new HashMap(){{
            put("current", "0");
            put("size", "3");
        }};
        byte[] buffers = this.aes.encrypt(GsonBuilder.gsonDefault().toJson(body, Map.class).getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(buffers));
        request.setBody(buffers).setHttpProxy("127.0.0.1", 8888);
        Response<byte[]> response = request.execute(byte[].class);
        buffers = response.getBody();
        System.out.println(new String(buffers));
        System.out.println(new String(this.aes.decrypt(buffers)));
    }

    @After
    public void tearDown() throws Exception {}
}
