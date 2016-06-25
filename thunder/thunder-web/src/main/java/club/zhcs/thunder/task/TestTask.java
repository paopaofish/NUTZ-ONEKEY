package club.zhcs.thunder.task;

import org.nutz.integration.quartz.annotation.Scheduled;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project thunder-web
 *
 * @file Test.java
 *
 * @description 测试任务
 *
 * @time 2016年3月8日 上午10:51:26
 *
 */
@IocBean
@Scheduled(cron = "0 */45 * * * ? ")
public class TestTask implements Job {
	private static Log LOG = Logs.getLog(TestTask.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		LOG.debug("running...");
	}

}
