package org.wlpiaoyi.server.demo.sys.domain.ro;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;


/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	平台 请求包装类
 * {@code @date:} 			2024-10-11 17:17:25
 * {@code @version:}: 		1.0
 */
public class PlatformRo {
    @Data
    @Schema(description = "平台 请求实例")
	public static class PlatformQuery extends org.wlpiaoyi.server.demo.utils.request.Query implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
		/** 平台名称 **/
		@Schema(name = "name" , description = "平台名称")
		@NotBlank(message = "平台名称不能为空")
		private String name;
		/** 平台编码 **/
		@Schema(name = "code" , description = "平台编码")
		@NotBlank(message = "平台编码不能为空")
		private String code;
    }

    @Data
    @Schema(description = "平台 请求实例")
    public static class PlatformSubmit implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
		/** 平台名称 **/
		@Schema(name = "name" , description = "平台名称")
		@NotBlank(message = "平台名称不能为空")
		private String name;
		/** 平台编码 **/
		@Schema(name = "code" , description = "平台编码")
		@NotBlank(message = "平台编码不能为空")
		private String code;
    }
}
