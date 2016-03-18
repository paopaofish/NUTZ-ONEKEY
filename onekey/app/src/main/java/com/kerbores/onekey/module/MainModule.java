package com.kerbores.onekey.module;

import javax.servlet.http.HttpServletRequest;

import org.beetl.ext.nutz.BeetlViewMaker;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.annotation.Views;
import org.nutz.mvc.filter.CheckSession;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

import com.kerbores.nutz.captcha.JPEGView;
import com.kerbores.nutz.module.base.AbstractBaseModule;
import com.kerbores.onekey.ext.cache.Menus;
import com.kerbores.onekey.setup.Setup;
import com.kerbores.onkey.bean.Application.SessionKeys;
import com.kerbores.onkey.bean.acl.User;
import com.kerbores.shiro.ShiroActionFilter;
import com.kerbores.utils.entries.Result;

/**
 * @author 王贵源<kerbores>
 *
 *         create at 2015年12月7日 上午9:24:39
 */
@Modules(scanPackage = true)
@IocBy(type = ComboIocProvider.class, args = { "*anno", "com.kerbores", "*tx",
		"*js", "ioc", "*async", "*com.kerbores.quartz.QuartzIocLoader", "quartz" })
@Views(BeetlViewMaker.class)
@Fail("http:500")
@Ok("json")
@Filters({ @By(type = ShiroActionFilter.class, args = "/"), @By(type = CheckSession.class, args = { SessionKeys.USER_KEY, "/" }) })
@SetupBy(Setup.class)
public class MainModule extends AbstractBaseModule {

	@Inject
	Dao dao;
	@Inject
	Menus menus;

	@At
	@Filters
	public View captcha(@Param("length") int length) {
		return new JPEGView(null, length);
	}

	@At("/")
	@Ok("jsp:/login")
	@Filters
	public View login(@Attr(SessionKeys.USER_KEY) User user) {
		if (user != null) {
			return _renderRedirct("/system/main");
		}
		return null;
	}

	@At
	@Filters
	public Result client(HttpServletRequest request) {
		return Result.success().addData("ip", _ip()).addData("ua", _ua());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kerbores.nutz.module.base.AbstractBaseModule#_getNameSpace()
	 */
	@Override
	public String _getNameSpace() {
		return null;
	}

}
