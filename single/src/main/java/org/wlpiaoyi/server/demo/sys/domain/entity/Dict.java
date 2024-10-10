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
 * {@code @description:} 	数据字典 实体类
 * {@code @date:} 			2024-10-10 23:41:53
 * {@code @version:}: 		1.0
 */
@Data
@Accessors(chain=true)
@TableName("sys_dict")
@Schema(description = "数据字典")
@EqualsAndHashCode(callSuper = true)
public class Dict extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /** parentId **/
    @Schema(description = "parentId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    /** name **/
    @Schema(description = "name")
    @NotBlank(message = "name不能为空")
    private String name;

    /** code **/
    @Schema(description = "code")
    @NotBlank(message = "code不能为空")
    private String code;

    /** value **/
    @Schema(description = "value")
    @NotBlank(message = "value不能为空")
    private String value;

    /** isPublic **/
    @Schema(description = "isPublic")
    @NotNull(message = "isPublic不能为空")
    private Byte isPublic;

    /** sort **/
    @Schema(description = "sort")
    @NotNull(message = "sort不能为空")
    private Integer sort;

    /** deep **/
    @Schema(description = "deep")
    @NotNull(message = "deep不能为空")
    private Integer deep;

    /** isLeaf **/
    @Schema(description = "isLeaf")
    @NotNull(message = "isLeaf不能为空")
    private Byte isLeaf;

}