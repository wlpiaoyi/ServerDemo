package org.wlpiaoyi.server.demo.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.wlpiaoyi.server.demo.sys.domain.entity.Role;
import org.wlpiaoyi.server.demo.sys.domain.ro.RoleRo;
import org.wlpiaoyi.server.demo.sys.service.IRoleService;
import org.wlpiaoyi.server.demo.sys.domain.vo.RoleVo;
import org.wlpiaoyi.server.demo.common.datasource.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.common.core.response.R;
import org.wlpiaoyi.server.demo.common.datasource.tools.Condition;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.framework.utils.ValueUtils;

import jakarta.validation.Valid;
import org.wlpiaoyi.server.demo.common.core.web.annotation.PreAuthorize;


/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	角色 控制器
 * {@code @date:} 			2024-10-11 17:34:54
 * {@code @version:}: 		1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys/role")
@Tag(name = "角色接口")
public class RoleController {

	private final IRoleService roleService;

	/**
	 * 角色 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@Operation(summary = "角色 详情")
	@PreAuthorize("role_detail")
	public R<RoleVo> detail(@RequestParam Long id) {
		return R.success(this.roleService.getDetail(id));
	}

	/**
	 * 角色 分页
	 */
	@PostMapping("/list")
	@ApiOperationSupport(order = 3)
	@Operation(summary = "角色 分页")
	@PreAuthorize("role_list")
	public R<IPage<RoleVo>> list(@RequestBody RoleRo.RoleQuery body) {
		QueryWrapper<Role> wrapper = Condition.getQueryWrapper(ModelWrapper.parseOne(body, Role.class));
		wrapper.orderByDesc("create_time");
		IPage<Role> pages = roleService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, RoleVo.class));
	}

	/**
	 * 角色 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@Operation(summary = "角色 新增")
	@PreAuthorize("role_add")
	public R<Boolean> save(@Valid @RequestBody RoleRo.RoleSubmit body) {
		return R.success(roleService.save(ModelWrapper.parseOne(body, Role.class)));
	}

	/**
	 * 角色 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@Operation(summary = "角色 修改")
	@PreAuthorize("role_update")
	public R<Boolean> update(@RequestBody RoleRo.RoleSubmit body) {
		return R.success(roleService.updateById(ModelWrapper.parseOne(body, Role.class)));
	}

	/**
	 * 角色 删除
	 */
	@GetMapping("/remove")
	@ApiOperationSupport(order = 7)
	@Operation(summary = "角色 逻辑删除")
	@PreAuthorize("role_remove")
	public R remove(@Parameter(description = "主键集合", required = true) @RequestParam String ids) {
		return R.success(roleService.deleteLogic(ValueUtils.toLongList(ids)));
	}

}
