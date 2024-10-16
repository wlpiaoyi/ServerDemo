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
import org.wlpiaoyi.server.demo.sys.domain.entity.Dict;
import org.wlpiaoyi.server.demo.sys.service.IDictService;
import org.wlpiaoyi.server.demo.sys.domain.vo.DictVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.DictRo.*;
import org.wlpiaoyi.server.demo.utils.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.request.Condition;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.framework.utils.ValueUtils;

import jakarta.validation.Valid;


/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	数据字典 控制器
 * {@code @date:} 			2024-10-11 17:34:54
 * {@code @version:}: 		1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dict")
@Tag(name = "数据字典接口")
public class DictController {

	private final IDictService dictService;

	/**
	 * 数据字典 分页
	 */
	@PostMapping("/page")
	@ApiOperationSupport(order = 2)
	@Operation(summary = "数据字典 分页")
	public R<IPage<DictVo>> page(@RequestBody DictQuery body){
		LambdaQueryWrapper<Dict> wrapper = Wrappers.<Dict>lambdaQuery();
		if(ValueUtils.isNotBlank(body.getParentId())){
			wrapper.eq(Dict::getParentId, body.getParentId());
		}
		if(ValueUtils.isNotBlank(body.getName())){
			wrapper.like(Dict::getName, body.getName());
		}
		if(ValueUtils.isNotBlank(body.getCode())){
			wrapper.like(Dict::getCode, body.getCode());
		}
		if(ValueUtils.isNotBlank(body.getIsPublic()) && body.getIsPublic().intValue() == 1){

		}
		wrapper.orderByDesc(Dict::getSort);
		wrapper.orderByDesc(Dict::getCreateTime);
		IPage<Dict> pages = dictService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, DictVo.class));
	}

	/**
	 * 数据字典 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@Operation(summary = "数据字典 新增")
	public R<Boolean> save(@Valid @RequestBody DictSubmit body) {
		return R.success(dictService.save(ModelWrapper.parseOne(body, Dict.class)));
	}

	/**
	 * 数据字典 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@Operation(summary = "数据字典 修改")
	public R<Boolean> update(@RequestBody DictSubmit body) {
		return R.success(dictService.updateById(ModelWrapper.parseOne(body, Dict.class)));
	}

	/**
	 * 数据字典 删除
	 */
	@GetMapping("/remove")
	@ApiOperationSupport(order = 7)
	@Operation(summary = "数据字典 逻辑删除")
	public R remove(@Parameter(description = "主键集合", required = true) @RequestParam String ids) {
		return R.success(dictService.deleteLogic(ValueUtils.toLongList(ids)));
	}

}
