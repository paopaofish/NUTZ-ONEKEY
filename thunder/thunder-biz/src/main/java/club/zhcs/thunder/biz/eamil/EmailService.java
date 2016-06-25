package club.zhcs.thunder.biz.eamil;

import java.io.IOException;

import org.apache.commons.mail.HtmlEmail;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.nutz.aop.interceptor.async.Async;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.util.Callback;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import club.zhcs.thunder.bean.apm.APMAlarm;
import club.zhcs.titans.utils.db.Result;

/**
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project 积分商城业务
 *
 * @file EmailService.java
 *
 * @description // TODO write some description of this class
 *
 * @time 2016年3月30日 下午2:18:08
 *
 */
@IocBean
public class EmailService {

	Log log = Logs.get();

	@Inject("refer:$ioc")
	protected Ioc ioc;

	/**
	 * 发送邮件
	 * 
	 * @param to
	 *            目标
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @return
	 */
	public Result send(String subject, String content, String... to) {
		try {
			HtmlEmail email = ioc.get(HtmlEmail.class);
			email.setSubject(subject);
			email.setHtmlMsg(content);
			email.addTo(to);
			email.buildMimeMessage();
			email.sendMimeMessage();
			return Result.success();
		} catch (Exception e) {
			log.fatal(e);
		}
		return Result.fail("发送失败!");
	}

	/**
	 * 发送报警邮件
	 * 
	 * @param alarm
	 *            报警对象
	 * @param to
	 *            邮件目标
	 * @return
	 */
	public Result sendAlarm(APMAlarm alarm, String... to) {
		String html = null;
		try {
			html = genHtmlAlarm(alarm);
		} catch (IOException e) {
			log.fatal(e);
			return Result.fail("生成邮件内容失败!");
		}
		return send(alarm.getTitle(), html, to);
	}

	/**
	 * 生成 alarm 邮件内容
	 * 
	 * @param alarm
	 * @return
	 * @throws IOException
	 */
	private String genHtmlAlarm(APMAlarm alarm) throws IOException {

		StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
		Configuration cfg = Configuration.defaultConfiguration();
		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
		Template t = gt.getTemplate(Files.read(getClass().getPackage().getName().replaceAll("[.]", "/") + "/alarm.html"));
		t.binding("alarm", alarm);

		return t.render();
	}

	/**
	 * 异步发送邮件
	 * 
	 * @param to
	 * @param subject
	 * @param html
	 * @param callback
	 */
	@Async
	public void sendAsync(String to, String subject, String html, Callback<Result> callback) {
		Result re = this.send(to, subject, html);
		if (callback != null)
			callback.invoke(re);
	}

}
