package org.wlpiaoyi.server.demo.sys.domain.ro;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import org.wlpiaoyi.server.demo.domain.entity.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	用户表 请求包装类
 * {@code @date:} 			2024-10-10 23:41:53
 * {@code @version:}: 		1.0
 */
public class UserRo {
    @Data
    @Schema(description = "用户表 请求实例")
	public static class Query extends org.wlpiaoyi.server.demo.utils.request.Query implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
		/** account **/
		@Schema(description = "account")
		@NotBlank(message = "account不能为空")
		private String account;
		/** password **/
		@Schema(description = "password")
		@NotBlank(message = "password不能为空")
		private String password;
		/** deptId **/
		@Schema(description = "deptId")
		@NotNull(message = "deptId不能为空")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long deptId;
    }

    @Data
    @Schema(description = "用户表 请求实例")
    public static class Submit implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
		/** account **/
		@Schema(description = "account")
		@NotBlank(message = "account不能为空")
		private String account;
		/** password **/
		@Schema(description = "password")
		@NotBlank(message = "password不能为空")
		private String password;
		/** deptId **/
		@Schema(description = "deptId")
		@NotNull(message = "deptId不能为空")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long deptId;
    }

	@Data
	@Schema(description = "用户认证 请求实例")
	public static class Auth implements Serializable {

		private static final long serialVersionUID = 1L;
		/** account **/
		@Schema(description = "account")
		@NotBlank(message = "account不能为空")
		private String account;
		/** password **/
		@Schema(description = "password")
		@NotBlank(message = "password不能为空")
		private String password;
	}
}
