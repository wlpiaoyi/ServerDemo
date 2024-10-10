package org.wlpiaoyi.server.demo.sys.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import org.wlpiaoyi.server.demo.domain.entity.BaseEntity;
import org.wlpiaoyi.server.demo.domain.entity.CommonEntity;



/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	角色 实体类
 * {@code @date:} 			2024-10-10 23:41:54
 * {@code @version:}: 		1.0
 */
@Data
@Accessors(chain=true)
@TableName("sys_role")
@Schema(description = "角色")
@EqualsAndHashCode(callSuper = true)
public class Role extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /** name **/
    @Schema(description = "name")
    private String name;

    /** 角色编码=(amdin:管理员) **/
    @Schema(description = "角色编码=(amdin:管理员)")
    private String code;

}