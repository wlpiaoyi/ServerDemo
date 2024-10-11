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
import org.wlpiaoyi.server.demo.sys.domain.entity.Dept;
import org.wlpiaoyi.server.demo.sys.service.IDeptService;
import org.wlpiaoyi.server.demo.sys.domain.vo.DeptVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.DeptRo.*;
import org.wlpiaoyi.server.demo.utils.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.request.Condition;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.framework.utils.ValueUtils;

import jakarta.validation.Valid;


/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	部门 控制器
 * {@code @date:} 			2024-10-11 17:34:54
 * {@code @version:}: 		1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dept")
@Tag(name = "部门接口")
public class DeptController {

	private final IDeptService deptService;

	/**
	 * 部门 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@Operation(summary = "部门 详情")
	public R<DeptVo> detail(DeptQuery body) {
		DeptVo dept = ModelWrapper.parseOne(
				this.deptService.getOne(
						Condition.getQueryWrapper(ModelWrapper.parseOne(body, Dept.class))
				),
				DeptVo.class
		);
		return R.success(dept);

	}

	/**
	 * 部门 分页
	 */
	@PostMapping("/page")
	@ApiOperationSupport(order = 2)
	@Operation(summary = "部门 分页")
	public R<IPage<DeptVo>> page(@RequestBody DeptQuery body){
		LambdaQueryWrapper<Dept> wrapper = Wrappers.<Dept>lambdaQuery();
		wrapper.orderByDesc(Dept::getCreateTime);
		IPage<Dept> pages = deptService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, DeptVo.class));
	}

	/**
	 * 部门 分页
	 */
	@PostMapping("/list")
	@ApiOperationSupport(order = 3)
	@Operation(summary = "部门 分页")
	public R<IPage<DeptVo>> list(@RequestBody DeptQuery body) {
		QueryWrapper<Dept> wrapper = Condition.getQueryWrapper(ModelWrapper.parseOne(body, Dept.class));
		wrapper.orderByDesc("create_time");
		IPage<Dept> pages = deptService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, DeptVo.class));
	}

	/**
	 * 部门 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@Operation(summary = "部门 新增")
	public R<Boolean> save(@Valid @RequestBody DeptSubmit body) {
		return R.success(deptService.save(ModelWrapper.parseOne(body, Dept.class)));
	}

	/**
	 * 部门 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@Operation(summary = "部门 修改")
	public R<Boolean> update(@RequestBody DeptSubmit body) {
		return R.success(deptService.updateById(ModelWrapper.parseOne(body, Dept.class)));
	}

	/**
	 * 部门 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@Operation(summary = "部门 新增或修改")
	public R<Boolean> submit(@Valid @RequestBody DeptSubmit body) {
		return R.success(deptService.saveOrUpdate(ModelWrapper.parseOne(body, Dept.class)));
	}

	/**
	 * 部门 删除
	 */
	@GetMapping("/remove")
	@ApiOperationSupport(order = 7)
	@Operation(summary = "部门 逻辑删除")
	public R remove(@Parameter(description = "主键集合", required = true) @RequestParam String ids) {
		return R.success(deptService.deleteLogic(ValueUtils.toLongList(ids)));
	}

}