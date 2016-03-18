package com.kerbores.onekey.ext.cache;

import java.util.concurrent.TimeUnit;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * 
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project syl-biz
 *
 * @file PermissionChecker.java
 *
 * @description 带缓存的权限检查器
 *
 * @time 2016年3月8日 上午11:28:12
 *
 */
public class PermissionChecker {

	LoadingCache<String, Boolean> cache;
	Log log = Logs.get();

	/**
	 * 缓存单例
	 * 
	 * @return
	 *
	 * @author 王贵源
	 */
	public LoadingCache<String, Boolean> getCache() {
		if (cache == null) {
			cache = get();
		}
		return cache;
	}

	/**
	 * 检查逻辑
	 * 
	 * @return 状态
	 *
	 * @author 王贵源
	 */
	private LoadingCache<String, Boolean> get() {
		return CacheBuilder.newBuilder().maximumSize(2000).expireAfterAccess(5, TimeUnit.MINUTES).removalListener(new RemovalListener<String, Boolean>() {

			@Override
			public void onRemoval(RemovalNotification<String, Boolean> notification) {
				log.debug(notification.getKey() + " removed....");
			}
		}).build(new CacheLoader<String, Boolean>() {

			@Override
			public Boolean load(String key) throws Exception {
				log.debug(key + " loading.... ");
				return false;
			}
		});
	}

}
