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
import org.wlpiaoyi.server.demo.sys.domain.entity.User;
import org.wlpiaoyi.server.demo.sys.service.IUserService;
import org.wlpiaoyi.server.demo.sys.domain.vo.UserVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.UserRo;
import org.wlpiaoyi.server.demo.utils.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.request.Condition;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.framework.utils.ValueUtils;

import jakarta.validation.Valid;


/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	用户表 控制器
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Tag(name = "用户表接口")
public class UserController {

	private final IUserService userService;

	/**
	 * 用户表 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@Operation(summary = "用户表 详情")
	public R<UserVo> detail(UserRo.Query body) {
		UserVo user = ModelWrapper.parseOne(
				this.userService.getOne(
						Condition.getQueryWrapper(ModelWrapper.parseOne(body, User.class))
				),
				UserVo.class
		);
		return R.success(user);

	}

	/**
	 * 用户表 分页
	 */
	@PostMapping("/page")
	@ApiOperationSupport(order = 2)
	@Operation(summary = "用户表 分页")
	public R<IPage<UserVo>> page(@RequestBody UserRo.Query body){
		LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
		wrapper.orderByDesc(User::getCreateTime);
		IPage<User> pages = userService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, UserVo.class));
	}

	/**
	 * 用户表 分页
	 */
	@PostMapping("/list")
	@ApiOperationSupport(order = 3)
	@Operation(summary = "用户表 分页")
	public R<IPage<UserVo>> list(@RequestBody UserRo.Query body) {
		QueryWrapper<User> wrapper = Condition.getQueryWrapper(ModelWrapper.parseOne(body, User.class));
		wrapper.orderByDesc("create_time");
		IPage<User> pages = userService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, UserVo.class));
	}

	/**
	 * 用户表 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@Operation(summary = "用户表 新增")
	public R<Boolean> save(@Valid @RequestBody UserRo.Submit body) {
		return R.success(userService.save(ModelWrapper.parseOne(body, User.class)));
	}

	/**
	 * 用户表 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@Operation(summary = "用户表 修改")
	public R<Boolean> update(@RequestBody UserRo.Submit body) {
		return R.success(userService.updateById(ModelWrapper.parseOne(body, User.class)));
	}

	/**
	 * 用户表 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@Operation(summary = "用户表 新增或修改")
	public R<Boolean> submit(@Valid @RequestBody UserRo.Submit body) {
		return R.success(userService.saveOrUpdate(ModelWrapper.parseOne(body, User.class)));
	}

	/**
	 * 用户表 删除
	 */
	@GetMapping("/remove")
	@ApiOperationSupport(order = 7)
	@Operation(summary = "用户表 逻辑删除")
	public R remove(@Parameter(description = "主键集合", required = true) @RequestParam String ids) {
		return R.success(userService.deleteLogic(ValueUtils.toLongList(ids)));
	}

}
