package com.kerbores.onekey.module;

import java.io.IOException;
import java.util.List;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.lang.Streams;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import com.kerbores.nutz.module.base.AbstractBaseModule;
import com.kerbores.qiniu.QiNiuUploader;
import com.kerbores.utils.collection.Lists;
import com.kerbores.utils.entries.Result;

/**
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project app
 *
 * @file FileUploadModule.java
 *
 * @description 文件上传下载
 *
 * @time 2016年3月24日 下午1:46:46
 *
 */
@At
public class FileModule extends AbstractBaseModule {

	@Inject
	QiNiuUploader uploader;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kerbores.nutz.module.base.AbstractBaseModule#_getNameSpace()
	 */
	@Override
	public String _getNameSpace() {
		return null;
	}

	/**
	 * ajax 文件上传
	 * 
	 * @param files
	 * @return
	 * @throws IOException
	 */
	@At
	@AdaptBy(type = UploadAdaptor.class, args = { "${app.root}/WEB-INF/tmp" })
	public Result upload(@Param("files") TempFile[] files) throws IOException {
		List<NutMap> data = Lists.newArrayList();
		for (TempFile tempFile : files) {
			NutMap temp = NutMap.NEW();
			String key = uploader.upload(Streams.readBytes(tempFile.getInputStream()));
			temp.put("key", key);
			temp.put("url", uploader.privateUrl(key));
			temp.put("name", tempFile.getName());
			temp.put("localName", tempFile.getSubmittedFileName());
			temp.put("size", tempFile.getSize());
			temp.put("type", tempFile.getContentType());
			data.add(temp);
		}
		return Result.success().addData("data", data);
	}

	public static void main(String[] args) throws IOException {
		String host = "www.baidu.com";
		Process p = Runtime.getRuntime().exec("ping -c 1 " + host);

		System.err.println(Streams.read(Streams.utf8r(p.getInputStream())));
	}

}
