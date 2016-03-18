package com.kerbores.onekey.biz.acl;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;

import com.kerbores.onkey.bean.acl.RolePermission;
import com.kerbores.utils.biz.BaseService;
import com.kerbores.utils.db.SqlActuator;

/**
 * 
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project syl-biz
 *
 * @file RolePermissionService.java
 *
 * @description 角色权限关系业务处理
 *
 * @time 2016年3月8日 上午11:29:54
 *
 */
public class RolePermissionService extends BaseService<RolePermission> {

	/**
	 * 根据权限 id 查询
	 * 
	 * @param id
	 *            权限 id
	 * @return 关系列表
	 *
	 * @author 王贵源
	 */
	public List<RolePermission> findByPermissionId(int id) {
		return query(Cnd.where("permissionId", "=", id));
	}

	/**
	 * 根据角色 id 查询
	 * 
	 * @param id
	 *            角色 id
	 * @return 关系列表
	 *
	 * @author 王贵源
	 */
	public List<Record> findByRoleId(int id) {
		final Dao dao = dao();
		Sql sql = dao.sqls().create("find.role.permission.by.role.id");
		sql.params().set("id", id);
		return SqlActuator.runReport(sql, dao);
	}

}
