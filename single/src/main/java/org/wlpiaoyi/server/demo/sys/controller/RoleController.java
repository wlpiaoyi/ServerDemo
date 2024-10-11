package org.wlpiaoyi.server.demo.sys.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.wlpiaoyi.server.demo.sys.domain.entity.Role;
import org.wlpiaoyi.server.demo.sys.service.IRoleService;
import org.wlpiaoyi.server.demo.sys.domain.vo.RoleVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.RoleRo;
import org.wlpiaoyi.server.demo.utils.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.request.Condition;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.framework.utils.ValueUtils;

import jakarta.validation.Valid;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	角色 控制器
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/role")
@Tag(name = "角色接口")
public class RoleController {

	private final IRoleService roleService;

	/**
	 * 角色 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@Operation(summary = "角色 详情")
	public R<RoleVo> detail(RoleRo.RoleQuery body) {
		RoleVo role = ModelWrapper.parseOne(
				this.roleService.getOne(
						Condition.getQueryWrapper(ModelWrapper.parseOne(body, Role.class))
				),
				RoleVo.class
		);
		return R.success(role);

	}

	/**
	 * 角色 分页
	 */
	@PostMapping("/page")
	@ApiOperationSupport(order = 2)
	@Operation(summary = "角色 分页")
	public R<IPage<RoleVo>> page(@RequestBody RoleRo.RoleQuery body){
		LambdaQueryWrapper<Role> wrapper = Wrappers.<Role>lambdaQuery();
		wrapper.orderByDesc(Role::getCreateTime);
		IPage<Role> pages = roleService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, RoleVo.class));
	}

	/**
	 * 角色 分页
	 */
	@PostMapping("/list")
	@ApiOperationSupport(order = 3)
	@Operation(summary = "角色 分页")
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
	public R<Boolean> save(@Valid @RequestBody RoleRo.RoleSubmit body) {
		return R.success(roleService.save(ModelWrapper.parseOne(body, Role.class)));
	}

	/**
	 * 角色 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@Operation(summary = "角色 修改")
	public R<Boolean> update(@RequestBody RoleRo.RoleSubmit body) {
		return R.success(roleService.updateById(ModelWrapper.parseOne(body, Role.class)));
	}

	/**
	 * 角色 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@Operation(summary = "角色 新增或修改")
	public R<Boolean> submit(@Valid @RequestBody RoleRo.RoleSubmit body) {
		return R.success(roleService.saveOrUpdate(ModelWrapper.parseOne(body, Role.class)));
	}

	/**
	 * 角色 删除
	 */
	@GetMapping("/remove")
	@ApiOperationSupport(order = 7)
	@Operation(summary = "角色 逻辑删除")
	public R remove(@Parameter(description = "主键集合", required = true) @RequestParam String ids) {
		return R.success(roleService.deleteLogic(ValueUtils.toLongList(ids)));
	}

}
