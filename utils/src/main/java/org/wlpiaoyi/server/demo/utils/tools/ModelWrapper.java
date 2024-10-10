package org.wlpiaoyi.server.demo.utils.tools;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.wlpiaoyi.framework.utils.ValueUtils;

import java.util.*;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/12/8 11:55
 * {@code @version:}:       1.0
 */
public class ModelWrapper {

    @SneakyThrows
    public static <T> T parseOne(Object orgObj, Class<T> resClazz) {
//        T resObj = resClazz.newInstance();
        T resObj = resClazz.getDeclaredConstructor().newInstance();
        BeanUtils.copyProperties(orgObj, resObj);
        return resObj;
    }

    public static <T> List<T> parseForList(Collection orgObjs, Class<T> resClazz) {
        List<T> resList = new ArrayList<>(orgObjs.size());
        for (Object orgObj : orgObjs){
            resList.add(parseOne(orgObj, resClazz));
        }
        return resList;
    }

    public static <T> Set<T> parseForSet(Collection orgObjs, Class<T> resClazz) {
        Set<T> resList = new HashSet<>(orgObjs.size());
        for (Object orgObj : orgObjs){
            resList.add(parseOne(orgObj, resClazz));
        }
        return resList;
    }

    public static <PK, T> Map<PK, T> parseForMap(Map<PK, ?> orgMap, Class<T> resClazz) {
        Map<PK, T> resMap = new HashMap<>();
        for (Map.Entry<PK, ?> entry : orgMap.entrySet()){
            resMap.put(entry.getKey(), parseOne(entry.getValue(), resClazz));
        }
        return resMap;
    }

    public static <T> IPage<T> parseForPage(IPage orgPage, Class<T> resClazz) {
        if(orgPage == null){
            return null;
        }
        if(ValueUtils.isBlank(orgPage.getRecords())){
            return orgPage;
        }
        List<T> records = ModelWrapper.parseForList(orgPage.getRecords(), resClazz);
        IPage<T> resPage = new Page(orgPage.getCurrent(), orgPage.getSize(), orgPage.getTotal());
        resPage.setRecords(records);
        return resPage;
    }
}
