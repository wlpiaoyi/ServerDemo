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
 * {@code @description:} 	菜单 实体类
 * {@code @date:} 			2024-10-11 17:17:25
 * {@code @version:}: 		1.0
 */
@Data
@Accessors(chain=true)
@TableName("sys_menu")
@Schema(description = "菜单")
@EqualsAndHashCode(callSuper = true)
public class Menu extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /** 上级ID,如果为空就是顶级节点 **/
    @Schema(name = "parentId" , description = "上级ID:如果为空就是顶级节点")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    /** 名称 **/
    @Schema(name = "name" , description = "名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    /** 编码 **/
    @Schema(name = "code" , description = "编码")
    @NotBlank(message = "编码不能为空")
    private String code;

    /** 事件响应 **/
    @Schema(name = "action" , description = "事件响应")
    private String action;

    /** 排序 **/
    @Schema(name = "sort" , description = "排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    /** icon **/
    @Schema(name = "icon" , description = "icon")
    private String icon;

    /** 菜单类型=(0:未知类型, 1:菜单, 2:按钮) **/
    @Schema(name = "type" , description = "菜单类型 enums(0:未知类型, 1:菜单, 2:按钮)" , examples = {"0:未知类型"," 1:菜单"," 2:按钮"})
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

}
