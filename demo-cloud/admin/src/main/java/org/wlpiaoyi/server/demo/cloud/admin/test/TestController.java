package org.wlpiaoyi.server.demo.cloud.admin.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.framework.utils.exception.SystemException;
import org.wlpiaoyi.server.demo.common.core.response.R;
import org.wlpiaoyi.server.demo.common.core.web.annotation.Encrypt;
import org.wlpiaoyi.server.demo.common.core.web.annotation.Idempotence;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-21 17:17:59</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class TestController {


    @GetMapping("/doing")
    public R doing() throws SystemException {
        return R.success(System.currentTimeMillis());
    }

    @GetMapping("/test/get")
    public R get(@RequestHeader String testAdd, @RequestHeader String testEdit) throws SystemException {
        return R.success(new HashMap(){{
            put("testAdd", testAdd);
            put("testEdit", testEdit);
        }});
    }

    @PostMapping("/test/post")
    public R post(HttpServletRequest request, @RequestBody String body) throws SystemException {
        Map headers = new HashMap<>();
        Iterator<String> iterator = request.getHeaderNames().asIterator();
        while (iterator.hasNext()){
            String headerName = iterator.next();
            headers.put(headerName, request.getHeader(headerName));
        }
        return R.success(new HashMap(){{
            put("header", headers);
            put("body", body);
        }});
    }

}
