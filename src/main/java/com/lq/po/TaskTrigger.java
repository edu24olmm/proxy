/**
 * 
 */
package com.lq.po;

import org.quartz.Trigger.TriggerState;

import java.util.Date;

/**
 * @author wangxiaoran-ds
 *
 */
public final class TaskTrigger {

	/**
	 * 类型: 0 其他
	 */
	public final static int TRIGGER_TYPE_OTHER = 0;

	/**
	 * 类型: 1 固定间隔时间
	 */
	public final static int TRIGGER_TYPE_REPEAT_INTERVAL = 1;

	/**
	 * 类型: 2 cron
	 */
	public final static int TRIGGER_TYPE_CRON = 2;

	/**
	 * 类型: 3 每日重复
	 */
	public final static int TRIGGER_TYPE_DAILY_TIME_INTERVAL = 3;

	/**
	 * 类型: 4 日历重复
	 */
	public final static int TRIGGER_TYPE_CALENDAR_INTERVAL = 4;

	private String triggerName;
	private String jobName;
	private String groupName;
	/**
	 * 类型:1:cron,2:间隔
	 */
	private Integer triggerType;
	/**
	 * 已执行次数(cron触发器无此变量)
	 */
	private Integer triggeredTimes;

	/**
	 * cron表达式
	 */
	private String cronExpression;

	/**
	 * 固定间隔时间
	 */
	private Long repeatInterval;
	/**
	 * 固定间隔时间单位
	 */
	private String repeatIntervalUnit;

	/**
	 * 重复运行次数
	 */
	private Integer repeatCount;

	/**
	 * 任务开始时间
	 */
	private Date startTime;

	/**
	 * 任务结束时间
	 */
	private Date endTime;
	/**
	 * 上一次执行时间
	 */
	private Date previousFireTime;
	/**
	 * 下一次执行时间(预计)
	 */
	private Date nextFireTime;
	/**
	 * 状态
	 */
	private TriggerState status;

	public final String getTriggerName() {
		return triggerName;
	}
	public final void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	public final String getJobName() {
		return jobName;
	}
	public final void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public final String getGroupName() {
		return groupName;
	}
	public final void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public final Integer getTriggerType() {
		return triggerType;
	}
	public final void setTriggerType(Integer triggerType) {
		this.triggerType = triggerType;
	}
	public final Integer getTriggeredTimes() {
		return triggeredTimes;
	}
	public final void setTriggeredTimes(Integer triggeredTimes) {
		this.triggeredTimes = triggeredTimes;
	}
	public final String getCronExpression() {
		return cronExpression;
	}
	public final void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public final Long getRepeatInterval() {
		return repeatInterval;
	}
	public final void setRepeatInterval(Long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
	public final String getRepeatIntervalUnit() {
		return repeatIntervalUnit;
	}
	public final void setRepeatIntervalUnit(String repeatIntervalUnit) {
		this.repeatIntervalUnit = repeatIntervalUnit;
	}
	public final Integer getRepeatCount() {
		return repeatCount;
	}
	public final void setRepeatCount(Integer repeatCount) {
		this.repeatCount = repeatCount;
	}
	public final Date getStartTime() {
		return startTime;
	}
	public final void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public final Date getEndTime() {
		return endTime;
	}
	public final void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public final Date getPreviousFireTime() {
		return previousFireTime;
	}
	public final void setPreviousFireTime(Date previousFireTime) {
		this.previousFireTime = previousFireTime;
	}
	public final Date getNextFireTime() {
		return nextFireTime;
	}
	public final void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	public final TriggerState getStatus() {
		return status;
	}
	public final void setStatus(TriggerState status) {
		this.status = status;
	}

	/**
	 * 触发类型名称
	 * @return
	 */
	/*public final String getTriggerTypeName() {
		String triggerTypeName;
		if(triggerType==null){
			triggerTypeName = null;
		}else{
			switch(triggerType.intValue()){
			case TRIGGER_TYPE_REPEAT_INTERVAL:
				triggerTypeName = "固定间隔";
				break;
			case TRIGGER_TYPE_CRON:
				triggerTypeName = "cron";
				break;
			case TRIGGER_TYPE_DAILY_TIME_INTERVAL:
				triggerTypeName = "每日固定间隔";
				break;
			case TRIGGER_TYPE_CALENDAR_INTERVAL:
				triggerTypeName = "日历固定间隔";
				break;
			case TRIGGER_TYPE_OTHER:
				triggerTypeName = "其他";
				break;
			default:
				triggerTypeName = null;
				break;
			}
		}
		return triggerTypeName;
	}*/

	/*public final String getStatusName() {
		String statusName;
		if(status==null){
			statusName = null;
		}else{
			switch(status){
			case NONE:
				statusName = "无";
				break;
			case NORMAL:
				statusName = "就绪";
				break;
			case BLOCKED:
				statusName = "正在运行";
				break;
			case COMPLETE:
				statusName = "完毕";
				break;
			case ERROR:
				statusName = "错误";
				break;
			case PAUSED:
				statusName = "暂停";
				break;
			default:
				statusName = null;
				break;
			}
		}
		return statusName;
	}*/
	/*public final Integer getEnumRepeatIntervalUnit() {
		return repeatIntervalUnit;
	}
	public final void setEnumRepeatIntervalUnit(Integer repeatIntervalUnit) {
		this.repeatIntervalUnit = repeatIntervalUnit;
	}*/

	private String taskName;

    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    
    /*public final static IntervalUnit translateIntervalUnit(int repeatIntervalUnit){
    	IntervalUnit iu = null;
    	IntervalUnit[] iuList = IntervalUnit.values();
    	for(int i=0;i<iuList.length;i++){
    		if(iuList[i].ordinal()==repeatIntervalUnit){
    			iu = iuList[i];
    			break;
    		}
    	}
    	return iu;
    }*/
}
