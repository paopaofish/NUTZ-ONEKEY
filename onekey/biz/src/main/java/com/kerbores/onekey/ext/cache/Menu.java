package com.kerbores.onekey.ext.cache;

import java.util.List;

import org.nutz.json.Json;

import com.kerbores.utils.collection.Lists;

/**
 * 
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project syl-biz
 *
 * @file Menu.java
 *
 * @description 菜单定义
 *
 * @time 2016年3月8日 上午11:27:36
 *
 */
public class Menu {
	/**
	 * 菜单名称
	 */
	private String name;
	/**
	 * 菜单显示名称
	 */
	private String display;
	/**
	 * 菜单级别
	 */
	private int level;
	/**
	 * 菜单高亮关键词
	 */
	private String key;

	/**
	 * 菜单icon
	 */
	private String icon;

	/**
	 * 菜单url
	 */
	private String url;
	/**
	 * 子菜单
	 */
	private List<Menu> subNodes = Lists.newArrayList();

	/**
	 * @return the display
	 */
	public String getDisplay() {
		return display;
	}

	/**
	 * @param display
	 *            the display to set
	 */
	public void setDisplay(String display) {
		this.display = display;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the subNodes
	 */
	public List<Menu> getSubNodes() {
		return subNodes;
	}

	/**
	 * @param subNodes
	 *            the subNodes to set
	 */
	public void setSubNodes(List<Menu> subNodes) {
		this.subNodes = subNodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Json.toJson(this);
	}

}
