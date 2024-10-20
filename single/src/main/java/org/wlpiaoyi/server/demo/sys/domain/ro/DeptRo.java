package org.wlpiaoyi.server.demo.sys.domain.ro;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import org.wlpiaoyi.server.demo.common.core.request.Query;


/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	部门 请求包装类
 * {@code @date:} 			2024-10-11 17:17:25
 * {@code @version:}: 		1.0
 */
public class DeptRo {
    @Data
    @Schema(description = "部门 请求实例")
	public static class DeptQuery extends Query implements Serializable {

        private static final long serialVersionUID = 1L;

		/** 上级部门Id **/
		@Schema(name = "parentId" , description = "上级部门Id")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long parentId;
		/** 部门名称 **/
		@Schema(name = "name" , description = "部门名称")
		@NotBlank(message = "部门名称不能为空")
		private String name;
		/** 部门编码 **/
		@Schema(name = "code" , description = "部门编码")
		@NotBlank(message = "部门编码不能为空")
		private String code;
    }

    @Data
    @Schema(description = "部门 请求实例")
    public static class DeptSubmit implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
		/** 上级部门Id **/
		@Schema(name = "parentId" , description = "上级部门Id")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long parentId;
		/** 部门名称 **/
		@Schema(name = "name" , description = "部门名称")
		@NotBlank(message = "部门名称不能为空")
		private String name;
		/** 部门编码 **/
		@Schema(name = "code" , description = "部门编码")
		@NotBlank(message = "部门编码不能为空")
		private String code;
    }
}
