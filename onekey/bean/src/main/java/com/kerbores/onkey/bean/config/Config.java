package com.kerbores.onkey.bean.config;

import java.net.SocketException;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import com.kerbores.utils.db.data.Entity;

/**
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project bean
 *
 * @file Config.java
 *
 * @description // TODO write some description of this class
 *
 * @time 2016年3月15日 下午1:20:36
 *
 */
@Table("tdb_sys_config")
public class Config extends Entity {

	public static void main(String[] args) throws SocketException {

	}

	@Name
	@Column("cfg_key")
	@Comment("配置键")
	private String name;

	@Column("cfg_value")
	@Comment("配置值")
	private String value;

	@Column("cfg_description")
	@Comment("配置说明")
	@ColDefine(width = 250)
	private String description;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	

}
