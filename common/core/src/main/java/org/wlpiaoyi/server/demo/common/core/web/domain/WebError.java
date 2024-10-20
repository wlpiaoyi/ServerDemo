package org.wlpiaoyi.server.demo.common.core.web.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wlpiaoyi.framework.utils.exception.Error;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-18 10:57:57</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Getter
@AllArgsConstructor
public enum WebError implements Error {

    UnLogin(402, "Not login"),
    UnAccess(412, "Not access this path"),
    Unknown(500, "System error");

    private int code;

    private String message;
}
