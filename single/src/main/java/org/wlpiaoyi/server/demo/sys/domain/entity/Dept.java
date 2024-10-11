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



/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	部门 实体类
 * {@code @date:} 			2024-10-11 17:17:25
 * {@code @version:}: 		1.0
 */
@Data
@Accessors(chain=true)
@TableName("sys_dept")
@Schema(description = "部门")
@EqualsAndHashCode(callSuper = true)
public class Dept extends CommonEntity implements Serializable {

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