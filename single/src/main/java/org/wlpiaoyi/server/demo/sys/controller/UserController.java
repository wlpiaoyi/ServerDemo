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
import org.wlpiaoyi.framework.utils.exception.SystemException;
import org.wlpiaoyi.server.demo.sys.domain.entity.User;
import org.wlpiaoyi.server.demo.sys.service.IUserService;
import org.wlpiaoyi.server.demo.sys.domain.vo.UserVo;
import org.wlpiaoyi.server.demo.sys.domain.ro.UserRo.*;
import org.wlpiaoyi.server.demo.utils.tools.ModelWrapper;
import org.wlpiaoyi.server.demo.utils.response.R;
import org.wlpiaoyi.server.demo.utils.request.Condition;
import org.springframework.web.bind.annotation.*;
import org.wlpiaoyi.framework.utils.ValueUtils;

import jakarta.validation.Valid;
import org.wlpiaoyi.server.demo.utils.web.annotation.Decrypt;
import org.wlpiaoyi.server.demo.utils.web.annotation.Encrypt;
import org.wlpiaoyi.server.demo.utils.web.annotation.Idempotence;
import org.wlpiaoyi.server.demo.utils.web.annotation.PreAuthorize;

/**
 * {@code @author:} 		wlpia:WLPIAOYI-PC
 * {@code @description:} 	用户表 控制器
 * {@code @date:} 			2024-10-10 23:05:43
 * {@code @version:}: 		1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys/user")
@Tag(name = "用户表接口")
public class UserController {

	private final IUserService userService;



	/**
	 * 用户登录
	 */
	@Idempotence
	@Encrypt
	@Decrypt
	@PostMapping("/login")
	@ApiOperationSupport(order = 0)
	@Operation(summary = "用户登录")
	public R<UserVo> login(@RequestHeader String token, @RequestBody UserAuth body) {
		return R.success(this.userService.login(token, body));
	}


	/**
	 * 切换角色
	 */
	@Idempotence
	@Encrypt
	@GetMapping("/switch/role")
	@ApiOperationSupport(order = 0)
	@Operation(summary = "切换角色")
	public R<Boolean> switchRole(@RequestHeader String token, @RequestParam Long roleId) throws SystemException {
		this.userService.switchRole(token, roleId);
		return R.success(true);
	}

	/**
	 * token续期
	 */
	@Idempotence
	@Encrypt
	@GetMapping("/expire")
	@ApiOperationSupport(order = 0)
	@Operation(summary = "token续期")
	public R<Boolean> expire(@RequestHeader String token) throws SystemException {
		this.userService.expire(token);
		return R.success(true);
	}

	/**
	 * 用户表 详情
	 */
	@Encrypt
	@Decrypt
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@Operation(summary = "用户表 详情")
	public R<UserVo> detail(@RequestParam Long id) {
		return R.success(this.userService.getDetail(id));

	}

	/**
	 * 用户表 分页
	 */
	@Decrypt
	@PostMapping("/list")
	@PreAuthorize("user_list")
	@ApiOperationSupport(order = 3)
	@Operation(summary = "用户表 分页")
	public R<IPage<UserVo>> list(@RequestBody UserQuery body) {
		QueryWrapper<User> wrapper = Condition.getQueryWrapper(ModelWrapper.parseOne(body, User.class));
		wrapper.orderByDesc("create_time");
		IPage<User> pages = userService.page(Condition.getPage(body), wrapper);
		return R.success(ModelWrapper.parseForPage(pages, UserVo.class));
	}

	/**
	 * 用户表 新增
	 */
	@Decrypt
	@PostMapping("/save")
	@PreAuthorize("user_add")
	@ApiOperationSupport(order = 4)
	@Operation(summary = "用户表 新增")
	public R<Boolean> save(@Valid @RequestBody UserSubmit body) {
		return R.success(userService.save(ModelWrapper.parseOne(body, User.class)));
	}

	/**
	 * 用户表 修改
	 */
	@Decrypt
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@PreAuthorize("user_update")
	@Operation(summary = "用户表 修改")
	public R<Boolean> update(@RequestBody UserSubmit body) {
		return R.success(userService.updateById(ModelWrapper.parseOne(body, User.class)));
	}

	/**
	 * 用户表 删除
	 */
	@GetMapping("/remove")
	@ApiOperationSupport(order = 7)
	@PreAuthorize("user_remove")
	@Operation(summary = "用户表 逻辑删除")
	public R remove(@Parameter(description = "主键集合", required = true) @RequestParam String ids) {
		return R.success(userService.deleteLogic(ValueUtils.toLongList(ids)));
	}

}
