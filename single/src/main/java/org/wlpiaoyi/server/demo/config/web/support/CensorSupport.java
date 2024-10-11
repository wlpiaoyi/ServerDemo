package org.wlpiaoyi.server.demo.config.web.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-10 10:07:34</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Component
public class CensorSupport extends org.wlpiaoyi.server.demo.utils.web.support.impl.censor.CensorSupport {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected boolean censor(String token) {
        if(ValueUtils.isBlank(token)){
            return false;
        }
        String value = this.redisTemplate.opsForValue().get(token);
        return  !ValueUtils.isBlank(value);
    }

    @Override
    public String[] getURIRegexes() {
        return new String[]{"/test/censor/.*|/test/common/.*","(^(?!/test/common/do).*$)"};
    }
}
