package com.kerbores.onekey.biz.acl;

import java.util.List;

import org.nutz.dao.Cnd;

import com.kerbores.onkey.bean.acl.UserPermission;
import com.kerbores.utils.biz.BaseService;

/**
 * 
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project syl-biz
 *
 * @file UserPermissionService.java
 *
 * @description 用户权限相关业务
 *
 * @time 2016年3月8日 上午11:30:46
 *
 */
public class UserPermissionService extends BaseService<UserPermission> {

	/**
	 * 根据权限 id 查询
	 * 
	 * @param id
	 *            权限 id
	 * @return 用户权限关系列表
	 *
	 * @author 王贵源
	 */
	public List<UserPermission> findByPermissionId(int id) {
		return query(Cnd.where("permissionId", "=", id));
	}
}
