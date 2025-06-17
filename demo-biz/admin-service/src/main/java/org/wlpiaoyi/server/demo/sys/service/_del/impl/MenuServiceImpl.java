package org.wlpiaoyi.server.demo.sys.service._del.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.server.demo.common.datasource.service.impl.BaseServiceImpl;
import org.wlpiaoyi.server.demo.sys.domain.entity.Menu;
import org.wlpiaoyi.server.demo.sys.domain.mapper.MenuMapper;
import org.wlpiaoyi.server.demo.sys.service._del.IMenuService;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	菜单 服务类实现
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Primary
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements IMenuService {


    protected void checkForSave(Menu entity){
        if(ValueUtils.isNotBlank(entity.getParentId())){
            Menu menu = this.getById(entity.getParentId());
            if(menu == null){
                throw new BusinessException("未找到上级菜单");
            }
            if(ValueUtils.isNotBlank(menu.getType())){
                throw new BusinessException("菜单类型不能为空");
            }
            if(menu.getType().intValue() != 1 ){
                throw new BusinessException("上级菜单类型错误");
            }
        }
    }

    protected void checkForUpdate(Menu entity){
        if(ValueUtils.isNotBlank(entity.getParentId())){
            Menu menu = this.getById(entity.getParentId());
            if(menu == null){
                throw new BusinessException("未找到上级菜单");
            }
            if(ValueUtils.isNotBlank(menu.getType())){
                throw new BusinessException("菜单类型不能为空");
            }
            if(menu.getType().intValue() != 1 ){
                throw new BusinessException("上级菜单类型错误");
            }
        }
    }

    @Override
    public boolean save(Menu entity) {
        this.checkForSave(entity);
        return super.save(entity);
    }

    @Override
    public boolean updateById(Menu entity) {
        return super.updateById(entity);
    }
}
