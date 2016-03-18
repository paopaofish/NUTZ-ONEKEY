package com.kerbores.onkey.bean;

import com.kerbores.onkey.bean.acl.Role;

/**
 * 
 * @author 王贵源
 *
 * @email kerbores@kerbores.com
 *
 * @description 内置角色枚举
 * 
 * @copyright 内部代码,禁止转发
 *
 *
 * @time 2016年1月26日 下午2:23:10
 */
public enum InstalledRole {
	/**
	 * 超级管理员
	 */
	ADMIN("admin", "超级管理员");
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 描述
	 */
	private String desc;

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
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @param name
	 * @param desc
	 */
	private InstalledRole(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	public Role toRole() {
		Role role = new Role();
		role.setDescription(desc);
		role.setInstalled(true);
		role.setName(name);
		return role;
	}

}
