package org.wlpiaoyi.server.demo.sys.service;

import org.wlpiaoyi.server.demo.sys.domain.entity.Access;
import org.wlpiaoyi.server.demo.sys.domain.vo.AccessVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.AccessRo;
import org.wlpiaoyi.server.demo.service.IBaseService;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	数据权限 服务类接口
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
public interface IAccessService extends IBaseService<Access> {

    void mergeAll();

}
