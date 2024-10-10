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
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	菜单 实体类
 * {@code @date:} 			2024-10-10 23:41:53
 * {@code @version:}: 		1.0
 */
@Data
@Accessors(chain=true)
@TableName("sys_menu")
@Schema(description = "菜单")
@EqualsAndHashCode(callSuper = true)
public class Menu extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;


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