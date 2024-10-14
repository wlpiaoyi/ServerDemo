package org.wlpiaoyi.server.demo.sys.controller;

import lombok.SneakyThrows;
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
 * <p><b>{@code @date:}</b> 2024-10-13 16:32:03</p>
 * <p><b>{@code @version:}:</b> 1.0</p>
 */
public class UserControllerTest {

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

    private String token = "wl12";
    private String salt = "";

    @SneakyThrows
    @Test
    public void test() throws IOException {
        this.login();
        this.expire();
    }

    @SneakyThrows
    @Test
    public void login() throws IOException {
        HttpClientContext context = HttpClientContext.create();
        Request<byte[]> request = new Request<>(context, "http://127.0.0.1:8180/sys/user/login", Request.Method.Post);
        request.setHeader(HttpHeaders.CONTENT_TYPE, WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + "application/json;charset=utf-8");
        request.setHeader(HttpHeaders.ACCEPT, WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + "application/json");
        request.setHeader("token", this.token);
        Map body = new HashMap(){{
            put("account", "admin");
            put("password", "jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=");
        }};
        byte[] buffers = this.aes.encrypt(GsonBuilder.gsonDefault().toJson(body, Map.class).getBytes(StandardCharsets.UTF_8));
        System.out.println("request:" + request.getUrl() + " body:[\n" + ValueUtils.bytesToHex(buffers) + "\n]");
        request.setBody(buffers).setHttpProxy("127.0.0.1", 8888);
        Response<byte[]> response = request.execute(byte[].class);
        buffers = response.getBody();
        String salt = response.getHeaders().get(WebUtils.HEADER_SALT_KEY);
        System.out.println(" ebody:[\n" + ValueUtils.bytesToHex(buffers) + "\n]");
        System.out.println(" dbody:[\n" + new String(this.aes.decrypt(buffers)) + "\n]");
        if(ValueUtils.isNotBlank(salt))
            System.out.println(" esalt:" + salt);
        if(ValueUtils.isNotBlank(salt)){
            this.salt = new String(this.rsaDecrypt.decrypt(DataUtils.base64Decode(salt.getBytes())));
            System.out.println(" dsalt:[\n" + this.salt + "\n]");
        }
        System.out.println("====================================>");
    }

    @SneakyThrows
    @Test
    public void expire() throws IOException {
        HttpClientContext context = HttpClientContext.create();
        Request<byte[]> request;
        Response<byte[]> response;
        byte[] buffers;
        String salt;

        request = new Request<>(context, "http://127.0.0.1:8180/sys/user/expire", Request.Method.Get);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.setHeader("token", this.token);
        request.setHttpProxy("127.0.0.1", 8888);
        System.out.println("request:" + request.getUrl());
        response = request.execute(byte[].class);
        buffers = response.getBody();
        salt = response.getHeaders().get(WebUtils.HEADER_SALT_KEY);
        System.out.println(" ebody:[\n" + ValueUtils.bytesToHex(buffers) + "\n]");
        System.out.println(" dbody:[\n" + new String(buffers) + "\n]");
        if(ValueUtils.isNotBlank(salt))
            System.out.println(" esalt:" + salt);
        if(ValueUtils.isNotBlank(salt)){
            this.salt = new String(this.rsaDecrypt.decrypt(DataUtils.base64Decode(salt.getBytes())));
            System.out.println(" dsalt:[\n" + this.salt + "\n]");
        }

    }

    @After
    public void tearDown() throws Exception {}
}
