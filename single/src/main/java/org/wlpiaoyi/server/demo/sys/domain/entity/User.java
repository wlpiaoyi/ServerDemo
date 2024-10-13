package org.wlpiaoyi.server.demo.sys.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import org.wlpiaoyi.server.demo.domain.entity.BaseEntity;
import org.wlpiaoyi.server.demo.domain.entity.CommonEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;



/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	用户表 实体类
 * {@code @date:} 			2024-10-11 17:17:25
 * {@code @version:}: 		1.0
 */
@Data
@Accessors(chain=true)
@TableName("sys_user")
@Schema(description = "用户表")
@EqualsAndHashCode(callSuper = true)
public class User extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /** 账号 **/
    @Schema(name = "account" , description = "账号")
    @NotBlank(message = "账号不能为空")
    private String account;

    /** 密码 **/
    @JsonIgnore
    @Schema(name = "password" , description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    /** 部门ID **/
    @Schema(name = "deptId" , description = "部门ID")
    @NotNull(message = "部门ID不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

}
