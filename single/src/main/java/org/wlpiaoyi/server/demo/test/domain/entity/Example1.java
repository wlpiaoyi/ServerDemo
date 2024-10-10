package org.wlpiaoyi.server.demo.test.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.wlpiaoyi.server.demo.domain.entity.CommonEntity;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	测试用的表格 实体类
 * {@code @date:} 			2024-09-15 17:30:33
 * {@code @version:}: 		1.0
 */
@Data
@Accessors(chain=true)
@TableName("test_example_1")
@Schema(description = "测试用的表格")
@EqualsAndHashCode(callSuper = true)
public class Example1 extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /** stringVar **/
    @Schema(description = "stringVar")
    private String stringVar;

    /** intVar **/
    @Schema(description = "intVar")
    private Integer intVar;

    /** dateVar **/
    @Schema(description = "dateVar")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateVar;

}
