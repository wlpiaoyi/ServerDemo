package org.wlpiaoyi.server.demo.common.tools.web.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.io.Serializable;

/**
 * <p<b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b>
 * 查询条件
 * </p>
 * <p><b>{@code @date:}</b>2023/9/16 12:39</p>
 * <p><b>{@code @version:}</b>1.0</p>
 */
@Getter
@Schema(description = "查询条件")
public class Query implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "当前页")
    private Integer pageNum = 0;

    @Schema(description = "每页的数量")
    private Integer pageSize = 10;

    @Schema(description = "增序排序字段")
    private String ascs;

    @Schema(description = "降序排序字段")
    private String descs;

    public Query setPageNum(final Integer pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public Query setPageSize(final Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }
    public Query setAscs(final String ascs) {
        this.ascs = ascs;
        return this;
    }

    public Query setDescs(final String descs) {
        this.descs = descs;
        return this;
    }

}