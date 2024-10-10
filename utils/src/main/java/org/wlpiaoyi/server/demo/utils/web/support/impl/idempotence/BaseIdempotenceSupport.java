package org.wlpiaoyi.server.demo.utils.web.support.impl.idempotence;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-09 17:07:13</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
public abstract class BaseIdempotenceSupport extends IdempotenceSupport{

    /**
     * 幂等URI的时间记录
     */
    private static final IdempotenceMoonMap IDEMPOTENCE_TIMER_MAP = new IdempotenceMoonMapImpl();

    @Override
    public final IdempotenceMoonMap getIdempotenceMoon() {
        return IDEMPOTENCE_TIMER_MAP;
    }


    static {
        new Thread(() -> {
            IdempotenceMoonMap.IdempotenceMoonIterator iterator = (key, value) -> IDEMPOTENCE_TIMER_MAP.remove(key);
            while (true){
                try{
                    Thread.sleep(20 * 60 * 1000);
                    IDEMPOTENCE_TIMER_MAP.iterator(iterator);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static class IdempotenceMoonMapImpl implements IdempotenceMoonMap {
        /**
         * 幂等URI的时间记录
         */
        private final Map<String, Long> idempotenceTimerMap = new HashMap<>();

        @Override
        public Long get(String key) {
            return idempotenceTimerMap.get(key);
        }

        @Override
        public void put(String key, Long value, Integer duriTime) {
            this.idempotenceTimerMap.put(key, value);
        }

        @Override
        public void remove(String key) {
            this.idempotenceTimerMap.remove(key);
        }

        @Override
        public void iterator(IdempotenceMoonIterator iterator) {
            if(idempotenceTimerMap.isEmpty()){
                return;
            }
            Set<Map.Entry<String, Long>> setEntries = idempotenceTimerMap.entrySet();
            for(Map.Entry<String, Long> entry : setEntries){
                iterator.iterator(entry.getKey(), entry.getValue());
            }
        }
    }
}
