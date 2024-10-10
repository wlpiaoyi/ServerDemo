package org.wlpiaoyi.server.demo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import org.wlpiaoyi.server.demo.utils.web.ConfigModel;

import java.io.Serial;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/11/21 17:05
 * {@code @version:}:       1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final String ZONE = ConfigModel.ZONE;

    @JsonIgnore
    private static final MessageDigest MESSAGE_DIGEST;

    static {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    @JsonSerialize(
            using = ToStringSerializer.class
    )
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "创建人")
    private Long createUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = BaseEntity.ZONE)
    @Schema(description = "创建时间")
    private Date createTime;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "更新人")
    private Long updateUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = BaseEntity.ZONE)
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
        this.setId(null);
        this.setCreateTime(null);
        this.setCreateUser(null);
        this.setUpdateTime(null);
        this.setUpdateUser(null);
    }


}
