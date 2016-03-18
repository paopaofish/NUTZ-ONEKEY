package com.kerbores.onekey.ext.ibeetl.tag;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.beetl.core.Tag;
import org.nutz.ioc.IocException;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;

import com.kerbores.onekey.ext.cache.Menu;
import com.kerbores.onekey.ext.cache.Menus;
import com.kerbores.onkey.bean.Application.SessionKeys;
import com.kerbores.onkey.bean.acl.User;
import com.kerbores.utils.collection.Lists;

/**
 * 
 * @author 王贵源
 *
 * @email kerbores@kerbores.com
 *
 * @description 树状菜单扩展标签
 * 
 * @copyright 内部代码,禁止转发
 *
 *
 * @time 2016年1月26日 下午3:33:10
 */
public class MenuTag extends Tag {
	Log log = Logs.get();

	protected String base = Mvcs.getReq().getAttribute("base").toString();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beetl.core.Tag#render()
	 */
	@Override
	public void render() {
		try {
			this.bw.writeString(genMenu());
		} catch (IOException e) {
			log.error(e);
		}
	}

	protected String genMenu() {
		List<Menu> menus = Lists.newArrayList();
		try {
			menus = Mvcs.getIoc().get(Menus.class).getCache().get((User) Mvcs.getReq().getSession().getAttribute(SessionKeys.USER_KEY));// 获取数据
		} catch (IocException e) {
			log.error(e);
		} catch (ExecutionException e) {
			log.error(e);
		}
		StringBuilder sb = new StringBuilder();
		for (Menu menu : menus) {
			sb.append(renderMenuNode(menu));
		}
		return sb.toString();
	}

	/**
	 * 渲染菜单节点
	 * 
	 * @param menu
	 * @return
	 */
	protected String renderMenuNode(Menu menu) {
		if (hasSub(menu)) {// 有子菜单
			StringBuilder subMenu = new StringBuilder();

			subMenu.append("<ul class='submenu'>");
			for (Menu sub : menu.getSubNodes()) {
				subMenu.append(renderMenuNode(sub));// 递归
			}
			subMenu.append("</ul>");
			return "<li class='" + (active(menu) ? "active open" : "") + "'><a href='#' class='dropdown-toggle'><i class='menu-icon fa fa-" + menu.getIcon()
					+ "'></i><span class='menu-text'> " + menu.getDisplay() + " </span><b class='arrow fa fa-angle-down'></b></a><b class='arrow'></b>" + subMenu + "</li>";
		} else {// 无子菜单
			return "<li class='" + (active(menu) ? "active open" : "") + "'><a href='" + base + "/" + menu.getUrl() + "'><i class='menu-icon fa fa-" + menu.getIcon()
					+ "'></i><span class='menu-text'> " + menu.getDisplay() + " </span></a><b class='arrow'></b></li>";
		}
	}

	/**
	 * 是否高亮
	 * 
	 * @param menu
	 * @return
	 */
	protected boolean active(Menu menu) {
		if (args == null || args.length == 0) {
			log.debug("no args");
			return false;
		}
		List<String> menuTags = (List<String>) args[0];// 这里可能抛出类型异常

		return Strings.equalsIgnoreCase(menu.getKey(), menuTags.get(menu.getLevel()));
	}

	/**
	 * 是否有子菜单
	 * 
	 * @param menu
	 * @return
	 */
	protected boolean hasSub(Menu menu) {
		if (menu.getSubNodes() == null) {
			return false;
		}
		return !menu.getSubNodes().isEmpty();
	}

}
