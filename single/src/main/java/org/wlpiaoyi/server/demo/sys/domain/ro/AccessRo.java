package org.wlpiaoyi.server.demo.sys.domain.ro;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.util.Collection;

import jakarta.validation.constraints.NotBlank;
import org.wlpiaoyi.server.demo.common.core.request.Query;


/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	数据权限 请求包装类
 * {@code @date:} 			2024-10-11 17:17:25
 * {@code @version:}: 		1.0
 */
public class AccessRo {
    @Data
    @Schema(description = "数据权限 请求实例")
	public static class AccessQuery extends Query implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
		/** value **/
		@Schema(name = "value" , description = "value")
		@NotBlank(message = "value不能为空")
		private String value;
		/** path **/
		@Schema(name = "path" , description = "path")
		@NotBlank(message = "path不能为空")
		private String path;
    }

    @Data
    @Schema(description = "数据权限 请求实例")
    public static class AccessSubmit implements Serializable {

        private static final long serialVersionUID = 1L;

		@Schema(description = "角色Id")
		@NotBlank(message = "角色Id不能为空")
		private Long roleId;

		@Schema(description = "添加的数据权限Id")
		private Collection<Long> addAccessIds;

		@Schema(description = "删除的数据权限Id")
		private Collection<Long> delAccessIds;
    }
}
