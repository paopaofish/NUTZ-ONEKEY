package com.kerbores.onekey.ext.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.kerbores.onekey.biz.acl.ShiroUserService;
import com.kerbores.onkey.bean.acl.Permission;
import com.kerbores.onkey.bean.acl.User;
import com.kerbores.onkey.bean.acl.User.Type;
import com.kerbores.utils.collection.Lists;

/**
 * 
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project syl-biz
 *
 * @file Menus.java
 *
 * @description 菜单缓存
 *
 * @time 2016年3月8日 上午11:27:50
 *
 */
@IocBean
public class Menus {

	LoadingCache<User, List<Menu>> cache;
	Log log = Logs.get();

	/**
	 * 
	 * @return 缓存单例
	 *
	 * @author 王贵源
	 */
	public LoadingCache<User, List<Menu>> getCache() {
		if (cache == null) {
			cache = get();
		}
		return cache;
	}

	/**
	 * 
	 * @return 缓存实现
	 *
	 * @author 王贵源
	 */
	protected LoadingCache<User, List<Menu>> get() {
		return CacheBuilder.newBuilder().maximumSize(2000).expireAfterAccess(5, TimeUnit.MINUTES).removalListener(new RemovalListener<User, List<Menu>>() {

			@Override
			public void onRemoval(RemovalNotification<User, List<Menu>> notification) {
				log.debug(notification.getKey() + " removed....");
			}
		}).build(new CacheLoader<User, List<Menu>>() {

			@Override
			public List<Menu> load(User key) throws Exception {
				log.debug(key + " loading.... ");
				return loadUserMenus(key.getId(), key.getUserType());
			}
		});
	}

	/**
	 * 获取菜单
	 * 
	 * @param id
	 *            用户 id
	 * @param type
	 *            用户类型
	 * @return 菜单列表
	 *
	 * @author 王贵源
	 */
	public List<Menu> loadUserMenus(int id, Type type) {
		ShiroUserService shiroUserService = Mvcs.getIoc().get(ShiroUserService.class);
		List<Permission> permissions = shiroUserService.getAllPermissions(id, type);// 用户的全部权限
		final List<Permission> menuPermissions = Lists.newArrayList();
		// 筛选一下
		Lang.each(permissions, new Each<Permission>() {

			@Override
			public void invoke(int index, Permission permission, int length) throws ExitLoop, ContinueLoop, LoopException {
				if (permission.isMenu()) {
					menuPermissions.add(permission);
				}
			}
		});
		// 找一级
		List<Menu> menus = Lists.newArrayList();
		for (Permission permission : menuPermissions) {
			if (permission.getLevel() == 0 && permission.isMenu()) {
				Menu menu = permissionToMenu(permission);
				fillSubMenus(menu, menuPermissions);
				menus.add(menu);
			}
		}
		return menus;
	}

	/**
	 * 填充子菜单
	 * 
	 * @param menu
	 *            菜单
	 * @param menuPermissions
	 *            全部属于菜单的权限
	 *
	 * @author 王贵源
	 */
	private static void fillSubMenus(Menu menu, List<Permission> menuPermissions) {
		for (Permission permission : menuPermissions) {
			if (Strings.equals(permission.getNeedPermission(), menu.getName()) && permission.getLevel() == menu.getLevel() + 1) {// 依赖和级别相邻
				Menu temp = permissionToMenu(permission);
				fillSubMenus(temp, menuPermissions);// 递归来起
				menu.getSubNodes().add(temp);
			}
		}
	}

	/**
	 * 权限转换菜单
	 * 
	 * @param permission
	 *            权限
	 * @return 对应的菜单描述
	 *
	 * @author 王贵源
	 */
	public static Menu permissionToMenu(Permission permission) {
		Menu menu = new Menu();
		menu.setKey(permission.getHilightKey());
		menu.setLevel(permission.getLevel());
		menu.setName(permission.getName());
		menu.setUrl(permission.getUrl());
		menu.setIcon(permission.getIcon());
		menu.setDisplay(permission.getDescription());
		return menu;
	}

}
