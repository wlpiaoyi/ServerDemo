package org.wlpiaoyi.server.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.transaction.annotation.Transactional;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.server.demo.config.web.support.AccessSupport;
import org.wlpiaoyi.server.demo.sys.service.IAccessService;
import org.wlpiaoyi.server.demo.sys.domain.entity.Access;
import org.wlpiaoyi.server.demo.sys.domain.mapper.AccessMapper;
import org.wlpiaoyi.server.demo.sys.domain.vo.AccessVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.AccessRo;
import org.wlpiaoyi.server.demo.service.impl.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.wlpiaoyi.server.demo.utils.IdUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	数据权限 服务类实现
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Primary
@Service
public class AccessServiceImpl extends BaseServiceImpl<AccessMapper, Access> implements IAccessService {

    @Transactional
    @Override
    public void mergeAll(){
        List<Long> ids = new ArrayList<>();
        for (String uri : AccessSupport.ACCESS_URI_SET.getAllUri()){
            String value = AccessSupport.ACCESS_URI_SET.get(uri);
            if(ValueUtils.isBlank(value)){
                continue;
            }
            ids.add(this.merge(uri, value).getId());
        }
        if(ValueUtils.isNotBlank(ids)){
            List<Access> removes = this.baseMapper.selectList(Wrappers.<Access>lambdaQuery().notIn(Access::getId, ids));
            if(ValueUtils.isNotBlank(removes)){
                this.baseMapper.deleteBatchIds(removes.stream().map(Access::getId).collect(Collectors.toList()));
            }
        }
    }

    @Transactional
    public Access merge(String uri, String value) {
        Access access = this.getOne(Wrappers.<Access>lambdaQuery().eq(Access::getPath, uri).eq(Access::getValue, value));
        if(access != null){
            return access;
        }
        access = new Access();
        access.setId(IdUtils.nextId());
        access.setPath(uri);
        access.setValue(value);
        this.save(access);
        return access;
    }
}
