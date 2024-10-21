package org.wlpiaoyi.server.demo.sys.controller;



import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.wlpiaoyi.server.demo.sys.domain.ro.AccessRo;
import org.wlpiaoyi.server.demo.sys.service.IAccessService;
import org.wlpiaoyi.server.demo.sys.domain.vo.AccessVo;
import org.wlpiaoyi.server.demo.common.datasource.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.common.core.response.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	数据权限 控制器
 * {@code @date:} 			2024-10-11 18:08:10
 * {@code @version:}: 		1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys/access")
@Tag(name = "数据权限接口")
public class AccessController {

	private final IAccessService accessService;

	/**
	 * 修改角色数据权限
	 */
	@PostMapping("/updateRoleAccess")
	@ApiOperationSupport(order = 1)
	@Operation(summary = "修改角色数据权限")
	public R<Boolean> updateRoleAccess(@RequestBody AccessRo.AccessSubmit body){
		this.accessService.mergeAccesses(body.getRoleId(), body.getAddAccessIds(), body.getAddAccessIds());
		return R.success(true);
	}

	/**
	 * 数据权限 详情
	 */
	@GetMapping("/listByRoleId")
	@ApiOperationSupport(order = 1)
	@Operation(summary = "数据权限 详情")
	public R<List<AccessVo>> listByRoleId(@RequestParam Long roleId) {
		return R.success(ModelWrapper.parseForList(this.accessService.listForRoleId(roleId), AccessVo.class));
	}

}
