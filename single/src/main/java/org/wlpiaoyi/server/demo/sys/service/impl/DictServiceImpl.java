package org.wlpiaoyi.server.demo.sys.service.impl;

import org.springframework.transaction.annotation.Transactional;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.sys.service.IDictService;
import org.wlpiaoyi.server.demo.sys.domain.entity.Dict;
import org.wlpiaoyi.server.demo.sys.domain.mapper.DictMapper;
import org.wlpiaoyi.server.demo.common.datasource.service.impl.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	数据字典 服务类实现
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Primary
@Service
public class DictServiceImpl extends BaseServiceImpl<DictMapper, Dict> implements IDictService {

    @Transactional
    @Override
    public boolean save(Dict entity) {
        if(ValueUtils.isNotBlank(entity.getParentId())){
            Dict parent = this.baseMapper.selectById(entity.getParentId());
            if(parent == null){
                throw new BusinessException("上级Id不能为空");
            }
            entity.setDeep(parent.getDeep() + 1);
            Dict pDb = new Dict();
            pDb.setIsLeaf((byte) 0);
            pDb.setId(parent.getId());
            this.baseMapper.updateById(pDb);
        }
        if(ValueUtils.isBlank(entity.getSort())){
            entity.setSort(0);
        }
        entity.setIsLeaf((byte) 1);
        return super.save(entity);
    }

    @Override
    public boolean updateById(Dict entity) {
        if(ValueUtils.isNotBlank(entity.getParentId())){
            Dict parent = this.baseMapper.selectById(entity.getParentId());
            if(parent == null){
                throw new BusinessException("上级Id不能为空");
            }
            entity.setDeep(parent.getDeep() + 1);
            Dict pDb = new Dict();
            pDb.setIsLeaf((byte) 0);
            pDb.setId(parent.getId());
            this.baseMapper.updateById(pDb);
        }
        return super.updateById(entity);
    }
}
