package org.wlpiaoyi.server.demo.test.service.impl;

import org.wlpiaoyi.server.demo.test.service.IExample1Service;
import org.wlpiaoyi.server.demo.test.domain.entity.Example1;
import org.wlpiaoyi.server.demo.test.domain.mapper.Example1Mapper;
import org.wlpiaoyi.server.demo.test.domain.vo.Example1Vo;
import org.wlpiaoyi.server.demo.test.domain.ro.Example1Ro;
import org.wlpiaoyi.server.demo.service.impl.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	测试用的表格 服务类实现
 * {@code @date:} 			2024-09-15 17:30:33
 * {@code @version:}: 		1.0
 */
@Primary
@Service
public class Example1ServiceImpl extends BaseServiceImpl<Example1Mapper, Example1> implements IExample1Service {


}