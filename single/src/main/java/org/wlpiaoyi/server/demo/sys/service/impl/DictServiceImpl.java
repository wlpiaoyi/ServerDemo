package org.wlpiaoyi.server.demo.sys.service.impl;

import org.wlpiaoyi.server.demo.sys.service.IDictService;
import org.wlpiaoyi.server.demo.sys.domain.entity.Dict;
import org.wlpiaoyi.server.demo.sys.domain.mapper.DictMapper;
import org.wlpiaoyi.server.demo.sys.domain.vo.DictVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.DictRo;
import org.wlpiaoyi.server.demo.service.impl.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	数据字典 服务类实现
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Primary
@Service
public class DictServiceImpl extends BaseServiceImpl<DictMapper, Dict> implements IDictService {


}