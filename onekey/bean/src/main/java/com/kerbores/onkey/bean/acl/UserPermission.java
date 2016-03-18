package com.kerbores.onkey.bean.acl;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import com.kerbores.onkey.bean.acl.User.Type;
import com.kerbores.utils.db.data.Entity;

/**
 * 
 * @author 王贵源
 *
 * @email kerbores@kerbores.com
 *
 * @description 用户权限关系实体
 * 
 * @copyright 内部代码,禁止转发
 *
 *
 * @time 2016年1月26日 下午2:19:54
 */
@Table("tdb_user_permission")
@Comment("用户权限关系表")
public class UserPermission extends Entity {
	/**
	 * 用户id
	 */
	@Column("u_id")
	@Comment("用户id")
	private int userId;
	/**
	 * 权限id
	 */
	@Column("p_id")
	@Comment("权限id")
	private int permissionId;

	@Column("u_type")
	@Comment("用户类型")
	private Type type;

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return the permissionId
	 */
	public int getPermissionId() {
		return permissionId;
	}

	/**
	 * @param permissionId
	 *            the permissionId to set
	 */
	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}

}