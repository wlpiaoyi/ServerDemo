package org.wlpiaoyi.server.demo.sys.service;

import org.wlpiaoyi.server.demo.sys.domain.entity.User;
import org.wlpiaoyi.server.demo.sys.domain.vo.UserVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.UserRo;
import org.wlpiaoyi.server.demo.service.IBaseService;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	用户表 服务类接口
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
public interface IUserService extends IBaseService<User> {


    UserVo login(UserRo.UserAuth auth);

}
