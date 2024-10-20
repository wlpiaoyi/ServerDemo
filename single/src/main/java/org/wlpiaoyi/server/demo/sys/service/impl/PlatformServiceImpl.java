package org.wlpiaoyi.server.demo.sys.service.impl;

import org.wlpiaoyi.server.demo.sys.service.IPlatformService;
import org.wlpiaoyi.server.demo.sys.domain.entity.Platform;
import org.wlpiaoyi.server.demo.sys.domain.mapper.PlatformMapper;
import org.wlpiaoyi.server.demo.common.datasource.service.impl.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	平台 服务类实现
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@Primary
@Service
public class PlatformServiceImpl extends BaseServiceImpl<PlatformMapper, Platform> implements IPlatformService {


}
