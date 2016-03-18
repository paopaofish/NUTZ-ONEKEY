package com.kerbores.onekey.module.acl;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.kerbores.nutz.module.base.AbstractBaseModule;
import com.kerbores.onekey.biz.acl.PermissionService;
import com.kerbores.onkey.bean.acl.Permission;
import com.kerbores.utils.entries.Result;
import com.kerbores.utils.web.pager.Pager;

/**
 * 
 * @author 王贵源
 *
 * @email kerbores@kerbores.com
 *
 * @description 权限控制器
 * 
 * @copyright 内部代码,禁止转发
 *
 *
 * @time 2016年1月26日 下午3:37:37
 */
@At("permission")
public class PermissionModule extends AbstractBaseModule {

	@Inject
	private PermissionService permissionService;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgj.nutz.module.base.AbstractBaseModule#_getNameSpace()
	 */
	@Override
	public String _getNameSpace() {
		return "acl";
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @return
	 *
	 * @author 王贵源
	 */
	@At
	@Ok("beetl:pages/auth/permission/list.html")
	@RequiresRoles("admin")
	public Result list(@Param(value = "page", df = "1") int page) {
		page = _fixPage(page);
		Pager<Permission> pager = permissionService.findByPage(page);
		pager.setUrl(_base() + "/permission/list");
		return Result.success().addData("pager", pager).setTitle("权限列表");
	}

	/**
	 * 搜索
	 * 
	 * @param page
	 * @param key
	 * @return
	 */
	@At
	@Ok("beetl:pages/auth/permission/list.html")
	@RequiresRoles("admin")
	public Result search(@Param(value = "page", df = "1") int page, @Param("key") String key) {
		page = _fixPage(page);
		key = _fixSearchKey(key);
		Pager<Permission> pager = permissionService.search(page, key);
		pager.setUrl(_base() + "/permission/search");
		pager.addParas("key", key);
		return Result.success().addData("pager", pager).addData("key", key);
	}

}
