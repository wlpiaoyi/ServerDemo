package org.wlpiaoyi.server.demo.test;

import lombok.SneakyThrows;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpHeaders;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.data.DataUtils;
import org.wlpiaoyi.framework.utils.encrypt.aes.Aes;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.gson.GsonBuilder;
import org.wlpiaoyi.framework.utils.http.factory.CookieFactory;
import org.wlpiaoyi.framework.utils.http.request.Request;
import org.wlpiaoyi.framework.utils.http.response.Response;
import org.wlpiaoyi.framework.utils.security.RsaCipher;
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

    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/1mdKj9OPw5K8M12MmVGC7KJpu9zguyfU4g42\n" +
            "iavdJkgY7uL61F4gvGmRhydIFN9p007AuI0wViP6qR9q9zYenuL2G5ce3bsB4iyxBosoPw1gbPRR\n" +
            "EqZWCUD8rluvqoHZNjdwbHdx5SMLwwtF6xDG6dEq4Vgriwt3pGPEF7csCQIDAQAB";

    private RsaCipher rsaEncrypt = RsaCipher.build(1).setPublicKey(publicKey).loadConfig();
    private RsaCipher rsaDecrypt = RsaCipher.build(0).setPublicKey(publicKey).loadConfig();

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
            put("error", "中文测试akladklak阿拉丁垃圾啊了当时就哭了爱神的箭拉萨大家flak就");
        }};
        byte[] buffers = this.aes.encrypt(GsonBuilder.gsonDefault().toJson(body, Map.class).getBytes(StandardCharsets.UTF_8));
        System.out.println("request:" + request.getUrl() + " body:" + ValueUtils.bytesToHex(buffers));
        request.setBody(buffers).setHttpProxy("127.0.0.1", 8888);
        Response<byte[]> response = request.execute(byte[].class);
        buffers = response.getBody();
        System.out.println(" ebody:" + ValueUtils.bytesToHex(buffers));
        System.out.println(" dbody:" + new String(this.aes.decrypt(buffers)));
        System.out.println("====================================>");

        request = new Request<>(context, "http://127.0.0.1:8180/test/auth/login", Request.Method.Get);
        request.setHeader(HttpHeaders.CONTENT_TYPE, WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + "application/json;charset=utf-8");
        request.setHeader(HttpHeaders.ACCEPT, WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + "application/json");
        request.setHeader("token", "wl12");
        body = new HashMap(){{
            put("current", "0");
            put("size", "3");
        }};
        buffers = this.aes.encrypt(GsonBuilder.gsonDefault().toJson(body, Map.class).getBytes(StandardCharsets.UTF_8));
        System.out.println("request:" + request.getUrl() + " body:" + ValueUtils.bytesToHex(buffers));
        request.setBody(buffers).setHttpProxy("127.0.0.1", 8888);
        response = request.execute(byte[].class);
        buffers = response.getBody();
        System.out.println(" ebody:" + ValueUtils.bytesToHex(buffers));
        System.out.println(" dbody:" + new String(this.aes.decrypt(buffers)));


        request = new Request<>(context, "http://127.0.0.1:8180/test/auth/login", Request.Method.Post);
        request.setHeader(HttpHeaders.CONTENT_TYPE, WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + "application/json;charset=utf-8");
        request.setHeader(HttpHeaders.ACCEPT, WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + "application/json");
        request.setHeader("token", "wl12");
        body = new HashMap(){{
            put("current", "0");
            put("size", "3");
        }};
        buffers = this.aes.encrypt(GsonBuilder.gsonDefault().toJson(body, Map.class).getBytes(StandardCharsets.UTF_8));
        System.out.println("request:" + request.getUrl() + " body:" +ValueUtils.bytesToHex(buffers));
        request.setBody(buffers).setHttpProxy("127.0.0.1", 8888);
        response = request.execute(byte[].class);
        buffers = response.getBody();
        System.out.println(" ebody:" + ValueUtils.bytesToHex(buffers));
        System.out.println(" dbody:" + new String(this.aes.decrypt(buffers)));
        System.out.println("====================================>");
        String salt = new String(this.rsaDecrypt.decrypt(DataUtils.base64Decode(response.getHeaders().get(WebUtils.HEADER_SALT_KEY).getBytes())));

        request = new Request<>(context, "http://127.0.0.1:8180/test/common/list", Request.Method.Post);
        request.setHeader(HttpHeaders.CONTENT_TYPE, WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + "application/json;charset=utf-8");
        request.setHeader(HttpHeaders.ACCEPT, WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + "application/json");
        request.setHeader(WebUtils.HEADER_SALT_KEY, new String(DataUtils.base64Encode(this.rsaEncrypt.encrypt(salt.getBytes()))));
        request.setHeader("token", "wl12");
        body = new HashMap(){{
        }};
        buffers = this.aes.encrypt(GsonBuilder.gsonDefault().toJson(body, Map.class).getBytes(StandardCharsets.UTF_8));
        System.out.println("request:" + request.getUrl() + " body:" +ValueUtils.bytesToHex(buffers));
        request.setBody(buffers).setHttpProxy("127.0.0.1", 8888);
        response = request.execute(byte[].class);
        buffers = response.getBody();
        System.out.println(" ebody:" + ValueUtils.bytesToHex(buffers));
        System.out.println(" dbody:" + new String(this.aes.decrypt(buffers)));
        System.out.println("====================================>");


        request = new Request<>(context, "http://127.0.0.1:8180/test/common/list", Request.Method.Post);
        request.setHeader(HttpHeaders.CONTENT_TYPE, WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + "application/json;charset=utf-8");
        request.setHeader(HttpHeaders.ACCEPT, WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + "application/json");
        request.setHeader(WebUtils.HEADER_SALT_KEY, new String(DataUtils.base64Encode(this.rsaEncrypt.encrypt(salt.getBytes()))));
        request.setHeader("token", "wl12");
        body = new HashMap(){{
            put("v1", "0");
            put("v4", "3");
        }};
        buffers = this.aes.encrypt(GsonBuilder.gsonDefault().toJson(body, Map.class).getBytes(StandardCharsets.UTF_8));
        System.out.println("request:" + request.getUrl() + " body:" +ValueUtils.bytesToHex(buffers));
        request.setBody(buffers).setHttpProxy("127.0.0.1", 8888);
        response = request.execute(byte[].class);
        buffers = response.getBody();
        System.out.println(" ebody:" + ValueUtils.bytesToHex(buffers));
        System.out.println(" dbody:" + new String(this.aes.decrypt(buffers)));
        System.out.println("====================================>");



        request = new Request<>(context, "http://127.0.0.1:8180/test/censor/list", Request.Method.Post);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        request.setHeader(HttpHeaders.ACCEPT,"application/json");
        request.setHeader(WebUtils.HEADER_SALT_KEY, new String(DataUtils.base64Encode(this.rsaEncrypt.encrypt(salt.getBytes()))));
        request.setHeader("token", "wl12");
        request.setParam("n1", "v1");
        body = new HashMap(){{
            put("v1", "0");
            put("v4", "3");
        }};
        buffers = GsonBuilder.gsonDefault().toJson(body, Map.class).getBytes(StandardCharsets.UTF_8);
//        buffers = this.aes.encrypt(buffers);
        System.out.println("request:" + request.getUrl() + " body:" + new String(buffers));
        request.setBody(buffers).setHttpProxy("127.0.0.1", 8888);
        response = request.execute(byte[].class);
        buffers = response.getBody();
        System.out.println(" ebody:" + ValueUtils.bytesToHex(buffers));
        System.out.println(" dbody:" + new String(this.aes.decrypt(buffers)));
        System.out.println("====================================>");


        request = new Request<>(context, "http://127.0.0.1:8180/test/common/do", Request.Method.Post);
        request.setHeader(HttpHeaders.CONTENT_TYPE, WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + "application/json;charset=utf-8");
        request.setHeader(HttpHeaders.ACCEPT, WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + "application/json");
        request.setHeader(WebUtils.HEADER_SALT_KEY, new String(DataUtils.base64Encode(this.rsaEncrypt.encrypt(salt.getBytes()))));
        request.setHeader("token", "wl12");
        body = new HashMap(){{
            put("v1", "0");
            put("v4", "3");
        }};
        buffers = this.aes.encrypt(GsonBuilder.gsonDefault().toJson(body, Map.class).getBytes(StandardCharsets.UTF_8));
        System.out.println("request:" + request.getUrl() + " body:" +ValueUtils.bytesToHex(buffers));
        request.setBody(buffers).setHttpProxy("127.0.0.1", 8888);
        response = request.execute(byte[].class);
        buffers = response.getBody();
        System.out.println(" ebody:" + new String(buffers));
//        System.out.println(" dbody:" + new String(this.aes.decrypt(buffers)));
        System.out.println("====================================>");
    }

    @After
    public void tearDown() throws Exception {}
}
