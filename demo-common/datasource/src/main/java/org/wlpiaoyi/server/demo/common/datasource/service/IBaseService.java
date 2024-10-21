package org.wlpiaoyi.server.demo.common.datasource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/9/16 12:28
 * {@code @version:}:       1.0
 */
public interface IBaseService<T> extends IService<T> {

    boolean deleteLogic(@NotEmpty List<Long> ids);

    boolean changeStatus(@NotEmpty List<Long> ids, Integer status);
}

