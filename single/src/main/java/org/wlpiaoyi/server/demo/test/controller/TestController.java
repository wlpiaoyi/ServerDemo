package org.wlpiaoyi.server.demo.test.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;
import org.wlpiaoyi.server.demo.utils.web.annotation.Idempotence;
import org.wlpiaoyi.server.demo.utils.web.annotation.PreAuthorize;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-09-18 00:20:45</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/auth/login")
    @Idempotence
    @ApiOperationSupport(order = 1)
    @Operation(summary = "授权令牌")
    public R authLogin(@RequestHeader String token, @RequestBody Map body) {
        this.redisTemplate.opsForValue().set(token, System.currentTimeMillis() + "", 5, TimeUnit.MINUTES);
        body.put("currentTimeMillis", System.currentTimeMillis());
        return R.success(body);
    }

    @GetMapping("/censor/list")
    @ApiOperationSupport(order = 2)
    @PreAuthorize("path1")
    @Operation(summary = "审查令牌1")
    public R censorList(@RequestHeader String token) {
        return R.success(System.currentTimeMillis());
    }

    @PostMapping("/common/list")
    @Operation(summary = "审查令牌2")
    @PreAuthorize("path2")
    @ApiOperationSupport(order = 3)
    public R commonList(@RequestHeader String token) {
        return R.success(System.currentTimeMillis());
    }

    @GetMapping("/common/do")
    @Operation(summary = "无审查令牌")
    @ApiOperationSupport(order = 4)
    public R commonDo(@RequestHeader String token) {
        return R.success(System.currentTimeMillis());
    }
}
