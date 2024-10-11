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
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	平台 实体类
 * {@code @date:} 			2024-10-11 17:17:25
 * {@code @version:}: 		1.0
 */
@Data
@Accessors(chain=true)
@TableName("sys_platform")
@Schema(description = "平台")
@EqualsAndHashCode(callSuper = true)
public class Platform extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /** 平台名称 **/
    @Schema(name = "name" , description = "平台名称")
    @NotBlank(message = "平台名称不能为空")
    private String name;

    /** 平台编码 **/
    @Schema(name = "code" , description = "平台编码")
    @NotBlank(message = "平台编码不能为空")
    private String code;

}