package org.wlpiaoyi.server.demo.sys.controller;

import lombok.SneakyThrows;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpHeaders;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wlpiaoyi.framework.utils.StringUtils;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.data.DataUtils;
import org.wlpiaoyi.framework.utils.encrypt.aes.Aes;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.gson.GsonBuilder;
import org.wlpiaoyi.framework.utils.http.request.Request;
import org.wlpiaoyi.framework.utils.http.response.Response;
import org.wlpiaoyi.framework.utils.security.RsaCipher;
import org.wlpiaoyi.framework.utils.security.SignVerify;
import org.wlpiaoyi.server.demo.common.tools.utils.WebUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    private String signPublicKey = "MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZp\n" +
            "RV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fn\n" +
            "xqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuE\n" +
            "C/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJ\n" +
            "FnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImo\n" +
            "g9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAW2KjsxHHfm1igOPrP//mfptfS5/2qdRw+okO\n" +
            "u5txFnQtTsXSEV1A7+/rj9E/nxCgoyQzpVSaJajhpz/DU20qtFtVkf8HzBmhFniO5SEJRwlziFrf\n" +
            "Yxvy7H2C17T7ccGulfzN2jns1ZWfn9B4cYj4LidpeSuucNJinyIWGr01GU4=";



    private RsaCipher rsaEncrypt = RsaCipher.build(1).setPublicKey(publicKey).loadConfig();
    private RsaCipher rsaDecrypt = RsaCipher.build(0).setPublicKey(publicKey).loadConfig();

    private SignVerify verify = SignVerify.build().setPublicKey(this.signPublicKey).loadConfig();

    @Before
    public void setUp() throws Exception {}

    @SneakyThrows
    public void checkRequestBody(Request<byte[]> request, boolean eFlag){
        String contentType = request.getHeaders().get(HttpHeaders.CONTENT_TYPE);
        if(eFlag){
            if(request.getBody() != null){
                System.out.println("request dbody:[\n" + new String(request.getBody()) + "\n]");
            }
            contentType = WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG + contentType;
            String key = StringUtils.getUUID32();
            String iv = StringUtils.getUUID32().substring(0, 16);
            String dSalt = key + "," + iv;
            String eSalt = new String(DataUtils.base64Encode(this.rsaEncrypt.encrypt(dSalt.getBytes())));
            request.getHeaders().put(WebUtils.HEADER_SALT_KEY, eSalt);
            if(request.getBody() != null){
                Aes aes = Aes.create().setKey(key).setIV(iv).load();
                request.setBody(aes.encrypt(request.getBody()));
                System.out.println("request ebody:[\n" + ValueUtils.bytesToHex(request.getBody()) + "\n]");
            }
        }else{
            if(request.getBody() != null){
                System.out.println("request body:[\n" + new String(request.getBody()) + "\n]");
            }
        }
        request.getHeaders().put(HttpHeaders.CONTENT_TYPE, contentType);
    }

    @SneakyThrows
    public void checkResponseBody(Response<byte[]> response){
        byte[] body = response.getBody();
        String contentType = response.getHeaders().get(HttpHeaders.CONTENT_TYPE);
        if(ValueUtils.isNotBlank(contentType) && contentType.startsWith(WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG)){
            System.out.println("response ebody:[\n" + ValueUtils.bytesToHex(body) + "\n]");
            String token = response.getHeaders().get(WebUtils.HEADER_TOKEN_KEY);
            if(ValueUtils.isNotBlank(token)){
                System.out.println("response token:" + token);
                this.token = token;
            }
            String eSalt = response.getHeaders().get(WebUtils.HEADER_SALT_KEY);
            String args[] =  new String(this.rsaDecrypt.decrypt(DataUtils.base64Decode(eSalt.getBytes()))).split(",");
            Aes aes = Aes.create().setKey(args[0]).setIV(args[1]).load();
            byte[] dBody = aes.decrypt(body);
            String sign = response.getHeaders().get(WebUtils.HEADER_SIGN_KEY);
            if(ValueUtils.isNotBlank(sign)){
                System.out.println("response sign:" + sign);
                if(this.verify.verify(dBody, DataUtils.base64Decode(sign.getBytes()))){
                    System.out.println("response sign verify: true");
                }else throw new BusinessException("sign verify failed");
            }
            System.out.println("response dbody:[\n" + new String(aes.decrypt(body)) + "\n]");
        }else{
            System.out.println("response body:[\n" + new String(body) + "\n]");
        }
    }

    private String token = StringUtils.getUUID32();

    @SneakyThrows
    @Test
    public void test() throws IOException {
        this.login404();
        this.logine();
        this.loginer();
        this.save();
        this.login();
        this.save();
        this.expire();
        this.delete();
    }


    @SneakyThrows
    @Test
    public void save() throws IOException {
        HttpClientContext context = HttpClientContext.create();
        Request<byte[]> request = new Request<>(context, "http://127.0.0.1:8180/sys/user/save", Request.Method.Post);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        request.setHeader("token", this.token);
        Map body = new HashMap(){{
            put("account", new Random().nextInt() % 10000000);
            put("password", DataUtils.MD(new Random().nextInt() % 10000 + "", DataUtils.KEY_SHA));
            put("deptId", 1L);
        }};
        System.out.println("request:" + request.getUrl() + "\n]");
        request.setBody(GsonBuilder.gsonDefault().toJson(body, Map.class).getBytes(StandardCharsets.UTF_8))
                .setHttpProxy("127.0.0.1", 8888);
        this.checkRequestBody(request, true);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        Response<byte[]> response = request.execute(byte[].class);
        this.checkResponseBody(response);
        System.out.println("====================================>");
    }


    @SneakyThrows
    @Test
    public void login() throws IOException {
        HttpClientContext context = HttpClientContext.create();
        Request<byte[]> request = new Request<>(context, "http://127.0.0.1:8180/sys/user/login", Request.Method.Post);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        request.setHeader("token", this.token);
        Map body = new HashMap(){{
            put("account", "admin");
            put("password", "jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=");
        }};
        System.out.println("request:" + request.getUrl() + "\n]");
        request.setBody(GsonBuilder.gsonDefault().toJson(body, Map.class).getBytes(StandardCharsets.UTF_8))
                .setHttpProxy("127.0.0.1", 8888);
        this.checkRequestBody(request, true);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        Response<byte[]> response = request.execute(byte[].class);
        this.checkResponseBody(response);
        System.out.println("====================================>");
    }

    @SneakyThrows
    @Test
    public void expire() throws IOException {
        HttpClientContext context = HttpClientContext.create();
        Request<byte[]> request;
        Response<byte[]> response;
        request = new Request<>(context, "http://127.0.0.1:8180/sys/user/expire", Request.Method.Get);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.setHeader("token", this.token);
        request.setHttpProxy("127.0.0.1", 8888);
        System.out.println("request:" + request.getUrl() + "\n]");
        this.checkRequestBody(request, false);
        response = request.execute(byte[].class);
        this.checkResponseBody(response);
        System.out.println("====================================>");
    }

    @SneakyThrows
    @Test
    public void delete() throws IOException {
        HttpClientContext context = HttpClientContext.create();
        Request<byte[]> request;
        Response<byte[]> response;
        request = new Request<>(context, "http://127.0.0.1:8180/sys/user/remove", Request.Method.Get);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.setHeader("token", this.token);
        request.setHttpProxy("127.0.0.1", 8888);
        request.setParam("ids", "1");
        System.out.println("request:" + request.getUrl() + "\n]");
        this.checkRequestBody(request, false);
        response = request.execute(byte[].class);
        this.checkResponseBody(response);
        System.out.println("====================================>");
    }

    @SneakyThrows
    @Test
    public void logine() throws IOException {
        HttpClientContext context = HttpClientContext.create();
        Request<byte[]> request = new Request<>(context, "http://127.0.0.1:8180/sys/user/login", Request.Method.Put);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        request.setHeader("token", this.token);
        Map body = new HashMap(){{
            put("account", "admin");
            put("password", "jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=");
        }};
        System.out.println("request:" + request.getUrl() + "\n]");
        request.setBody(GsonBuilder.gsonDefault().toJson(body, Map.class).getBytes(StandardCharsets.UTF_8))
                .setHttpProxy("127.0.0.1", 8888);
        this.checkRequestBody(request, true);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        Response<byte[]> response = request.execute(byte[].class);
        this.checkResponseBody(response);
        System.out.println("====================================>");
    }

    @SneakyThrows
    @Test
    public void login404() throws IOException {
        HttpClientContext context = HttpClientContext.create();
        Request<byte[]> request = new Request<>(context, "http://127.0.0.1:8180/sys2/user/login2", Request.Method.Post);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        request.setHeader("token", this.token);
        Map body = new HashMap(){{
            put("account", "admin");
            put("password", "jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=");
        }};
        System.out.println("request:" + request.getUrl() + "\n]");
        request.setBody(GsonBuilder.gsonDefault().toJson(body, Map.class).getBytes(StandardCharsets.UTF_8))
                .setHttpProxy("127.0.0.1", 8888);
        this.checkRequestBody(request, true);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        Response<byte[]> response = request.execute(byte[].class);
        this.checkResponseBody(response);
        System.out.println("====================================>");
    }

    @SneakyThrows
    @Test
    public void loginer() throws IOException {
        HttpClientContext context = HttpClientContext.create();
        Request<byte[]> request = new Request<>(context, "http://127.0.0.1:8180/sys/user/login", Request.Method.Post);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        request.setHeader("token", this.token);
        Map body = new HashMap(){{
            put("account", "admin1");
            put("password", "jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=");
        }};
        System.out.println("request:" + request.getUrl() + "\n]");
        request.setBody(GsonBuilder.gsonDefault().toJson(body, Map.class).getBytes(StandardCharsets.UTF_8))
                .setHttpProxy("127.0.0.1", 8888);
        this.checkRequestBody(request, true);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        Response<byte[]> response = request.execute(byte[].class);
        this.checkResponseBody(response);
        System.out.println("====================================>");
    }

    @After
    public void tearDown() throws Exception {}
}
