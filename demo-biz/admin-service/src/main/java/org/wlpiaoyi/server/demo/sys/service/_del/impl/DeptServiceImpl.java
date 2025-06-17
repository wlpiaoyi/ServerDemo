package org.wlpiaoyi.server.demo.sys.service._del.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.common.datasource.service.impl.BaseServiceImpl;
import org.wlpiaoyi.server.demo.sys.domain.entity.Dept;
import org.wlpiaoyi.server.demo.sys.domain.mapper.DeptMapper;
import org.wlpiaoyi.server.demo.sys.service._del.IDeptService;

import java.util.List;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	部门 服务类实现
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Primary
@Service
public class DeptServiceImpl extends BaseServiceImpl<DeptMapper, Dept> implements IDeptService {

    @Override
    public boolean save(Dept entity) {
        if("admin".equals(entity.getCode())){
            throw new BusinessException("不能新增管理员部门");
        }
        if(ValueUtils.isNotBlank(entity.getParentId()) && this.baseMapper.selectById(entity.getParentId()) == null){
            throw new BusinessException("没有找到对应的上级部门");
        }
        return super.save(entity);
    }

    @Override
    public boolean updateById(Dept entity) {
        Dept db = this.baseMapper.selectById(entity.getId());
        if("admin".equals(db.getCode())){
            throw new BusinessException("不能操作管理员部门");
        }
        if("admin".equals(entity.getCode())){
            throw new BusinessException("不能修改为管理员部门");
        }
        if(ValueUtils.isNotBlank(entity.getParentId()) && this.baseMapper.selectById(entity.getParentId()) == null){
            throw new BusinessException("没有找到对应的上级部门");
        }
        return super.updateById(entity);
    }

    @Override
    public boolean deleteLogic(List<Long> ids) {
        if(this.baseMapper.selectCount(Wrappers.<Dept>lambdaQuery().in(Dept::getId, ids).eq(Dept::getCode, "admin")) > 0){
            throw new BusinessException("不能删除管理员");
        }
        return super.deleteLogic(ids);
    }
}
