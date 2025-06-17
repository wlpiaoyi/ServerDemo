package org.wlpiaoyi.server.demo.common.tools.web.model;

//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.common.tools.web.domain.ErrorEnum;

import java.util.*;

/**
 * <p<b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>
 * 模型转换器
 * </p>
 * <p><b>{@code @date:}</b>22023/12/8 11:55</p>
 * <p><b>{@code @version:}</b>1.0</p>
 */
public class ModelWrapper {

    public static <T> T parseOne(Object orgObj, Class<T> resClazz) {
        if(orgObj == null){
            return null;
        }
        T resObj;
        try {
            resObj = resClazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.ModelParse, e);
        }
        BeanUtils.copyProperties(orgObj, resObj);
        return resObj;
    }

    public static <T> List<T> parseForList(Collection orgObjs, Class<T> resClazz) {
        if(orgObjs == null){
            return null;
        }
        if(orgObjs.isEmpty()){
            return new ArrayList<>();
        }
        List<T> resList = new ArrayList<>(orgObjs.size());
        for (Object orgObj : orgObjs){
            resList.add(parseOne(orgObj, resClazz));
        }
        return resList;
    }

    public static <T> Set<T> parseForSet(Collection orgObjs, Class<T> resClazz) {
        if(orgObjs == null){
            return null;
        }
        if(orgObjs.isEmpty()){
            return new HashSet<>();
        }
        Set<T> resList = new HashSet<>(orgObjs.size());
        for (Object orgObj : orgObjs){
            resList.add(parseOne(orgObj, resClazz));
        }
        return resList;
    }

    public static <PK, T> Map<PK, T> parseForMap(Map<PK, ?> orgMap, Map<PK, Class<T>> resClazz) {
        if(orgMap == null){
            return null;
        }
        if(orgMap.isEmpty()){
            return new HashMap<>();
        }
        Map<PK, T> resMap = new HashMap<>();
        for (Map.Entry<PK, ?> entry : orgMap.entrySet()){
            resMap.put(entry.getKey(), parseOne(entry.getValue(), resClazz.get(entry.getKey())));
        }
        return resMap;
    }

//    public static <T> IPage<T> parseForPage(IPage orgPage, Class<T> resClazz) {
//        if(orgPage == null){
//            return null;
//        }
//        if(ValueUtils.isBlank(orgPage.getRecords())){
//            return orgPage;
//        }
//        List<T> records = ModelWrapper.parseForList(orgPage.getRecords(), resClazz);
//        IPage<T> resPage = new Page(orgPage.getCurrent(), orgPage.getSize(), orgPage.getTotal());
//        resPage.setRecords(records);
//        return resPage;
//    }
}
