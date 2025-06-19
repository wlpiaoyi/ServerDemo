package org.wlpiaoyi.server.demo.cloud.admin.test;

import lombok.Data;
import org.wlpiaoyi.server.demo.common.tools.web.domain.AuthUser;

@Data
public class AuthUserI implements AuthUser<Long> {
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getAccount() {
        return "account" + this.id;
    }

    @Override
    public String getRoleCode() {
        return "role" + this.id;
    }
}
