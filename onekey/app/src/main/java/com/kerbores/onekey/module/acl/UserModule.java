package com.kerbores.onekey.module.acl;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.nutz.dao.entity.Record;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.lang.Lang;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.kerbores.nutz.module.base.AbstractBaseModule;
import com.kerbores.onekey.aop.SystemLog;
import com.kerbores.onekey.biz.acl.UserService;
import com.kerbores.onkey.bean.acl.User;
import com.kerbores.onkey.bean.acl.User.Type;
import com.kerbores.utils.entries.Result;
import com.kerbores.utils.web.pager.Pager;

/**
 * 
 * @author 王贵源
 *
 * @email kerbores@kerbores.com
 *
 * @description 用户控制器
 * 
 * @copyright 内部代码,禁止转发
 *
 *
 * @time 2016年1月26日 下午3:39:05
 */
@At("user")
public class UserModule extends AbstractBaseModule {

	@Inject
	UserService userService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kerbores.nutz.module.base.AbstractBaseModule#_getNameSpace()
	 */
	@Override
	public String _getNameSpace() {
		return "acl";
	}

	/**
	 * 用户列表
	 * 
	 * @param page
	 *            页码
	 * @return
	 */
	@At
	@Ok("beetl:pages/auth/user/list.html")
	@RequiresRoles("admin")
	@SystemLog(module = "用户管理", methods = "用户列表")
	public Result list(@Param(value = "page", df = "1") int page) {
		page = _fixPage(page);
		Pager<User> pager = userService.listByPage(page);
		pager.setUrl(_base() + "/user/list");
		return Result.success().addData("pager", pager);
	}

	/**
	 * 添加用户页面
	 * 
	 * @return
	 */
	@At
	@GET
	@Ok("beetl:pages/auth/user/add_edit.html")
	@RequiresRoles("admin")
	public Result add() {
		return Result.success();
	}

	/**
	 * 编辑用户页面
	 * 
	 * @param id
	 *            用户id
	 * @return
	 */
	@At("/edit/*")
	@GET
	@Ok("beetl:pages/auth/user/add_edit.html")
	@RequiresRoles("admin")
	public Result edit(int id) {
		return Result.success().addData("user", userService.findById(id));
	}

	/**
	 * 添加用户
	 * 
	 * @param user
	 *            待添加用户
	 * @return
	 */
	@At
	@POST
	@RequiresRoles("admin")
	public Result add(@Param("..") User user) {
		// TODO 密码加密方式
		user.setPassword(Lang.md5(user.getPassword()));
		user = userService.addUser(user);
		return user != null ? Result.success().addData("user", user) : Result.fail("添加用户失败!");
	}

	/**
	 * 编辑用户
	 * 
	 * @param user
	 *            待更新用户
	 * @return
	 */
	@At
	@POST
	@RequiresRoles("admin")
	public Result edit(@Param("..") User user) {
		return userService.update(user, "realName", "phone", "email", "status") ? Result.success() : Result.fail("更新失败!");
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 *            用户id
	 * @return
	 */
	@At("/delete/*")
	@RequiresRoles("admin")
	public Result delete(int id) {
		return userService.deleteUser(id) ? Result.success() : Result.fail("删除用户失败!");
	}

	/**
	 * 用户详情
	 * 
	 * @param id
	 *            用户id
	 * @return
	 */
	@At("/detail/*")
	@Ok("beetl:pages/auth/user/detail.html")
	@RequiresRoles("admin")
	public Result detail(int id) {
		return Result.success().addData("user", userService.fetch(id));
	}

	/**
	 * 搜索用户
	 * 
	 * @param key
	 *            关键词
	 * @param page
	 *            页码
	 * @return
	 */
	@At
	@Ok("beetl:pages/auth/user/list.html")
	@RequiresRoles("admin")
	public Result search(@Param("key") String key, @Param(value = "page", df = "1") int page) {
		page = _fixPage(page);
		key = _fixSearchKey(key);
		Pager<User> pager = userService.searchByKey(page, key);
		pager.setUrl(_base() + "/user/search");
		pager.addParas("key", key);
		return Result.success().addData("pager", pager);
	}

	/**
	 * 授权
	 * 
	 * @param id
	 * @return
	 */
	@At("/grant/*")
	@GET
	@Ok("beetl:pages/auth/user/grant.html")
	@RequiresRoles("admin")
	public Result grant(int id) {
		List<Record> records = userService.findPermissionsWithUserPowerdInfoByUserId(id, Type.PLATFORM);
		return Result.success().addData("records", records).addData("userId", id);
	}

	/**
	 * 授权
	 * 
	 * @param ids
	 * @param id
	 * @return
	 */
	@At
	@RequiresRoles("admin")
	public Result grant(@Param("permissions") int[] ids, @Param("id") int id) {
		return userService.setPermission(ids, id, Type.PLATFORM);
	}

	/**
	 * 设置角色
	 * 
	 * @param id
	 *            用户id
	 * @return
	 */
	@At("/role/*")
	@GET
	@RequiresRoles("admin")
	@Ok("beetl:pages/auth/user/role.html")
	public Result role(int id) {
		List<Record> records = userService.findRolesWithUserPowerdInfoByUserId(id, Type.PLATFORM);
		return Result.success().addData("records", records).addData("userId", id);
	}

	/**
	 * 设置角色
	 * 
	 * @param ids
	 *            角色ID数组
	 * @param id
	 *            用户id
	 * @return
	 */
	@At
	@RequiresRoles("admin")
	public Result role(@Param("roles") int[] ids, @Param("id") int id) {
		return userService.setRole(ids, id, Type.PLATFORM);
	}

}
