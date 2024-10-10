package org.wlpiaoyi.server.demo.sys.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.List;

import org.wlpiaoyi.server.demo.sys.domain.entity.Role;

/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	角色 视图实体类
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleVo extends Role implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 当前角色菜单
	 */
	private List<MenuVo> menus;

}
