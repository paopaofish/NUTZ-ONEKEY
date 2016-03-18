package com.kerbores.onkey.bean.acl;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import com.kerbores.utils.db.data.Entity;

/**
 * 
 * @author 王贵源
 *
 * @email kerbores@kerbores.com
 *
 * @description 角色实体类
 * 
 * @copyright 内部代码,禁止转发
 *
 *
 * @time 2016年1月26日 下午2:17:54
 */
@Table("tdb_role")
@Comment("角色表")
public class Role extends Entity {

	@Column("r_name")
	@Comment("角色名称")
	@Name
	private String name;

	@Column("r_desc")
	@Comment("描述")
	private String description;

	@Column("r_installed")
	@Comment("是否内置角色标识")
	private boolean installed;

	public boolean isInstalled() {
		return installed;
	}

	public void setInstalled(boolean installed) {
		this.installed = installed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
