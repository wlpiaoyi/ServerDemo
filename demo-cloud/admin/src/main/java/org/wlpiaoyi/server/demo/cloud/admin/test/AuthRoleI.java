package org.wlpiaoyi.server.demo.cloud.admin.test;

import org.wlpiaoyi.server.demo.common.tools.web.domain.AuthRole;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2025-06-19 14:47:51</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
public class AuthRoleI implements AuthRole {
    @Override
    public Object getId() {
        return 1001;
    }

    @Override
    public String getCode() {
        return "1001";
    }
}
