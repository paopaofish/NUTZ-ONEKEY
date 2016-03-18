package com.kerbores.onekey.module;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.lang.Strings;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.kerbores.nutz.captcha.JPEGView;
import com.kerbores.nutz.module.base.AbstractBaseModule;
import com.kerbores.onekey.biz.acl.ShiroUserService;
import com.kerbores.onkey.bean.Application.SessionKeys;
import com.kerbores.utils.entries.Result;

/**
 * @author 王贵源<kerbores>
 *
 *         create at 2015年12月10日 下午3:11:44
 */
@At("system")
public class SystemModule extends AbstractBaseModule {

	@Inject
	ShiroUserService shiroUserService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kerbores.nutz.module.base.AbstractBaseModule#_getNameSpace()
	 */
	@Override
	public String _getNameSpace() {
		return "system";
	}

	// 此处前台是json请求所以会返回json串
	@At
	@Filters
	public Result login(@Param("user") String user, @Param("password") String password, @Param("captcha") String captcha, HttpSession session) {
		if (Strings.equalsIgnoreCase(captcha, session.getAttribute(JPEGView.CAPTCHA).toString())) {
			Result result = shiroUserService.login(user, password);
			if (result.isSuccess()) {
				// 登录成功处理
				_putSession(SessionKeys.USER_KEY, result.getData().get("loginUser"));
			}
			return result;
		} else {
			return Result.fail("验证码输入错误");
		}
	}

	@At
	@Ok("beetl:pages/auth/permission/list.html")
	public Result main() {
		return Result.success();
	}

	@At("/logout")
	public View logOut() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
		if (log.isDebugEnabled()) {
			log.debug("用户退出登录");
		}
		return _renderRedirct("/");
	}
}
