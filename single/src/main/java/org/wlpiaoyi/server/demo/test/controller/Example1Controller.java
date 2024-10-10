package org.wlpiaoyi.server.demo.test.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.wlpiaoyi.server.demo.test.domain.entity.Example1;
import org.wlpiaoyi.server.demo.test.service.IExample1Service;
import org.wlpiaoyi.server.demo.test.domain.vo.Example1Vo;
import org.wlpiaoyi.server.demo.test.domain.ro.Example1Ro;
import org.wlpiaoyi.server.demo.utils.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.request.Condition;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.framework.utils.ValueUtils;

import jakarta.validation.Valid;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	测试用的表格 控制器
 * {@code @date:} 			2024-09-15 17:30:33
 * {@code @version:}: 		1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/example_1")
@Tag(name = "测试用的表格接口")
public class Example1Controller {

	private final IExample1Service example1Service;

	/**
	 * 测试用的表格 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@Operation(summary = "测试用的表格 详情")
	public R<Example1Vo> detail(Example1Ro.Query body) {
		Example1Vo example1 = ModelWrapper.parseOne(
				this.example1Service.getOne(
						Condition.getQueryWrapper(ModelWrapper.parseOne(body, Example1.class))
				),
				Example1Vo.class
		);
		return R.success(example1);

	}

	/**
	 * 测试用的表格 分页
	 */
	@PostMapping("/page")
	@ApiOperationSupport(order = 2)
	@Operation(summary = "测试用的表格 分页")
	public R<IPage<Example1Vo>> page(@RequestBody Example1Ro.Query body){
		LambdaQueryWrapper<Example1> wrapper = Wrappers.<Example1>lambdaQuery();
		wrapper.orderByDesc(Example1::getCreateTime);
		IPage<Example1> pages = example1Service.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, Example1Vo.class));
	}

	/**
	 * 测试用的表格 分页
	 */
	@PostMapping("/list")
	@ApiOperationSupport(order = 3)
	@Operation(summary = "测试用的表格 分页")
	public R<IPage<Example1Vo>> list(@RequestBody Example1Ro.Query body) {
		QueryWrapper<Example1> wrapper = Condition.getQueryWrapper(ModelWrapper.parseOne(body, Example1.class));
		wrapper.orderByDesc("create_time");
		IPage<Example1> pages = example1Service.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, Example1Vo.class));
	}

	/**
	 * 测试用的表格 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@Operation(summary = "测试用的表格 新增")
	public R<Boolean> save(@Valid @RequestBody Example1Ro.Submit body) {
		return R.success(example1Service.save(ModelWrapper.parseOne(body, Example1.class)));
	}

	/**
	 * 测试用的表格 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@Operation(summary = "测试用的表格 修改")
	public R<Boolean> update(@RequestBody Example1Ro.Submit body) {
		return R.success(example1Service.updateById(ModelWrapper.parseOne(body, Example1.class)));
	}

	/**
	 * 测试用的表格 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@Operation(summary = "测试用的表格 新增或修改")
	public R<Boolean> submit(@Valid @RequestBody Example1Ro.Submit body) {
		return R.success(example1Service.saveOrUpdate(ModelWrapper.parseOne(body, Example1.class)));
	}

	/**
	 * 测试用的表格 删除
	 */
	@GetMapping("/remove")
	@ApiOperationSupport(order = 7)
	@Operation(summary = "测试用的表格 逻辑删除")
	public R remove(@Parameter(description = "主键集合", required = true) @RequestParam String ids) {
		return R.success(example1Service.deleteLogic(ValueUtils.toLongList(ids)));
	}

}
