package org.wlpiaoyi.server.demo.sys.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

import org.wlpiaoyi.server.demo.common.datasource.domain.entity.CommonEntity;



/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	角色 实体类
 * {@code @date:} 			2024-10-11 17:17:25
 * {@code @version:}: 		1.0
 */
@Data
@Accessors(chain=true)
@TableName("sys_role")
@Schema(description = "角色")
@EqualsAndHashCode(callSuper = true)
public class Role extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /** 角色名称 **/
    @Schema(name = "name" , description = "角色名称")
    private String name;

    /** 角色编码=(amdin:管理员,...) **/
    @Schema(name = "code" , description = "角色编码 enums(amdin:管理员,...)" , examples = {"amdin:管理员","..."})
    private String code;


}
