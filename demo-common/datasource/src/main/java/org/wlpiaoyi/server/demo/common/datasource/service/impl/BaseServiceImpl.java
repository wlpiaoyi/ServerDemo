package org.wlpiaoyi.server.demo.common.datasource.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.reflect.ClassModel;
import org.wlpiaoyi.server.demo.common.core.utils.SpringUtils;
import org.wlpiaoyi.server.demo.common.datasource.domain.entity.BaseEntity;
import org.wlpiaoyi.server.demo.common.datasource.domain.entity.CommonEntity;
import org.wlpiaoyi.server.demo.common.datasource.service.IBaseService;
import org.wlpiaoyi.server.demo.common.core.utils.IdUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p><b>{@code @description:}</b>  基础服务类</p>
 * <p><b>{@code @date:}</b>         2023/9/16 12:26</p>
 * <p><b>{@code @author:}</b>       wlpiaoyi</p>
 * <p><b>{@code @version:}</b>      1.0</p>
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements IBaseService<T> {

//    protected Class<M> currentMapperClass() {
//        return (Class<M>) this.getResolvableType().as(BaseServiceImpl.class).getGeneric(new int[]{0}).getType();
//    }
//
//    protected Class<T> currentModelClass() {
//        return (Class<T>) this.getResolvableType().as(BaseServiceImpl.class).getGeneric(new int[]{1}).getType();
//    }
//
//    protected ResolvableType getResolvableType() {
//        return ResolvableType.forClass(ClassUtils.getUserClass(this.getClass()));
//    }

    public boolean save(T entity) {
        this.resolveEntityForSave(entity);
        return super.save(entity);
    }

    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        entityList.forEach(this::resolveEntityForSave);
        return super.saveBatch(entityList, batchSize);
    }

    public boolean updateById(T entity) {
        this.resolveEntityForMerge(entity);
        return super.updateById(entity);
    }

    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        entityList.forEach(this::resolveEntityForMerge);
        return super.updateBatchById(entityList, batchSize);
    }

    public boolean saveOrUpdate(T entity) {
        this.resolveEntityForMerge(entity);
        return entity.getId() == null ? this.save(entity) : this.updateById(entity);
    }

    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        entityList.forEach(this::resolveEntityForMerge);
        return super.saveOrUpdateBatch(entityList, batchSize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLogic(List<Long> ids) {
        List<T> list = new ArrayList<>();
        ids.forEach((id) -> {
            T entity = ClassModel.newInstance(this.currentModelClass());
            entity.setId(id);
            if(entity instanceof CommonEntity){
                ((CommonEntity) entity).setIsDeleted(1);
                ((CommonEntity) entity).setUpdateTime(new Date());
            }else{
                throw new BusinessException("not support logic delete");
            }
            list.add(entity);
        });
        return super.updateBatchById(list) && super.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeStatus(List<Long> ids, Integer status) {
        List<T> list = new ArrayList<>();
        ids.forEach((id) -> {
            T entity = ClassModel.newInstance(this.currentModelClass());
            entity.setId(id);
            if(entity instanceof CommonEntity){
                ((CommonEntity) entity).setUpdateTime(new Date());
                ((CommonEntity) entity).setStatus(status);
                Object user = SpringUtils.getAuthUser();
                if(user != null && ValueUtils.isBlank(((CommonEntity) entity).getUpdateUser())){
                    ((CommonEntity) entity).setUpdateUser(((BaseEntity) user).getId());
                }
            }else{
                throw new BusinessException("not support change status");
            }
            list.add(entity);
        });
        return super.updateBatchById(list);
    }

    private void resolveEntityForSave(T entity) {
        if(ValueUtils.isBlank(entity.getId())){
            entity.setId(IdUtils.nextId());
        }
        if(entity instanceof CommonEntity){
            if(((CommonEntity) entity).getCreateTime() == null){
                ((CommonEntity) entity).setCreateTime(new Date());
            }
            Object user = SpringUtils.getAuthUser();
            if(user != null && ValueUtils.isBlank(((CommonEntity) entity).getCreateUser())){
                ((CommonEntity) entity).setCreateUser(((BaseEntity) user).getId());
            }
        }
    }

    private void resolveEntityForMerge(T entity) {
        if(ValueUtils.isBlank(entity.getId())){
            this.resolveEntityForSave(entity);
        }else{
            if(entity instanceof CommonEntity){
                if(((CommonEntity) entity).getUpdateTime() == null){
                    ((CommonEntity) entity).setUpdateTime(new Date());
                }
                Object user = SpringUtils.getAuthUser();
                if(user != null && ValueUtils.isBlank(((CommonEntity) entity).getUpdateUser())){
                    ((CommonEntity) entity).setUpdateUser(((BaseEntity) user).getId());
                }
            }
        }
    }

}
