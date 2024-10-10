package org.wlpiaoyi.server.demo.sys.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.List;

import org.wlpiaoyi.server.demo.sys.domain.entity.Menu;

/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	菜单 视图实体类
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuVo extends Menu implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 下级菜单
	 */
	private List<MenuVo> children;

}
