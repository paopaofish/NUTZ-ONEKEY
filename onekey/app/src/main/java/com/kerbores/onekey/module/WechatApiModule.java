package com.kerbores.onekey.module;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Filters;
import org.nutz.weixin.spi.WxApi2;

import com.kerbores.nutz.module.base.AbstractBaseModule;
import com.kerbores.utils.entries.Result;

/**
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project app
 *
 * @file WechatApiModule.java
 *
 * @description // TODO write some description of this class
 *
 * @time 2016年3月25日 上午10:49:49
 *
 */
@At("wechat")
@Filters
public class WechatApiModule extends AbstractBaseModule {

	@Inject("wxApi")
	WxApi2 api;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kerbores.nutz.module.base.AbstractBaseModule#_getNameSpace()
	 */
	@Override
	public String _getNameSpace() {
		return null;
	}

	@At
	public Result test() {
		return Result.success().addData("resp", api.menu_delete());
	}

}
