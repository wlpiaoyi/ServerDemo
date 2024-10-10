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
import org.wlpiaoyi.server.demo.sys.domain.entity.Access;
import org.wlpiaoyi.server.demo.sys.service.IAccessService;
import org.wlpiaoyi.server.demo.sys.domain.vo.AccessVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.AccessRo;
import org.wlpiaoyi.server.demo.utils.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.request.Condition;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.framework.utils.ValueUtils;

import jakarta.validation.Valid;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	数据权限 控制器
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/access")
@Tag(name = "数据权限接口")
public class AccessController {

	private final IAccessService accessService;

	/**
	 * 数据权限 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@Operation(summary = "数据权限 详情")
	public R<AccessVo> detail(AccessRo.Query body) {
		AccessVo access = ModelWrapper.parseOne(
				this.accessService.getOne(
						Condition.getQueryWrapper(ModelWrapper.parseOne(body, Access.class))
				),
				AccessVo.class
		);
		return R.success(access);

	}

	/**
	 * 数据权限 分页
	 */
	@PostMapping("/page")
	@ApiOperationSupport(order = 2)
	@Operation(summary = "数据权限 分页")
	public R<IPage<AccessVo>> page(@RequestBody AccessRo.Query body){
		LambdaQueryWrapper<Access> wrapper = Wrappers.<Access>lambdaQuery();
		IPage<Access> pages = accessService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, AccessVo.class));
	}

	/**
	 * 数据权限 分页
	 */
	@PostMapping("/list")
	@ApiOperationSupport(order = 3)
	@Operation(summary = "数据权限 分页")
	public R<IPage<AccessVo>> list(@RequestBody AccessRo.Query body) {
		QueryWrapper<Access> wrapper = Condition.getQueryWrapper(ModelWrapper.parseOne(body, Access.class));
		IPage<Access> pages = accessService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, AccessVo.class));
	}

	/**
	 * 数据权限 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@Operation(summary = "数据权限 新增")
	public R<Boolean> save(@Valid @RequestBody AccessRo.Submit body) {
		return R.success(accessService.save(ModelWrapper.parseOne(body, Access.class)));
	}

	/**
	 * 数据权限 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@Operation(summary = "数据权限 修改")
	public R<Boolean> update(@RequestBody AccessRo.Submit body) {
		return R.success(accessService.updateById(ModelWrapper.parseOne(body, Access.class)));
	}

	/**
	 * 数据权限 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@Operation(summary = "数据权限 新增或修改")
	public R<Boolean> submit(@Valid @RequestBody AccessRo.Submit body) {
		return R.success(accessService.saveOrUpdate(ModelWrapper.parseOne(body, Access.class)));
	}

	/**
	 * 数据权限 删除
	 */
	@GetMapping("/remove")
	@ApiOperationSupport(order = 7)
	@Operation(summary = "数据权限 逻辑删除")
	public R remove(@Parameter(description = "主键集合", required = true) @RequestParam String ids) {
		return R.success(accessService.deleteLogic(ValueUtils.toLongList(ids)));
	}

}
