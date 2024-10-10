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



/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	平台 实体类
 * {@code @date:} 			2024-10-10 23:41:53
 * {@code @version:}: 		1.0
 */
@Data
@Accessors(chain=true)
@TableName("sys_platform")
@Schema(description = "平台")
@EqualsAndHashCode(callSuper = true)
public class Platform extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /** 名称 **/
    @Schema(description = "名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    /** 编码 **/
    @Schema(description = "编码")
    @NotBlank(message = "编码不能为空")
    private String code;

}