package org.wlpiaoyi.server.demo.common.datasource.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wlpiaoyi.server.demo.common.tools.web.model.ConfigModel;

import java.io.Serial;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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


    /**
     * <p><b>{@code @description:}</b>
     * 清理关键数据
     * </p>
     *
     * <p><b>{@code @param}</b> <b></b>
     * {@link }
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/1/11 16:373</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public void cleanKeyData(){
        this.setId(null);
    }


}
