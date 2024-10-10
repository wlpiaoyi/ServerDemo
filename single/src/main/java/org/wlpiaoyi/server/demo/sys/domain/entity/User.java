package org.wlpiaoyi.server.demo.sys.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	用户表 实体类
 * {@code @date:} 			2024-10-10 23:41:53
 * {@code @version:}: 		1.0
 */
@Data
@Accessors(chain=true)
@TableName("sys_user")
@Schema(description = "用户表")
@EqualsAndHashCode(callSuper = true)
public class User extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /** account **/
    @Schema(description = "account")
    @NotBlank(message = "account不能为空")
    private String account;

    /** password **/
    @Schema(description = "password")
    @NotBlank(message = "password不能为空")
    private String password;

    /** deptId **/
    @Schema(description = "deptId")
    @NotNull(message = "deptId不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

}