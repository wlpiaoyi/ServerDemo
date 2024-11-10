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
import org.wlpiaoyi.server.demo.common.tools.web.model.Query;


/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	数据字典 请求包装类
 * {@code @date:} 			2024-10-11 17:17:25
 * {@code @version:}: 		1.0
 */
public class DictRo {
    @Data
    @Schema(description = "数据字典 请求实例")
	public static class DictQuery extends Query implements Serializable {

        private static final long serialVersionUID = 1L;
		/** 上级节点 **/
		@Schema(name = "parentId" , description = "上级节点")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long parentId;
		/** 字典名称 **/
		@Schema(name = "name" , description = "字典名称")
		@NotBlank(message = "字典名称不能为空")
		private String name;
		/** 字典编码 **/
		@Schema(name = "code" , description = "字典编码")
		@NotBlank(message = "字典编码不能为空")
		private String code;
		/** 是否公开,如果是公开则所有人都能看到,否则只有指定角色的人才能看到=(0:非公开,1:公开) **/
		@Schema(name = "isPublic" , description = "是否公开:如果是公开则所有人都能看到,否则只有指定角色的人才能看到 enums(0:非公开,1:公开)" , examples = {"0:非公开","1:公开"})
		@NotNull(message = "是否公开不能为空")
		private Byte isPublic;
    }

    @Data
    @Schema(description = "数据字典 请求实例")
    public static class DictSubmit implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
		/** 上级节点 **/
		@Schema(name = "parentId" , description = "上级节点")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long parentId;
		/** 字典名称 **/
		@Schema(name = "name" , description = "字典名称")
		@NotBlank(message = "字典名称不能为空")
		private String name;
		/** 字典编码 **/
		@Schema(name = "code" , description = "字典编码")
		@NotBlank(message = "字典编码不能为空")
		private String code;
		/** 字典值 **/
		@Schema(name = "value" , description = "字典值")
		@NotBlank(message = "字典值不能为空")
		private String value;
		/** 是否公开,如果是公开则所有人都能看到,否则只有指定角色的人才能看到=(0:非公开,1:公开) **/
		@Schema(name = "isPublic" , description = "是否公开:如果是公开则所有人都能看到,否则只有指定角色的人才能看到 enums(0:非公开,1:公开)" , examples = {"0:非公开","1:公开"})
		@NotNull(message = "是否公开不能为空")
		private Byte isPublic;
		/** 排序 **/
		@Schema(name = "sort" , description = "排序")
		@NotNull(message = "排序不能为空")
		private Integer sort;
    }
}
