package com.kerbores.onekey.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.nutz.json.Json;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.nutz.lang.util.NutMap;

import com.kerbores.onkey.bean.acl.Permission;

/**
 * 
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project syl-bean
 *
 * @file MenuLoadTest.java
 *
 * @description 菜单加载测试
 *
 * @time 2016年3月8日 上午10:59:28
 *
 */
public class MenuLoadTest {

	@Test
	public void menu() {
		String info = Files.read("conf/menu.js");
		List<Map> list = (List) Json.fromJson(info);
		Lang.each(list, new Each<Map>() {

			@Override
			public void invoke(int index, Map map, int length) throws ExitLoop, ContinueLoop, LoopException {
				NutMap data = NutMap.WRAP(map);
				save(data, null);
			}
		});
	}

	/**
	 * 
	 * @param data
	 * @param pName
	 *
	 * @author 王贵源
	 */
	protected void save(NutMap data, String pName) {
		if (hasSub(data)) {
			final Permission p = Lang.map2Object(data, Permission.class);
			p.setNeedPermission(pName);
			p.setInstalled(true);
			System.err.println(Json.toJson(p));
			// 获取 sub 迭代 递归
			Lang.each(data.getList("sub", NutMap.class), new Each<NutMap>() {

				@Override
				public void invoke(int index, NutMap ele, int length) throws ExitLoop, ContinueLoop, LoopException {
					save(ele, p.getName());
				}
			});

		} else {
			Permission p = Lang.map2Object(data, Permission.class);
			p.setNeedPermission(pName);
			p.setInstalled(true);
			System.err.println(Json.toJson(p));
		}
	}

	/**
	 * 
	 * @param data
	 * @return
	 *
	 * @author 王贵源
	 */
	protected boolean hasSub(NutMap data) {
		return data.get("sub") != null && !data.getList("sub", Object.class).isEmpty();
	}
}
