package org.wlpiaoyi.server.demo.common.datasource.tools;

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
public class ModelWrapper extends org.wlpiaoyi.server.demo.common.core.tools.ModelWrapper {

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
