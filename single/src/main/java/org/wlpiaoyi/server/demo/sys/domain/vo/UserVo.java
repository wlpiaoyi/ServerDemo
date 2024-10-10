package org.wlpiaoyi.server.demo.sys.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.List;

import org.wlpiaoyi.server.demo.sys.domain.entity.User;

/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	用户表 视图实体类
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserVo extends User implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 当前部门
	 */
	private DeptVo dept;

	/**
	 * 当前角色
	 */
	private RoleVo curRole;

	/**
	 * 我的所有角色
	 */
	private List<RoleVo> roles;

}
