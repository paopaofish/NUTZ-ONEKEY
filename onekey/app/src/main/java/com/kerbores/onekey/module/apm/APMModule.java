package com.kerbores.onekey.module.apm;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.kerbores.nutz.module.base.AbstractBaseModule;
import com.kerbores.onekey.biz.apm.APMAlarmService;
import com.kerbores.onkey.bean.apm.APMAlarm;
import com.kerbores.sigar.gathers.Gathers;
import com.kerbores.utils.entries.Result;
import com.kerbores.utils.web.pager.Pager;

/**
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project app
 *
 * @file APMModule.java
 *
 * @description // TODO write some description of this class
 *
 * @time 2016年3月15日 下午4:55:00
 *
 */
@At("apm")
public class APMModule extends AbstractBaseModule {

	@Inject
	APMAlarmService apmAlarmService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kerbores.nutz.module.base.AbstractBaseModule#_getNameSpace()
	 */
	@Override
	public String _getNameSpace() {
		return "apm";
	}

	@At
	@Ok("beetl:pages/apm/dashboard.html")
	@RequiresRoles("admin")
	public Result dashboard() {
		return Result.success().addData(Gathers.all());
	}

	@At
	@Filters
	public Pager<APMAlarm> alarm() {
		return apmAlarmService.searchByPage(1, 5, null);
	}

	@At
	@Ok("beetl:pages/apm/list.html")
	@RequiresRoles("admin")
	public Result list(@Param(value = "page", df = "1") int page) {
		Pager<APMAlarm> pager = apmAlarmService.searchByPage(_fixPage(page));
		pager.setUrl(_base() + "/apm/list");
		return Result.success().addData("pager", pager).setTitle("告警列表");
	}

	@At
	@Ok("beetl:pages/apm/list.html")
	@RequiresRoles("admin")
	public Result search(@Param(value = "page", df = "1") int page, @Param("key") String key) {
		Pager<APMAlarm> pager = apmAlarmService.searchByKeyAndPage(_fixSearchKey(key), _fixPage(page), "type", "ip");
		pager.setUrl(_base() + "/apm/search");
		pager.addParas("key", key);
		return Result.success().addData("pager", pager).setTitle("告警列表");
	}

	@At("/detail/*")
	@Filters
	public APMAlarm detail(String code) {
		return apmAlarmService.fetch(code);
	}

}
