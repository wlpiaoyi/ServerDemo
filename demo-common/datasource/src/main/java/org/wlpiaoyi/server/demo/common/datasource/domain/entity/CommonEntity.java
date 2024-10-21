package org.wlpiaoyi.server.demo.common.datasource.domain.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p><b>{@code @author:}</b> wlpia</p>
 * <p><b>{@code @description:}</b> </p>
 * <p><b>{@code @date:}</b> 2024-10-10 22:33:13</p>
 * <p><b>{@code @version:}:</b> 1.0</p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonEntity extends BaseEntity{

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "创建人")
    private Long createUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = ZONE)
    @Schema(description = "创建时间")
    private Date createTime;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "更新人")
    private Long updateUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = ZONE)
    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "业务状态")
    private int status = 1;

    @TableLogic(value = "0", delval = "1")
    @Schema(description = "是否已删除")
    private int isDeleted = 0;

    /**
     * <p><b>{@code @description:}</b>
     * 清理关键数据
     * </p>
     *
     * <p><b>@param</b> <b></b>
     * {@link }
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/1/11 16:373</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public void cleanKeyData(){
        super.cleanKeyData();
        this.setCreateTime(null);
        this.setCreateUser(null);
        this.setUpdateTime(null);
        this.setUpdateUser(null);
    }
}
