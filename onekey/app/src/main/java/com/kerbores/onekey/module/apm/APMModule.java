package com.kerbores.onekey.module.apm;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.nutz.dao.Cnd;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.kerbores.nutz.module.base.AbstractBaseModule;
import com.kerbores.onekey.biz.apm.APMAlarmService;
import com.kerbores.onekey.biz.setting.ConfigService;
import com.kerbores.onkey.bean.apm.APMAlarm;
import com.kerbores.onkey.bean.config.Config;
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

	@Inject
	PropertiesProxy config;

	@Inject
	ConfigService configService;

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
		return Result.success().addData(Gathers.all()).addData("config", config);
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

	@At
	@RequiresRoles("admin")
	public Result setting(@Param("type") String type, @Param("types") String types, @Param("percent") String percent) {
		NutMap data = NutMap.NEW();
		data.put(type + ".alarm.percent", percent);
		data.put(type + ".alarm.types", types);

		config.putAll(data);// 更新内存

		Config pConfig = configService.fetch(Cnd.where("name", "=", type + ".alarm.percent"));
		Config tConfig = configService.fetch(Cnd.where("name", "=", type + ".alarm.types"));
		if (pConfig == null) {
			pConfig = new Config();
			pConfig.setName(type + ".alarm.percent");
			configService.save(pConfig);
		}
		pConfig.setValue(percent);
		if (tConfig == null) {
			tConfig = new Config();
			tConfig.setName(type + ".alarm.types");
			configService.save(tConfig);
		}
		tConfig.setValue(types);

		return configService.update(tConfig) == 1 && configService.update(pConfig) == 1 ? Result.success() : Result.fail("配置失败!"); // 更新数据库
	}

}
