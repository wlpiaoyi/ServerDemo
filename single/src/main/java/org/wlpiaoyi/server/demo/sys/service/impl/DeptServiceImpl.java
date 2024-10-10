package org.wlpiaoyi.server.demo.sys.service.impl;

import org.wlpiaoyi.server.demo.sys.service.IDeptService;
import org.wlpiaoyi.server.demo.sys.domain.entity.Dept;
import org.wlpiaoyi.server.demo.sys.domain.mapper.DeptMapper;
import org.wlpiaoyi.server.demo.sys.domain.vo.DeptVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.DeptRo;
import org.wlpiaoyi.server.demo.service.impl.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	部门 服务类实现
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Primary
@Service
public class DeptServiceImpl extends BaseServiceImpl<DeptMapper, Dept> implements IDeptService {


}