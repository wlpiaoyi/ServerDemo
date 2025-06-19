package org.wlpiaoyi.server.demo.cloud.admin.test;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.exception.SystemException;
import org.wlpiaoyi.server.demo.common.tools.utils.SpringUtils;
import org.wlpiaoyi.server.demo.common.tools.web.domain.AuthUser;
import org.wlpiaoyi.server.demo.common.tools.web.model.R;

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

    @Autowired
    private TestAuthenticationService testAuthenticationService;

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

    @PostMapping("/test/login")
    public R login(HttpServletRequest request, @RequestBody String body) throws SystemException {
        AuthUserI user = new AuthUserI();
        user.setId(1001L);
        String token = this.testAuthenticationService.login(user);
        return R.success(new HashMap(){{
            put("header.token", token);
            put("body", body);
        }});
    }

    @GetMapping("/test/expire")
    public R expire() throws SystemException {
        if(this.testAuthenticationService.isLogin() == false){
            throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name());
        }
        AuthUserI userI = SpringUtils.getAuthUser();
        this.testAuthenticationService.refresh();
        return R.success(new HashMap(){{}});
    }

    @PostMapping("/test/post")
    public R testPost(HttpServletRequest request, @RequestBody Map body) throws SystemException {
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


    @PostMapping("/sys/post")
    public R sysPost(HttpServletRequest request, @RequestBody Map body) throws SystemException {
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
