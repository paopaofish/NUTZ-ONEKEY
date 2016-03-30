package com.kerbores.onekey.test;

import java.io.File;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.FileResourceLoader;

/**
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project bean
 *
 * @file T.java
 *
 * @description // TODO write some description of this class
 *
 * @time 2016年3月30日 下午8:05:22
 *
 */
public class T {
	public static void main(String[] args) throws Exception {

		String root = System.getProperty("user.dir") + File.separator + "template";
		FileResourceLoader resourceLoader = new FileResourceLoader(root, "utf-8");
		Configuration cfg = Configuration.defaultConfiguration();
		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
		Template t = gt.getTemplate("/s01/hello.txt");
		String str = t.render();
		System.out.println(str);

	}
}
