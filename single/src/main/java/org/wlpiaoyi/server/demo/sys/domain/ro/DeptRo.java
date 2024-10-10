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


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	部门 请求包装类
 * {@code @date:} 			2024-10-10 23:41:53
 * {@code @version:}: 		1.0
 */
public class DeptRo {
    @Data
    @Schema(description = "部门 请求实例")
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
		/** name **/
		@Schema(description = "name")
		@NotBlank(message = "name不能为空")
		private String name;
		/** 部门编码 **/
		@Schema(description = "部门编码")
		@NotBlank(message = "部门编码不能为空")
		private String code;
    }

    @Data
    @Schema(description = "部门 请求实例")
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
		/** name **/
		@Schema(description = "name")
		@NotBlank(message = "name不能为空")
		private String name;
		/** 部门编码 **/
		@Schema(description = "部门编码")
		@NotBlank(message = "部门编码不能为空")
		private String code;
    }
}