package org.wlpiaoyi.server.demo.test.domain.ro;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	测试用的表格 请求包装类
 * {@code @date:} 			2024-09-15 17:30:33
 * {@code @version:}: 		1.0
 */
public class Example1Ro {
    @Data
    @Schema(description = "测试用的表格 请求实例")
	public static class Query extends org.wlpiaoyi.server.demo.common.tools.web.model.Query implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
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

    @Data
    @Schema(description = "测试用的表格 请求实例")
    public static class Submit implements Serializable {

        private static final long serialVersionUID = 1L;

		@JsonSerialize(using = ToStringSerializer.class)
		@Schema(description = "主键id")
		@TableId(value = "id", type = IdType.ASSIGN_ID)
		private Long id;
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
}
