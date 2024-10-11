package org.wlpiaoyi.server.demo.sys.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import org.wlpiaoyi.server.demo.domain.entity.BaseEntity;
import org.wlpiaoyi.server.demo.domain.entity.CommonEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	数据字典 实体类
 * {@code @date:} 			2024-10-11 17:17:25
 * {@code @version:}: 		1.0
 */
@Data
@Accessors(chain=true)
@TableName("sys_dict")
@Schema(description = "数据字典")
@EqualsAndHashCode(callSuper = true)
public class Dict extends CommonEntity implements Serializable {

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

    /** 树深度 **/
    @Schema(name = "deep" , description = "树深度")
    @NotNull(message = "树深度不能为空")
    private Integer deep;

    /** 是否是叶子节点 **/
    @Schema(name = "isLeaf" , description = "是否是叶子节点")
    @NotNull(message = "是否是叶子节点不能为空")
    private Byte isLeaf;

}