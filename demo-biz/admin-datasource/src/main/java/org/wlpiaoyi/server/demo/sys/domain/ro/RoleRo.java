package org.wlpiaoyi.server.demo.sys.domain.ro;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.wlpiaoyi.server.demo.common.tools.web.model.Query;

import java.io.Serializable;


/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	角色 请求包装类
 * {@code @date:} 			2024-10-11 17:17:25
 * {@code @version:}: 		1.0
 */
public class RoleRo {
    @Data
    @Schema(description = "角色 请求实例")
	public static class RoleQuery extends Query implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
		/** 角色名称 **/
		@Schema(name = "name" , description = "角色名称")
		private String name;
		/** 角色编码=(amdin:管理员,...) **/
		@Schema(name = "code" , description = "角色编码 enums(amdin:管理员,...)" , examples = {"amdin:管理员","..."})
		private String code;
    }

    @Data
    @Schema(description = "角色 请求实例")
    public static class RoleSubmit implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
		/** 角色名称 **/
		@Schema(name = "name" , description = "角色名称")
		private String name;
		/** 角色编码=(amdin:管理员,...) **/
		@Schema(name = "code" , description = "角色编码 enums(amdin:管理员,...)" , examples = {"amdin:管理员","..."})
		private String code;
    }
}
