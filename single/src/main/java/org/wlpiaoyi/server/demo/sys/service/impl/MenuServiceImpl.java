package org.wlpiaoyi.server.demo.sys.service.impl;

import org.wlpiaoyi.server.demo.sys.service.IMenuService;
import org.wlpiaoyi.server.demo.sys.domain.entity.Menu;
import org.wlpiaoyi.server.demo.sys.domain.mapper.MenuMapper;
import org.wlpiaoyi.server.demo.sys.domain.vo.MenuVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.MenuRo;
import org.wlpiaoyi.server.demo.service.impl.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	菜单 服务类实现
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Primary
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements IMenuService {


}