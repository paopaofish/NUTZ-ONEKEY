package com.kerbores.onekey.biz.acl;

import java.util.Collections;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;

import com.kerbores.onkey.bean.acl.Role;
import com.kerbores.onkey.bean.acl.User;
import com.kerbores.onkey.bean.acl.User.Type;
import com.kerbores.onkey.bean.acl.UserPermission;
import com.kerbores.onkey.bean.acl.UserRole;
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
 * @file UserService.java
 *
 * @description 用户相关业务
 *
 * @time 2016年3月8日 上午11:31:20
 *
 */
public class UserService extends BaseService<User> {

	@Inject
	UserRoleService userRoleService;

	@Inject
	UserPermissionService userPermissionService;

	/**
	 * 根据用户名查询
	 * 
	 * @param userName
	 *            用户名
	 * @return 用户
	 *
	 * @author 王贵源
	 */
	public User findByName(String userName) {
		return fetch(Cnd.where("name", "=", userName));
	}

	/**
	 * 添加用户
	 * 
	 * @param user
	 *            待添加用户
	 * @return 添加之后的用户
	 *
	 * @author 王贵源
	 */
	public User addUser(User user) {
		return save(user);
	}

	/**
	 * 根据真是姓名查询
	 * 
	 * @param realName
	 *            真是姓名
	 * @return 用户列表
	 *
	 * @author 王贵源
	 */
	public List<User> findByRealName(String realName) {
		return query(Cnd.where("realName", "=", realName));
	}

	/**
	 * 根据 id 查询
	 * 
	 * @param id
	 *            用户 id
	 * @return 用户
	 *
	 * @author 王贵源
	 */
	public User findById(int id) {
		return findById(id);
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 *            页面
	 * @return 用户分页对象
	 *
	 * @author 王贵源
	 */
	public Pager<User> listByPage(int page) {
		return searchByPage(page);
	}

	/**
	 * 关键词检索
	 * 
	 * @param page
	 *            页码
	 * @param key
	 *            关键词
	 * @return 用户分页列表
	 *
	 * @author 王贵源
	 */
	public Pager<User> searchByKey(int page, String key) {
		return searchByKeyAndPage(key, page, "name", "realName");
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 *            用户 id
	 * @return 删除结果
	 *
	 * @author 王贵源
	 */
	public boolean deleteUser(int id) {
		return delete(id) == 1;
	}

	/**
	 * 根据 id 查询用户的角色授权信息
	 * 
	 * @param id
	 *            用户 id
	 * @return 角色列表及当前用户的授权信息
	 *
	 * @author 王贵源
	 */
	public List<Record> findUserRoleStatusByUserId(int id) {
		Dao dao = dao();
		Sql sql = dao.sqls().create("find.user.role.status.by.user.id");
		sql.params().set("id", id);
		return SqlActuator.runReport(sql, dao);
	}

	/**
	 * 设置角色
	 * 
	 * @param ids
	 *            角色 id串
	 * @param userId
	 *            用户 id
	 * @param type
	 *            用户类型
	 * @return
	 *
	 * @author 王贵源
	 */
	public Result setRole(int[] ids, int userId, Type type) {
		/**
		 * 1.查询用户现在的全部角色<br>
		 * 2.遍历角色,如果存在更新时间,如果不存在删除,处理之后从目标数组中移除元素<br>
		 * 3.遍历剩余的目标数组,添加关系
		 */
		if (ids == null) {
			ids = new int[] {};
		}
		List<Integer> newIds = Lang.array2list(ids, Integer.class);
		Collections.sort(newIds);
		List<UserRole> userRoles = userRoleService.query(Cnd.where("userId", "=", userId).and("type", "=", type));
		for (UserRole role : userRoles) {
			int i = 0;
			if ((i = Collections.binarySearch(newIds, role.getRoleId())) >= 0) {
				newIds.remove(i);
			} else {
				userRoleService.delete(role.getId());
			}
		}
		for (int rid : newIds) {
			UserRole relation = new UserRole();
			relation.setRoleId(rid);
			relation.setUserId(userId);
			relation.setType(type);
			userRoleService.save(relation);
		}
		return Result.success();
	}

	/**
	 * 查询用户及权限授权信息
	 * 
	 * @param id
	 *            用户 id
	 * @return 权限列表及当前用户授权信息
	 *
	 * @author 王贵源
	 */
	public List<Record> findUserPermissionByUserId(int id) {
		final Dao dao = dao();
		Sql sql = dao.sqls().create("find.uer.permission.by.user.id");
		sql.params().set("id", id);
		return SqlActuator.runReport(sql, dao);
	}

	/**
	 * 设置权限
	 * 
	 * @param ids
	 *            权限 id 串
	 * @param userId
	 *            用户 id
	 * @param type
	 *            用户类型
	 * @return
	 *
	 * @author 王贵源
	 */
	public Result setPermission(int[] ids, int userId, Type type) {
		/**
		 * 1.查询用户现在的全部权限<br>
		 * 2.遍历权限,如果存在更新时间,如果不存在删除,处理之后从目标数组中移除元素<br>
		 * 3.遍历剩余的目标数组,添加关系
		 */
		if (ids == null) {
			ids = new int[] {};
		}
		List<Integer> newIds = Lang.array2list(ids, Integer.class);
		Collections.sort(newIds);
		List<UserPermission> list = userPermissionService.query(Cnd.where("userId", "=", userId).and("type", "=", type));
		for (UserPermission user : list) {
			int i = 0;
			if ((i = Collections.binarySearch(newIds, user.getPermissionId())) >= 0) {
				newIds.remove(i);
			} else {
				userPermissionService.delete(user.getId());
			}
		}
		for (int pid : newIds) {
			UserPermission userp = new UserPermission();
			userp.setUserId(userId);
			userp.setPermissionId(pid);
			userp.setType(type);
			userPermissionService.save(userp);
		}
		return Result.success();
	}

	/**
	 * 根据角色查询用户
	 * 
	 * @param role
	 *            角色
	 * @return 用户列表
	 *
	 * @author 王贵源
	 */
	public List<User> findUserByRole(Role role) {
		Dao dao = dao();
		Sql sql = dao.sqls().create("find.users.by.role.name");
		sql.params().set("name", role.getName());
		List<Record> records = SqlActuator.runReport(sql, dao);
		final List<User> users = Lists.newArrayList();
		Lang.each(records, new Each<Record>() {

			@Override
			public void invoke(int index, Record record, int length) throws ExitLoop, ContinueLoop, LoopException {
				users.add(record.toEntity(dao().getEntity(User.class)));
			}
		});
		return users;
	}

	/**
	 * 查询全部用户
	 * 
	 * @return 用户列表
	 *
	 * @author 王贵源
	 */
	public List<User> listAll() {
		return queryAll();
	}

	/**
	 * 根据角色名称查询用户
	 * 
	 * @param name
	 *            角色名
	 * @return 用户列表
	 *
	 * @author 王贵源
	 */
	public List<User> findUsersByRoleName(String name) {
		Sql sql = dao().sqls().create("find.users.by.role.name");
		sql.params().set("name", name);
		sql.setCallback(Sqls.callback.entities());
		dao().execute(sql);
		return sql.getList(User.class);
	}

	/**
	 * 查询用户的角色及授权信息
	 * 
	 * @param id
	 *            用户 id
	 * @param type
	 *            用户类型
	 * @return 角色列表及当前用户的授权信息
	 *
	 * @author 王贵源
	 */
	public List<Record> findRolesWithUserPowerdInfoByUserId(int id, Type type) {
		Dao dao = dao();
		Sql sql = dao.sqls().create("find.roles.with.user.powerd.info.by.user.id");
		sql.params().set("id", id);
		sql.params().set("type", type);
		return SqlActuator.runReport(sql, dao);
	}

	/**
	 * 查询用户及权限的授权状态
	 * 
	 * @param id
	 *            用户 id
	 * @param type
	 *            用户类型
	 * @return 权限列表及用户权限授权状态
	 *
	 * @author 王贵源
	 */
	public List<Record> findPermissionsWithUserPowerdInfoByUserId(int id, Type type) {
		Dao dao = dao();
		Sql sql = dao.sqls().create("find.permissions.with.user.powered.info.by.user.id");
		sql.params().set("id", id);
		sql.params().set("type", type);
		return SqlActuator.runReport(sql, dao);
	}

}
