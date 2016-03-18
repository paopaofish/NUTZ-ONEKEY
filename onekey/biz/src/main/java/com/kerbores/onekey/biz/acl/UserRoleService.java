package com.kerbores.onekey.biz.acl;

import java.util.List;

import org.nutz.dao.Cnd;

import com.kerbores.onkey.bean.acl.UserRole;
import com.kerbores.utils.biz.BaseService;

/**
 * 
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project syl-biz
 *
 * @file UserRoleService.java
 *
 * @description 用户角色相关业务
 *
 * @time 2016年3月8日 上午11:31:07
 *
 */
public class UserRoleService extends BaseService<UserRole> {

	/**
	 * 根据用户 id 查询
	 * 
	 * @param id
	 *            用户 id
	 * @return 用户角色关系列表
	 *
	 * @author 王贵源
	 */
	public List<UserRole> findByUserId(int id) {
		return query(Cnd.where("userId", "=", id));
	}

	/**
	 * 添加用户角色关系
	 * 
	 * @param ur
	 * @return
	 *
	 * @author 王贵源
	 */
	public UserRole addUserRole(UserRole ur) {
		return save(ur);
	}

	/**
	 * 根据角色 id 查询
	 * 
	 * @param id
	 *            角色 id
	 * @return 用户角色关系列表
	 *
	 * @author 王贵源
	 */
	public List<UserRole> findByRoleId(int id) {
		return query(Cnd.where("roleId", "=", id));
	}

}
