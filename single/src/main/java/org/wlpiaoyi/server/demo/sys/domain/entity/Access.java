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
 * {@code @description:} 	数据权限 实体类
 * {@code @date:} 			2024-10-10 23:41:53
 * {@code @version:}: 		1.0
 */
@Data
@Accessors(chain=true)
@TableName("sys_access")
@Schema(description = "数据权限")
@EqualsAndHashCode(callSuper = true)
public class Access extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /** value **/
    @Schema(description = "value")
    @NotBlank(message = "value不能为空")
    private String value;

    /** path **/
    @Schema(description = "path")
    @NotBlank(message = "path不能为空")
    private String path;

}
