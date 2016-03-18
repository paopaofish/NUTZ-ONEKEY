package com.kerbores.onekey.tasks;

import org.hyperic.sigar.Sigar;
import org.nutz.aop.interceptor.async.Async;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.kerbores.onkey.bean.apm.APMAlarm;
import com.kerbores.onkey.bean.apm.APMAlarm.Type;
import com.kerbores.sigar.gathers.CPUGather;
import com.kerbores.sigar.gathers.MemoryGather;
import com.kerbores.utils.common.Ips;

/**
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project app
 *
 * @file APMTask.java
 *
 * @description // TODO write some description of this class
 *
 * @time 2016年3月15日 上午11:54:46
 *
 */
@IocBean(name = "apmTask", fields = "dao")
public class APMTask implements Job {
	private static Log LOG = Logs.getLog(APMTask.class);
	private Dao dao;

	/**
	 * 
	 */
	public APMTask() {
	}

	public APMTask(Dao dao) {
		this.dao = dao;
	}

	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public String hostIp = Ips.hostIp();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Sigar sigar = new Sigar();
		MemoryGather memory = MemoryGather.init(sigar);
		// 内存
		double jvmUsage, ramUsage, swapUsage;
		if ((jvmUsage = memory.getJvm().getUsage()) > 60) {
			alarm(Type.MEM, String.format("内存警告:当前 JVM 内存使用率 %f,高于预警值 %f", jvmUsage, 60f));
		}
		if ((ramUsage = memory.getRam().getUsage()) > 60) {
			alarm(Type.MEM, String.format("内存警告:当前 RAM 内存使用率 %f,高于预警值 %f", ramUsage, 60f));
		}
		if ((swapUsage = memory.getSwap().getUsage()) > 30) {
			alarm(Type.MEM, String.format("内存警告:当前 SWAP 内存使用率 %f,高于预警值 %f", swapUsage, 30f));
		}

		CPUGather cpu = CPUGather.init(sigar);

		// CPU
		double cpuUsage;
		if ((cpuUsage = cpu.getTotal().getDouble("usage")) > 75) {
			alarm(Type.CPU, String.format("CPU警告:当前 CPU 使用率 %f,高于预警值 %f", cpuUsage, 75f));
		}

		// 磁盘

		// 网络
	}

	/**
	 * 
	 * @param type
	 * @param msg
	 */
	@Async
	public void alarm(Type type, String msg) {
		APMAlarm alarm = new APMAlarm();
		alarm.setType(type);
		alarm.setMsg(msg);
		alarm.setIp(hostIp);
		if (dao == null) {
			LOG.debug(alarm);
		} else {
			dao.insert(alarm);
		}
	}
}
