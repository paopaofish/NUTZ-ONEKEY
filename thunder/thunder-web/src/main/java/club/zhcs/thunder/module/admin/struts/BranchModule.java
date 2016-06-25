package club.zhcs.thunder.module.admin.struts;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import club.zhcs.thunder.aop.SystemLog;
import club.zhcs.thunder.bean.struts.Branch;
import club.zhcs.thunder.biz.acl.UserService;
import club.zhcs.thunder.biz.struts.BranchService;
import club.zhcs.titans.nutz.module.base.AbstractBaseModule;
import club.zhcs.titans.utils.db.Pager;
import club.zhcs.titans.utils.db.Result;

/**
 * 
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project thunder-web
 *
 * @file BranchModule.java
 *
 * @description 分支机构
 *
 * @time 2016年5月11日 上午10:41:20
 *
 */
@At("branch")
public class BranchModule extends AbstractBaseModule {

	@Inject
	BranchService branchService;

	@Inject
	UserService userService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kerbores.nutz.module.base.AbstractBaseModule#_getNameSpace()
	 */
	@Override
	public String _getNameSpace() {
		return "branch";
	}

	@At
	@Ok("beetl:pages/admin/struts/branch/list.html")
	@RequiresRoles("admin")
	@SystemLog(module = "分支机构", methods = "机构列表")
	public Result list(@Param(value = "page", df = "1") int page) {
		page = _fixPage(page);
		Pager<Branch> pager = branchService.listByPage(page, Cnd.where("parentId", "=", 0));
		pager.setUrl(_base() + "/branch/list");
		return Result.success().addData("pager", pager);
	}

	@At
	@GET
	@Ok("beetl:pages/admin/struts/branch/add_edit.html")
	@RequiresRoles("admin")
	public Result add() {
		return Result.success().addData("users", userService.queryAll());
	}

	@At("/edit/*")
	@GET
	@Ok("beetl:pages/admin/struts/branch/add_edit.html")
	@RequiresRoles("admin")
	public Result edit(int id) {
		Branch branch = branchService.fetch(id);
		return Result.success().addData("users", userService.queryAll()).addData("branch", branch).addData("parent", branchService.fetch(branch.getParentId()));
	}

	@At
	@POST
	@RequiresRoles("admin")
	public Result add(@Param("..") Branch branch) {
		return branchService.save(branch) == null ? Result.fail("添加失败") : Result.success().addData("branch", branch);
	}

	@At
	@POST
	@RequiresRoles("admin")
	public Result edit(@Param("..") Branch branch) {

		return branchService.update(branch) != 1 ? Result.fail("更新失败") : Result.success().addData("branch", branch);
	}

	@At
	@Ok("beetl:pages/admin/struts/branch/selector.html")
	@RequiresRoles("admin")
	public Result selector() {
		return Result.success().addData("topBranchs", Json.toJson(branchService.loadTop()));
	}

	@At
	@RequiresRoles("admin")
	public List<NutMap> nodes(@Param("id") int id) {
		return branchService.nodes(id);
	}

	@At("/delete/*")
	@POST
	@RequiresRoles("admin")
	public Result delete(int id) {
		return branchService.delete(id) == 1 ? Result.success() : Result.fail("删除失败!");
	}

}
