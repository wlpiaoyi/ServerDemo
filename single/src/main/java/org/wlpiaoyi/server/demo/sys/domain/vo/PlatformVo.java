package org.wlpiaoyi.server.demo.sys.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import org.wlpiaoyi.server.demo.sys.domain.entity.Platform;

/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	平台 视图实体类
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlatformVo extends Platform implements Serializable {
	private static final long serialVersionUID = 1L;

}