package org.wlpiaoyi.server.demo.common.tools.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wlpiaoyi.framework.utils.ValueUtils;

import java.util.HashMap;

/**
 * <p><b>{@code @author:}</b> wlpia</p>
 * <p><b>{@code @description:}</b> </p>
 * <p><b>{@code @date:}</b> 2024-11-10 10:53:08</p>
 * <p><b>{@code @version:}:</b> 1.0</p>
 */
public class WebUtilsTest {

    @Before
    public void setUp() throws Exception {}


    @Test
    public void mathPath(){
        //已xx开头的匹配
        String[] regexes = new String[]{"(/sys/.*)"};
        String path = "/sys/abc.do";
        boolean res = WebUtils.mathPath(path, regexes);
        System.out.println("xx开头的匹配 [path:" + path + "], [regexes:" + ValueUtils.toStrings(regexes) + "] [res:" + res + "]");

        regexes = new String[]{"(/sys/.*\\.do)"};
        path = "/sys/abc/abe.do";
        res = WebUtils.mathPath(path, regexes);
        System.out.println("xx开头xx结尾的匹配 [path:" + path + "], [regexes:" + ValueUtils.toStrings(regexes) + "] [res:" + res + "]");
        path = "/sys/abc/abe.action";
        res = WebUtils.mathPath(path, regexes);
        System.out.println("xx开头xx结尾的匹配 [path:" + path + "], [regexes:" + ValueUtils.toStrings(regexes) + "] [res:" + res + "]");

        regexes = new String[]{"(/sys/.*\\.do)", "(/sys/.*\\.action)"};
        path = "/sys/abc/abe.action";
        res = WebUtils.mathPath(path, regexes);
        System.out.println("xx开头xx结尾的匹配 [path:" + path + "], [regexes:" + ValueUtils.toStrings(regexes) + "] [res:" + res + "]");

        regexes = new String[]{"(/sys/.*\\.do|/sys/.*\\.action)"};
        path = "/sys/abc/abe.do";
        res = WebUtils.mathPath(path, regexes);
        System.out.println("多个xx开头xx结尾的匹配 [path:" + path + "], [regexes:" + ValueUtils.toStrings(regexes) + "] [res:" + res + "]");
        path = "/sys/abc/abe.action";
        res = WebUtils.mathPath(path, regexes);
        System.out.println("多个xx开头xx结尾的匹配 [path:" + path + "], [regexes:" + ValueUtils.toStrings(regexes) + "] [res:" + res + "]");
        path = "/sys/abc/abe.abc";
        res = WebUtils.mathPath(path, regexes);
        System.out.println("多个xx开头xx结尾的匹配 [path:" + path + "], [regexes:" + ValueUtils.toStrings(regexes) + "] [res:" + res + "]");

        regexes = new String[]{"^((?!(^/sys/user/login$|^/sys/user/expire$)).)*$"};
        path = "/sys/abc/abe.do";
        res = WebUtils.mathPath(path, regexes);
        System.out.println("排除xx的匹配 [path:" + path + "], [regexes:" + ValueUtils.toStrings(regexes) + "] [res:" + res + "]");
        path = "/sys/user/login";
        res = WebUtils.mathPath(path, regexes);
        System.out.println("排除xx的匹配 [path:" + path + "], [regexes:" + ValueUtils.toStrings(regexes) + "] [res:" + res + "]");
        path = "/sys/user/expire";
        res = WebUtils.mathPath(path, regexes);
        System.out.println("排除xx的匹配 [path:" + path + "], [regexes:" + ValueUtils.toStrings(regexes) + "] [res:" + res + "]");
        path = "/sys/user/expire/abc";
        res = WebUtils.mathPath(path, regexes);
        System.out.println("排除xx的匹配 [path:" + path + "], [regexes:" + ValueUtils.toStrings(regexes) + "] [res:" + res + "]");

    }

    @Test
    public void jwtTest(){
        String secretKey = "VGhpc0lzTXlDdXN0b21TZWNyZXRLZXkwMTIzNDU2Nzg=";
        String token = WebUtils.generateJwtToken(new HashMap(){{
            put("id","123");
        }},new HashMap(){{
            put("userName", "a");
            put("password", "1234");
        }}, 300, secretKey);
        WebUtils.isExpiredForToken(token, secretKey);
        WebUtils.getClaimsByToken(token, secretKey);
        WebUtils.getHeaderByToken(token, secretKey);
        System.out.println();
    }


    @After
    public void tearDown() throws Exception {}
}
