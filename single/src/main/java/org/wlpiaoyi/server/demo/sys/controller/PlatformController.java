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
import org.wlpiaoyi.server.demo.sys.domain.entity.Platform;
import org.wlpiaoyi.server.demo.sys.service.IPlatformService;
import org.wlpiaoyi.server.demo.sys.domain.vo.PlatformVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.PlatformRo;
import org.wlpiaoyi.server.demo.utils.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.request.Condition;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.framework.utils.ValueUtils;

import jakarta.validation.Valid;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	平台 控制器
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/platform")
@Tag(name = "平台接口")
public class PlatformController {

	private final IPlatformService platformService;

	/**
	 * 平台 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@Operation(summary = "平台 详情")
	public R<PlatformVo> detail(PlatformRo.PlatformQuery body) {
		PlatformVo platform = ModelWrapper.parseOne(
				this.platformService.getOne(
						Condition.getQueryWrapper(ModelWrapper.parseOne(body, Platform.class))
				),
				PlatformVo.class
		);
		return R.success(platform);

	}

	/**
	 * 平台 分页
	 */
	@PostMapping("/page")
	@ApiOperationSupport(order = 2)
	@Operation(summary = "平台 分页")
	public R<IPage<PlatformVo>> page(@RequestBody PlatformRo.PlatformQuery body){
		LambdaQueryWrapper<Platform> wrapper = Wrappers.<Platform>lambdaQuery();
		wrapper.orderByDesc(Platform::getCreateTime);
		IPage<Platform> pages = platformService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, PlatformVo.class));
	}

	/**
	 * 平台 分页
	 */
	@PostMapping("/list")
	@ApiOperationSupport(order = 3)
	@Operation(summary = "平台 分页")
	public R<IPage<PlatformVo>> list(@RequestBody PlatformRo.PlatformQuery body) {
		QueryWrapper<Platform> wrapper = Condition.getQueryWrapper(ModelWrapper.parseOne(body, Platform.class));
		wrapper.orderByDesc("create_time");
		IPage<Platform> pages = platformService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, PlatformVo.class));
	}

	/**
	 * 平台 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@Operation(summary = "平台 新增")
	public R<Boolean> save(@Valid @RequestBody PlatformRo.PlatformSubmit body) {
		return R.success(platformService.save(ModelWrapper.parseOne(body, Platform.class)));
	}

	/**
	 * 平台 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@Operation(summary = "平台 修改")
	public R<Boolean> update(@RequestBody PlatformRo.PlatformSubmit body) {
		return R.success(platformService.updateById(ModelWrapper.parseOne(body, Platform.class)));
	}

	/**
	 * 平台 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@Operation(summary = "平台 新增或修改")
	public R<Boolean> submit(@Valid @RequestBody PlatformRo.PlatformSubmit body) {
		return R.success(platformService.saveOrUpdate(ModelWrapper.parseOne(body, Platform.class)));
	}

	/**
	 * 平台 删除
	 */
	@GetMapping("/remove")
	@ApiOperationSupport(order = 7)
	@Operation(summary = "平台 逻辑删除")
	public R remove(@Parameter(description = "主键集合", required = true) @RequestParam String ids) {
		return R.success(platformService.deleteLogic(ValueUtils.toLongList(ids)));
	}

}
