package org.wlpiaoyi.server.demo.test.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import org.wlpiaoyi.server.demo.test.domain.entity.Example1;

/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	测试用的表格 视图实体类
 * {@code @date:} 			2024-09-15 17:30:33
 * {@code @version:}: 		1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Example1Vo extends Example1 implements Serializable {
	private static final long serialVersionUID = 1L;

}