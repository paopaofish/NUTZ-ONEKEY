package com.kerbores.onkey.bean.apm;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.lang.Times;
import org.nutz.lang.random.R;

import com.kerbores.utils.db.data.Entity;

/**
 * @author Kerbores(kerbores@gmail.com)
 *
 * @project bean
 *
 * @file APMAlarm.java
 *
 * @description 应用性能告警
 *
 * @time 2016年3月15日 下午3:30:08
 *
 */
@Table("tbd_apm_alarm")
@Comment("性能告警表")
public class APMAlarm extends Entity {

	@Name
	@Column("alarm_code")
	@Comment("报警信息编号")
	private String code = R.UU32();

	@Column("alarm_type")
	@Comment("报警类型")
	private Type type;

	@Column("alarm_time")
	@Comment("报警时间")
	private Date alarmTime = Times.now();

	@Column("alarm_msg")
	@Comment("报警消息")
	@ColDefine(width = 250)
	private String msg;

	@Column("alarm_ip")
	@Comment("报警 ip")
	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Date getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static enum Type {
		SLOW, MEM, DISK, CPU, NETWORK
	}

}
