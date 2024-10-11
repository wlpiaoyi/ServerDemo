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
import org.wlpiaoyi.server.demo.sys.domain.entity.Menu;
import org.wlpiaoyi.server.demo.sys.service.IMenuService;
import org.wlpiaoyi.server.demo.sys.domain.vo.MenuVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.MenuRo.*;
import org.wlpiaoyi.server.demo.utils.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.request.Condition;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.framework.utils.ValueUtils;

import jakarta.validation.Valid;


/**
 * {@code @author:} 		wlpiaoyi:WLPIAOYI-DELL
 * {@code @description:} 	菜单 控制器
 * {@code @date:} 			2024-10-11 17:34:54
 * {@code @version:}: 		1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/menu")
@Tag(name = "菜单接口")
public class MenuController {

	private final IMenuService menuService;

	/**
	 * 菜单 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@Operation(summary = "菜单 详情")
	public R<MenuVo> detail(MenuQuery body) {
		MenuVo menu = ModelWrapper.parseOne(
				this.menuService.getOne(
						Condition.getQueryWrapper(ModelWrapper.parseOne(body, Menu.class))
				),
				MenuVo.class
		);
		return R.success(menu);

	}

	/**
	 * 菜单 分页
	 */
	@PostMapping("/page")
	@ApiOperationSupport(order = 2)
	@Operation(summary = "菜单 分页")
	public R<IPage<MenuVo>> page(@RequestBody MenuQuery body){
		LambdaQueryWrapper<Menu> wrapper = Wrappers.<Menu>lambdaQuery();
		wrapper.orderByDesc(Menu::getCreateTime);
		IPage<Menu> pages = menuService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, MenuVo.class));
	}

	/**
	 * 菜单 分页
	 */
	@PostMapping("/list")
	@ApiOperationSupport(order = 3)
	@Operation(summary = "菜单 分页")
	public R<IPage<MenuVo>> list(@RequestBody MenuQuery body) {
		QueryWrapper<Menu> wrapper = Condition.getQueryWrapper(ModelWrapper.parseOne(body, Menu.class));
		wrapper.orderByDesc("create_time");
		IPage<Menu> pages = menuService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, MenuVo.class));
	}

	/**
	 * 菜单 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@Operation(summary = "菜单 新增")
	public R<Boolean> save(@Valid @RequestBody MenuSubmit body) {
		return R.success(menuService.save(ModelWrapper.parseOne(body, Menu.class)));
	}

	/**
	 * 菜单 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@Operation(summary = "菜单 修改")
	public R<Boolean> update(@RequestBody MenuSubmit body) {
		return R.success(menuService.updateById(ModelWrapper.parseOne(body, Menu.class)));
	}

	/**
	 * 菜单 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@Operation(summary = "菜单 新增或修改")
	public R<Boolean> submit(@Valid @RequestBody MenuSubmit body) {
		return R.success(menuService.saveOrUpdate(ModelWrapper.parseOne(body, Menu.class)));
	}

	/**
	 * 菜单 删除
	 */
	@GetMapping("/remove")
	@ApiOperationSupport(order = 7)
	@Operation(summary = "菜单 逻辑删除")
	public R remove(@Parameter(description = "主键集合", required = true) @RequestParam String ids) {
		return R.success(menuService.deleteLogic(ValueUtils.toLongList(ids)));
	}

}