package club.zhcs.thunder;

import java.nio.charset.Charset;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.integration.quartz.NutQuartzCronJobFactory;
import org.nutz.integration.shiro.NutShiro;
import org.nutz.ioc.Ioc;
import org.nutz.lang.Encoding;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import club.zhcs.thunder.bean.acl.User;
import club.zhcs.thunder.bean.acl.User.Status;
import club.zhcs.thunder.biz.acl.UserService;

/**
 * 
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project thunder-web
 *
 * @file ThunderSetup.java
 *
 * @description 初始化
 *
 * @time 2016年3月8日 上午10:51:26
 *
 */
public class ThunderSetup implements Setup {
	private static final Log log = Logs.get();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nutz.mvc.Setup#destroy(org.nutz.mvc.NutConfig)
	 */
	@Override
	public void destroy(NutConfig nc) {
		// 初始化
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nutz.mvc.Setup#init(org.nutz.mvc.NutConfig)
	 */
	@Override
	public void init(NutConfig nc) {

		NutShiro.DefaultLoginURL = "/";
		
		if (!Charset.defaultCharset().name().equalsIgnoreCase(Encoding.UTF8)) {
			log.warn("This project must run in UTF-8, pls add -Dfile.encoding=UTF-8 to JAVA_OPTS");
		}

		Ioc ioc = nc.getIoc();

		Dao dao = ioc.get(Dao.class);
		
		ioc.get(NutQuartzCronJobFactory.class);// 触发任务

		// 为全部标注了@Table的bean建表
		Daos.createTablesInPackage(dao, getClass().getPackage().getName() + ".bean", false);
		Daos.migration(dao, getClass().getPackage().getName() + ".bean", true, true);

		// 超级管理员
		UserService userService = ioc.get(UserService.class);
		User surperMan = null;
		if ((surperMan = userService.fetch(Cnd.where("name", "=", "admin"))) == null) {
			surperMan = new User();
			surperMan.setEmail("kerbores@zhcs.club");
			surperMan.setName("admin");
			surperMan.setPassword(Lang.md5("123456"));
			surperMan.setPhone("18996359755");
			surperMan.setRealName("王贵源");
			surperMan.setStatus(Status.ACTIVED);
			surperMan = userService.save(surperMan);
		}
	}

}
