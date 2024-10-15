package org.wlpiaoyi.server.demo.cache;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wlpiaoyi.server.demo.sys.domain.entity.Dept;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-14 16:27:12</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Service
public class DeptCachesService extends CachesService<Dept>{

    @Value("${wlpiaoyi.ee.cache.duri_minutes}")
    private int cacheDuriMinutes;

    @Override
    protected long getCacheDuriMinutes() {
        return this.cacheDuriMinutes;
    }

    @Override
    protected String getKeyTag() {
        return "dept";
    }
}
