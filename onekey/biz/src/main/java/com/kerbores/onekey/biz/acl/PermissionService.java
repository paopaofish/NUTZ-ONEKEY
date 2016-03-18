package com.kerbores.onekey.biz.acl;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;

import com.kerbores.onkey.bean.acl.Permission;
import com.kerbores.onkey.bean.acl.Role;
import com.kerbores.onkey.bean.acl.User.Type;
import com.kerbores.utils.biz.BaseService;
import com.kerbores.utils.collection.Lists;
import com.kerbores.utils.db.SqlActuator;
import com.kerbores.utils.web.pager.Pager;

/**
 * 
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project syl-biz
 *
 * @file PermissionService.java
 *
 * @description 权限相关业务处理
 *
 * @time 2016年3月8日 上午11:29:37
 *
 */
public class PermissionService extends BaseService<Permission> {

	/**
	 * 根据用户查询权限
	 * 
	 * @param id
	 *            用户 id
	 * @param type
	 *            用户类型
	 * @return 权限列表
	 *
	 * @author 王贵源
	 */
	public List<Permission> findByUserId(int id, Type type) {
		final Dao dao = dao();

		final List<Permission> permissions = Lists.newArrayList();
		// 获取配置的sql
		Sql sql = dao.sqls().create("select.direct.permission.by.user.id");

		sql.params().set("userId", id);// 注入sql参数
		sql.params().set("type", type);

		List<Record> records = SqlActuator.runReport(sql, dao);// 执行sql
		Lang.each(records, new Each<Record>()// 将记录转换成对象
				{

					@Override
					public void invoke(int index, Record record, int length) throws ExitLoop, ContinueLoop, LoopException {
						permissions.add(record.toEntity(dao.getEntity(Permission.class)));
					}
				});

		return permissions;
	}

	/**
	 * 根据一组角色查询权限
	 * 
	 * @param roles
	 *            角色列表
	 * @return 权限列表
	 *
	 * @author 王贵源
	 */
	public List<Permission> findByRoles(List<Role> roles) {
		if (roles == null || roles.size() == 0) {
			return Lists.newArrayList();
		}
		final int[] temp = new int[roles.size()];
		Lang.each(roles, new Each<Role>() {

			@Override
			public void invoke(int index, Role ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				temp[index] = ele.getId();
			}
		});

		return findByRoles(temp);
	}

	/**
	 * 根据一组角色查询权限
	 * 
	 * @param roles
	 *            角色id 数组
	 * @return 权限列表
	 *
	 * @author 王贵源
	 */
	public List<Permission> findByRoles(int[] roles) {

		if (roles == null || roles.length == 0) {
			return Lists.newArrayList();
		}

		final Dao dao = dao();// 获取数据访问对象
		final List<Permission> permissions = Lists.newArrayList();
		// 获取配置的sql
		Sql sql = dao.sqls().create("find.permission.by.role");

		sql.setCondition(Cnd.where("r_id", "IN", roles));

		List<Record> records = SqlActuator.runReport(sql, dao);// 执行sql

		Lang.each(records, new Each<Record>()// 将记录转换成对象
				{

					@Override
					public void invoke(int index, Record record, int length) throws ExitLoop, ContinueLoop, LoopException {
						permissions.add(record.toEntity(dao.getEntity(Permission.class)));
					}
				});

		return permissions;
	}

	/**
	 * 根据一组角色查询权限
	 * 
	 * @param roles
	 *            角色 id 列表
	 * @return 权限列表
	 *
	 * @author 王贵源
	 */
	public List<Permission> findByRoleIds(List<Integer> roles) {
		if (roles == null || roles.size() == 0) {
			return Lists.newArrayList();
		}

		final int[] temp = new int[roles.size()];

		Lang.each(roles, new Each<Integer>() {

			@Override
			public void invoke(int index, Integer ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				temp[index] = ele;
			}
		});

		return findByRoles(temp);
	}

	/**
	 * 根据名称查询权限
	 * 
	 * @param name
	 *            权限名称
	 * @return 权限
	 *
	 * @author 王贵源
	 */
	public Permission findByName(String name) {
		return fetch(Cnd.where("name", "=", name));
	}

	/**
	 * 添加权限
	 * 
	 * @param permission
	 *            待添加权限
	 * @return 添加之后的权限<将携带数据库 id>
	 *
	 * @author 王贵源
	 */
	public Permission addPermission(Permission permission) {
		return save(permission);
	}

	/**
	 * 查询子权限
	 * 
	 * @param needPermission
	 *            上级权限
	 * @return 子权限列表
	 *
	 * @author 王贵源
	 */
	public List<Permission> findByNeedPermission(int needPermission) {
		return query(Cnd.where("needPermission", "=", needPermission));
	}

	/**
	 * 根据 url 查询
	 * 
	 * @param url
	 *            参数 url
	 * @return 权限
	 *
	 * @author 王贵源
	 */
	public Permission findByUrl(String url) {
		return fetch(Cnd.where("url", "=", url));
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 *            页码
	 * @return 权限分页对象
	 *
	 * @author 王贵源
	 */
	public Pager<Permission> findByPage(int page) {
		return searchByPage(page);
	}

	/**
	 * 关键词搜索
	 * 
	 * @param page
	 *            页码
	 * @param key
	 *            关键词
	 * @return 权限分页对象
	 *
	 * @author 王贵源
	 */
	public Pager<Permission> search(int page, String key) {
		return searchByKeyAndPage(key, page, "name","description");
	}
}
