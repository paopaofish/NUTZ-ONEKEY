package com.kerbores.onkey.bean;

import org.nutz.ioc.impl.PropertiesProxy;

/**
 * 
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project syl-bean
 *
 * @file Application.java
 *
 * @description 应用常量数据
 *
 * @time 2016年3月8日 上午10:51:26
 *
 */
public class Application {
	protected static PropertiesProxy conf;

	static {
		conf = new PropertiesProxy(false);
		conf.setIgnoreResourceNotFound(true);
		conf.setPaths("conf/application.properties",
				"/home/tdb/conf/application.properties");
	}

	/**
	 * 页面大小
	 */
	public static final int PAGESIZE = conf.getInt("page.size");

	/**
	 * 应用名称
	 */
	public static final String NAME = conf.get("application.name");

	/**
	 * 百度地图appkey
	 */
	public static final String BAIDUMAPKEY = conf.get("baidu.map.key");

	/**
	 * 基础域名
	 */
	public static final String DOMAIN = conf.get("base.domain");

	/**
	 * 
	 * @author 王贵源
	 * @description session keys
	 * @Copyright 内部代码,禁止转发
	 * @date 2015年12月28日 下午9:54:31
	 *
	 */
	public static class SessionKeys {
		/**
		 * 用戶在session中保存的key
		 */
		public static final String USER_KEY = "KERBORES_USER";
	}

}
