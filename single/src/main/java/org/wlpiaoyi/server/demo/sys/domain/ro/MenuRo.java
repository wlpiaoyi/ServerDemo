package org.wlpiaoyi.server.demo.sys.domain.ro;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import org.wlpiaoyi.server.demo.domain.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	菜单 请求包装类
 * {@code @date:} 			2024-10-10 23:41:53
 * {@code @version:}: 		1.0
 */
public class MenuRo {
    @Data
    @Schema(description = "菜单 请求实例")
	public static class Query extends org.wlpiaoyi.server.demo.utils.request.Query implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
		/** parentId **/
		@Schema(description = "parentId")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long parentId;
		/** 菜单名称 **/
		@Schema(description = "菜单名称")
		@NotBlank(message = "菜单名称不能为空")
		private String name;
		/** 菜单编码 **/
		@Schema(description = "菜单编码")
		@NotBlank(message = "菜单编码不能为空")
		private String code;
		/** action **/
		@Schema(description = "action")
		private String action;
		/** icon **/
		@Schema(description = "icon")
		private String icon;
		/** 菜单类型=(0:未知类型, 1:菜单, 2:按钮) **/
		@Schema(description = "菜单类型=(0:未知类型, 1:菜单, 2:按钮)")
		@NotNull(message = "菜单类型=(0:未知类型, 1:菜单, 2:按钮)不能为空")
		private Integer type;
    }

    @Data
    @Schema(description = "菜单 请求实例")
    public static class Submit implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
		/** parentId **/
		@Schema(description = "parentId")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long parentId;
		/** 菜单名称 **/
		@Schema(description = "菜单名称")
		@NotBlank(message = "菜单名称不能为空")
		private String name;
		/** 菜单编码 **/
		@Schema(description = "菜单编码")
		@NotBlank(message = "菜单编码不能为空")
		private String code;
		/** action **/
		@Schema(description = "action")
		private String action;
		/** icon **/
		@Schema(description = "icon")
		private String icon;
		/** 菜单类型=(0:未知类型, 1:菜单, 2:按钮) **/
		@Schema(description = "菜单类型=(0:未知类型, 1:菜单, 2:按钮)")
		@NotNull(message = "菜单类型=(0:未知类型, 1:菜单, 2:按钮)不能为空")
		private Integer type;
    }
}