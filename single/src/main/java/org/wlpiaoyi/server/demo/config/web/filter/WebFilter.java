package org.wlpiaoyi.server.demo.config.web.filter;

import org.springframework.stereotype.Component;
import org.wlpiaoyi.server.demo.config.web.support.*;
import org.wlpiaoyi.server.demo.common.core.utils.SpringUtils;
import org.wlpiaoyi.server.demo.common.core.web.filter.WebBaseFilter;
import org.wlpiaoyi.server.demo.common.core.web.support.WebSupport;

import java.util.List;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-09 17:56:38</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Component
public class WebFilter extends WebBaseFilter {

    private List<WebSupport> webSupports;

    public WebFilter(){
        this.webSupports = List.of(
                SpringUtils.getBean(IdempotenceSupport.class),
                SpringUtils.getBean(DecryptSupport.class),
                SpringUtils.getBean(AuthenticationSupport.class),
                SpringUtils.getBean(CensorSupport.class),
                SpringUtils.getBean(AccessSupport.class),
                SpringUtils.getBean(EncryptSupport.class)
        );
    }

    @Override
    protected List<WebSupport> getWebSupports() {
        return this.webSupports;
    }
}
