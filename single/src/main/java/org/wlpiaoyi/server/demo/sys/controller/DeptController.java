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
import org.wlpiaoyi.server.demo.sys.domain.entity.Dict;
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
	 * 部门 分页
	 */
	@PostMapping("/page")
	@ApiOperationSupport(order = 2)
	@Operation(summary = "部门 分页")
	public R<IPage<DeptVo>> page(@RequestBody DeptQuery body){
		LambdaQueryWrapper<Dept> wrapper = Wrappers.<Dept>lambdaQuery();
		if(ValueUtils.isNotBlank(body.getParentId())){
			wrapper.eq(Dept::getParentId, body.getParentId());
		}
		if(ValueUtils.isNotBlank(body.getName())){
			wrapper.like(Dept::getName, body.getName());
		}
		if(ValueUtils.isNotBlank(body.getCode())){
			wrapper.like(Dept::getCode, body.getCode());
		}
		wrapper.orderByDesc(Dept::getCreateTime);
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
	 * 部门 删除
	 */
	@GetMapping("/remove")
	@ApiOperationSupport(order = 7)
	@Operation(summary = "部门 逻辑删除")
	public R remove(@Parameter(description = "主键集合", required = true) @RequestParam String ids) {
		return R.success(deptService.deleteLogic(ValueUtils.toLongList(ids)));
	}

}
