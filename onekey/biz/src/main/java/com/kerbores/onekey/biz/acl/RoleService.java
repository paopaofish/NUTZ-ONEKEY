package com.kerbores.onekey.biz.acl;

import java.util.Collections;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;

import com.kerbores.onkey.bean.acl.Role;
import com.kerbores.onkey.bean.acl.RolePermission;
import com.kerbores.onkey.bean.acl.User.Type;
import com.kerbores.utils.biz.BaseService;
import com.kerbores.utils.collection.Lists;
import com.kerbores.utils.db.SqlActuator;
import com.kerbores.utils.entries.Result;
import com.kerbores.utils.web.pager.Pager;

/**
 * 
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project syl-biz
 *
 * @file RoleService.java
 *
 * @description 角色相关业务处理
 *
 * @time 2016年3月8日 上午11:30:11
 *
 */
public class RoleService extends BaseService<Role> {

	@Inject
	private RolePermissionService rolePermissionService;

	/**
	 * 根据用户查询
	 * 
	 * @param id
	 *            用户 id
	 * @param type
	 *            用户类型
	 * @return 角色列表
	 *
	 * @author 王贵源
	 */
	public List<Role> findByUserId(int id, Type type) {
		final Dao dao = dao();// 获取数据访问对象

		final List<Role> roles = Lists.newArrayList();
		// 获取配置的sql
		Sql sql = dao.sqls().create("find.role.by.user.id");

		sql.params().set("userid", id);// 注入sql参数
		sql.params().set("type", type);

		List<Record> records = SqlActuator.runReport(sql, dao);// 执行sql

		Lang.each(records, new Each<Record>()// 将记录转换成对象
				{

					@Override
					public void invoke(int index, Record record, int length) throws ExitLoop, ContinueLoop, LoopException {
						roles.add(record.toEntity(dao.getEntity(Role.class)));
					}
				});

		return roles;
	}

	/**
	 * 根据名称查询
	 * 
	 * @param name
	 *            角色名称
	 * @return 角色
	 *
	 * @author 王贵源
	 */
	public Role findByName(String name) {
		return fetch(Cnd.where("name", "=", name));
	}

	/**
	 * 添加角色
	 * 
	 * @param role
	 *            待添加角色
	 * @return 角色 将携带数据库 id
	 *
	 * @author 王贵源
	 */
	public Role addRole(Role role) {
		return save(role);
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 *            页码
	 * @return 角色分页对象
	 *
	 * @author 王贵源
	 */
	public Pager<Role> findByPage(int page) {
		return searchByPage(page);

	}

	/**
	 * 关键词查询
	 * 
	 * @param page
	 *            页码
	 * @param key
	 *            关键词
	 * @return 角色分页对象
	 *
	 * @author 王贵源
	 */
	public Pager<Role> searchByKey(int page, String key) {
		return searchByKeyAndPage(key, page, "name", "description");
	}

	/**
	 * 设置权限
	 * 
	 * @param ids
	 *            权限 id 串
	 * @param roleId
	 *            角色 id
	 * @return 处理结果
	 *
	 * @author 王贵源
	 */
	public Result setPermission(int[] ids, int roleId) {
		/**
		 * 1.查询全部权限列表<br>
		 * 2.遍历权限.如果存在,则更新时间.如果不存在则删除,处理之后从目标数组中移除;<br>
		 * 3.遍历余下的目标数组
		 */
		if (ids == null) {
			ids = new int[] {};
		}
		List<Integer> newIds = Lang.array2list(ids, Integer.class);
		Collections.sort(newIds);
		List<RolePermission> rolePermissions = rolePermissionService.query(Cnd.where("roleId", "=", roleId));
		for (RolePermission role : rolePermissions) {
			int i = 0;
			if ((i = Collections.binarySearch(newIds, role.getPermissionId())) >= 0) {
				newIds.remove(i);
				// role.setCreateTime(now());
				rolePermissionService.update(role);
			} else {
				rolePermissionService.delete(role.getId());
			}
		}
		for (int pid : newIds) {
			RolePermission rolep = new RolePermission();
			rolep.setRoleId(roleId);
			rolep.setPermissionId(pid);
			rolePermissionService.save(rolep);
		}
		return Result.success();
	}

	/**
	 * 根据角色 id 查询权限及其授权情况
	 * 
	 * @param id
	 *            角色 id
	 * @return 记录列表
	 *
	 * @author 王贵源
	 */
	public List<Record> findPermissionsWithRolePowerdInfoByRoleId(int id) {
		Dao dao = dao();
		Sql sql = dao.sqls().create("find.permissions.with.role.powered.info.by.role.id");
		sql.params().set("id", id);
		return SqlActuator.runReport(sql, dao);
	}
}
