package org.wlpiaoyi.server.demo.common.core.web.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DoFilterEnum {

    Unknown(0b0),
    GoNext(0b1),
    CloseReq(0b1 << 1),
    CloseResp(0b1 << 2),
    ErrorResp(0b1 << 3),
    UndoChain(0b1 << 30);

    private int value;

}
