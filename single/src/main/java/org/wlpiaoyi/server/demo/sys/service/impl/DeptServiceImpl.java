package org.wlpiaoyi.server.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.sys.domain.entity.Role;
import org.wlpiaoyi.server.demo.sys.service.IDeptService;
import org.wlpiaoyi.server.demo.sys.domain.entity.Dept;
import org.wlpiaoyi.server.demo.sys.domain.mapper.DeptMapper;
import org.wlpiaoyi.server.demo.sys.domain.vo.DeptVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.DeptRo;
import org.wlpiaoyi.server.demo.service.impl.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

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
    public boolean deleteLogic(List<Long> ids) {
        if(this.baseMapper.selectCount(Wrappers.<Dept>lambdaQuery().in(Dept::getId, ids).eq(Dept::getCode, "admin")) > 0){
            throw new BusinessException("不能删除管理员");
        }
        return super.deleteLogic(ids);
    }
}
