package org.wlpiaoyi.server.demo.common.tools.utils;

import org.wlpiaoyi.framework.utils.snowflake.IdWorker;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/12/8 14:36
 * {@code @version:}:       1.0
 */
public class IdUtils {

    protected static IdWorker idWorker = new IdWorker((byte) 0, (byte) 0, 1694767192413L);

    public static long nextId(){
        return idWorker.nextId();
    }
}