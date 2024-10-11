package org.wlpiaoyi.server.demo.sys.domain.ro;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	用户表 请求包装类
 * {@code @date:} 			2024-10-11 17:17:25
 * {@code @version:}: 		1.0
 */
public class UserRo {
    @Data
    @Schema(description = "用户表 请求实例")
	public static class UserQuery extends org.wlpiaoyi.server.demo.utils.request.Query implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
		/** 账号 **/
		@Schema(name = "account" , description = "账号")
		@NotBlank(message = "账号不能为空")
		private String account;
		/** 密码 **/
		@Schema(name = "password" , description = "密码")
		@NotBlank(message = "密码不能为空")
		private String password;
		/** 部门ID **/
		@Schema(name = "deptId" , description = "部门ID")
		@NotNull(message = "部门ID不能为空")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long deptId;
    }

    @Data
    @Schema(description = "用户表 请求实例")
    public static class UserSubmit implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
		/** 账号 **/
		@Schema(name = "account" , description = "账号")
		@NotBlank(message = "账号不能为空")
		private String account;
		/** 密码 **/
		@Schema(name = "password" , description = "密码")
		@NotBlank(message = "密码不能为空")
		private String password;
		/** 部门ID **/
		@Schema(name = "deptId" , description = "部门ID")
		@NotNull(message = "部门ID不能为空")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long deptId;
    }

	@Data
	@Schema(description = "用户认证 请求实例")
	public static class UserAuth implements Serializable {

		private static final long serialVersionUID = 1L;

		/** 账号 **/
		@Schema(name = "account" , description = "账号")
		@NotBlank(message = "账号不能为空")
		private String account;
		/** 密码 **/
		@Schema(name = "password" , description = "密码")
		@NotBlank(message = "密码不能为空")
		private String password;
	}
}
