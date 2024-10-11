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
import org.wlpiaoyi.server.demo.sys.domain.ro.DictRo;
import org.wlpiaoyi.server.demo.utils.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.request.Condition;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.framework.utils.ValueUtils;

import jakarta.validation.Valid;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	数据字典 控制器
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dict")
@Tag(name = "数据字典接口")
public class DictController {

	private final IDictService dictService;

	/**
	 * 数据字典 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@Operation(summary = "数据字典 详情")
	public R<DictVo> detail(DictRo.DictQuery body) {
		DictVo dict = ModelWrapper.parseOne(
				this.dictService.getOne(
						Condition.getQueryWrapper(ModelWrapper.parseOne(body, Dict.class))
				),
				DictVo.class
		);
		return R.success(dict);

	}

	/**
	 * 数据字典 分页
	 */
	@PostMapping("/page")
	@ApiOperationSupport(order = 2)
	@Operation(summary = "数据字典 分页")
	public R<IPage<DictVo>> page(@RequestBody DictRo.DictQuery body){
		LambdaQueryWrapper<Dict> wrapper = Wrappers.<Dict>lambdaQuery();
		wrapper.orderByDesc(Dict::getCreateTime);
		IPage<Dict> pages = dictService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, DictVo.class));
	}

	/**
	 * 数据字典 分页
	 */
	@PostMapping("/list")
	@ApiOperationSupport(order = 3)
	@Operation(summary = "数据字典 分页")
	public R<IPage<DictVo>> list(@RequestBody DictRo.DictQuery body) {
		QueryWrapper<Dict> wrapper = Condition.getQueryWrapper(ModelWrapper.parseOne(body, Dict.class));
		wrapper.orderByDesc("create_time");
		IPage<Dict> pages = dictService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, DictVo.class));
	}

	/**
	 * 数据字典 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@Operation(summary = "数据字典 新增")
	public R<Boolean> save(@Valid @RequestBody DictRo.DictSubmit body) {
		return R.success(dictService.save(ModelWrapper.parseOne(body, Dict.class)));
	}

	/**
	 * 数据字典 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@Operation(summary = "数据字典 修改")
	public R<Boolean> update(@RequestBody DictRo.DictSubmit body) {
		return R.success(dictService.updateById(ModelWrapper.parseOne(body, Dict.class)));
	}

	/**
	 * 数据字典 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@Operation(summary = "数据字典 新增或修改")
	public R<Boolean> submit(@Valid @RequestBody DictRo.DictSubmit body) {
		return R.success(dictService.saveOrUpdate(ModelWrapper.parseOne(body, Dict.class)));
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
