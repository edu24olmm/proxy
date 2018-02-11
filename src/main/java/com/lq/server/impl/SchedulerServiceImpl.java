/**
 *
 */
package com.lq.server.impl;

import com.lq.po.TaskTrigger;
import com.lq.server.SchedulerService;
import org.quartz.*;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.DailyTimeIntervalTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author wangxiaoran-ds
 */
@Component
public class SchedulerServiceImpl implements SchedulerService {

    //private final static Logger logger = Logger.getLogger(SchedulerServiceImpl.class);

    @Autowired
    private Scheduler scheduler;

    /**
     * 获取当前定时任务列表
     *
     * @return
     * @throws Exception
     */
    public List<TaskTrigger> queryTaskList() throws Exception {
        List<TaskTrigger> jtlist;
        List<String> groupNameList1 = scheduler.getJobGroupNames();
        if (groupNameList1 == null || groupNameList1.size() < 1) {
            jtlist = new ArrayList<TaskTrigger>(0);
        } else {
            jtlist = new ArrayList<TaskTrigger>(100);
            //Map<String, String> taskNameMap = XYRemoteProppertiesUtil.getPropMap("job_name.properies");//XYPropertiesUtil.getPropertiesMap("props/task_name_map.properies");
            for (String groupName : groupNameList1) {
                GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.groupEquals(groupName);
                Iterator<TriggerKey> tkIt = scheduler.getTriggerKeys(groupMatcher).iterator();
                while (tkIt.hasNext()) {
                    TriggerKey triggerKey = tkIt.next();
                    TaskTrigger taskTrigger = new TaskTrigger();
                    taskTrigger.setGroupName(triggerKey.getGroup());
                    taskTrigger.setTriggerName(triggerKey.getName());
                    Trigger trigger = scheduler.getTrigger(triggerKey);
                    taskTrigger.setJobName(trigger.getJobKey().getName());
                    if (trigger instanceof CronTrigger) {//cron
                        taskTrigger.setCronExpression(((CronTrigger) trigger).getCronExpression());
                        taskTrigger.setTriggerType(TaskTrigger.TRIGGER_TYPE_CRON);
                    } else if (trigger instanceof SimpleTrigger) {//固定间隔
                        SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
                        taskTrigger.setRepeatInterval(simpleTrigger.getRepeatInterval());
                        taskTrigger.setRepeatCount(simpleTrigger.getRepeatCount());
                        taskTrigger.setTriggeredTimes(simpleTrigger.getTimesTriggered());
                        ;
                        taskTrigger.setTriggerType(TaskTrigger.TRIGGER_TYPE_REPEAT_INTERVAL);
                    } else if (trigger instanceof DailyTimeIntervalTrigger) {//每日固定间隔
                        DailyTimeIntervalTrigger dtiTrigger = (DailyTimeIntervalTrigger) trigger;
                        taskTrigger.setRepeatInterval((long) dtiTrigger.getRepeatInterval());
                        taskTrigger.setRepeatIntervalUnit(dtiTrigger.getRepeatIntervalUnit().name());
                        taskTrigger.setRepeatCount(dtiTrigger.getRepeatCount());
                        taskTrigger.setTriggeredTimes(dtiTrigger.getTimesTriggered());
                        taskTrigger.setTriggerType(TaskTrigger.TRIGGER_TYPE_DAILY_TIME_INTERVAL);
                    } else if (trigger instanceof CalendarIntervalTrigger) {//日历固定间隔
                        CalendarIntervalTrigger ciTrigger = (CalendarIntervalTrigger) trigger;
                        taskTrigger.setRepeatInterval((long) ciTrigger.getRepeatInterval());
                        taskTrigger.setRepeatIntervalUnit(ciTrigger.getRepeatIntervalUnit().name());
                        taskTrigger.setTriggeredTimes(ciTrigger.getTimesTriggered());
                        taskTrigger.setTriggerType(TaskTrigger.TRIGGER_TYPE_CALENDAR_INTERVAL);
                    } else {
                        taskTrigger.setTriggerType(TaskTrigger.TRIGGER_TYPE_OTHER);
                    }
                    taskTrigger.setPreviousFireTime(trigger.getPreviousFireTime());
                    taskTrigger.setNextFireTime(trigger.getNextFireTime());
                    taskTrigger.setStatus(scheduler.getTriggerState(triggerKey));
//					if(taskNameMap!=null){
//	                    taskTrigger.setTaskName(taskNameMap.get(triggerKey.getName()));
//					}
                    jtlist.add(taskTrigger);
                }
            }
        }
        Collections.sort(jtlist, new Comparator<TaskTrigger>() {
            public int compare(TaskTrigger o1, TaskTrigger o2) {
                String otn1 = (o1 == null ? null : o1.getTriggerName());
                String otn2 = (o2 == null ? null : o2.getTriggerName());
                return otn1 == null ? (otn2 == null ? 0 : 1) : (otn2 == null ? 1 : otn1.compareTo(otn2));
            }
        });
        return jtlist;
    }

    /**
     * 暂停定时任务
     *
     * @param groupName
     * @param triggerName
     * @throws Exception
     */
    public void pause(TaskTrigger jobTrigger) throws Exception {
        if (jobTrigger == null || StringUtils.isEmpty(jobTrigger.getGroupName()) || StringUtils.isEmpty(jobTrigger.getTriggerName())) {
            throw new IllegalArgumentException("groupName or triggerName is empty!");
        }
        scheduler.pauseTrigger(new TriggerKey(jobTrigger.getTriggerName(), jobTrigger.getGroupName()));
    }

    /**
     * 恢复定时任务运行
     *
     * @param groupName
     * @param triggerName
     * @throws Exception
     */
    public void resume(TaskTrigger jobTrigger) throws Exception {
        if (jobTrigger == null || StringUtils.isEmpty(jobTrigger.getGroupName()) || StringUtils.isEmpty(jobTrigger.getTriggerName())) {
            throw new IllegalArgumentException("groupName or triggerName is empty!");
        }
        scheduler.resumeTrigger(new TriggerKey(jobTrigger.getTriggerName(), jobTrigger.getGroupName()));
    }

    /**
     * 立即运行定时任务
     *
     * @param groupName
     * @param jobName
     * @throws Exception
     */
    public void run(TaskTrigger jobTrigger) throws Exception {
        if (jobTrigger == null || StringUtils.isEmpty(jobTrigger.getGroupName()) || StringUtils.isEmpty(jobTrigger.getJobName())) {
            throw new IllegalArgumentException("groupName or jobName is empty!");
        }
        scheduler.triggerJob(new JobKey(jobTrigger.getJobName(), jobTrigger.getGroupName()));
    }

    /**
     * 重设定时任务
     * 目前支持 [CronTriggerImpl, SimpleTriggerImpl, DailyTimeIntervalTriggerImpl, CalendarIntervalTriggerImpl] 共四种触发器定时任务
     *
     * @param jobTrigger(groupName,triggerName,cronExpression||repeatInterval)
     * @throws Exception
     */
    public void reset(TaskTrigger jobTrigger) throws Exception {
        if (jobTrigger == null || StringUtils.isEmpty(jobTrigger.getGroupName())) {
            throw new IllegalArgumentException("groupName or triggerName is empty!");
        }
        TriggerKey triggerKey = new TriggerKey(jobTrigger.getTriggerName(), jobTrigger.getGroupName());
        Trigger oldTrigger = scheduler.getTrigger(triggerKey);
        //重置cron定时任务
        if (oldTrigger instanceof CronTriggerImpl) {
            if (StringUtils.isEmpty(jobTrigger.getTriggerName())) {
                throw new IllegalArgumentException("cronExpression is empty!");
            } else {
                ((CronTriggerImpl) oldTrigger).setCronExpression(jobTrigger.getCronExpression());
                scheduler.rescheduleJob(triggerKey, oldTrigger);
            }
        } else if (oldTrigger instanceof SimpleTriggerImpl) {//固定间隔
            if (jobTrigger.getRepeatInterval() == null || jobTrigger.getRepeatInterval() < 100L) {
                throw new IllegalArgumentException("repeatInterval is null or less than 100!");
            } else {
                SimpleTriggerImpl simpleTrigger = (SimpleTriggerImpl) oldTrigger;
                if (jobTrigger.getRepeatCount() != null && jobTrigger.getRepeatCount() > -2) {
                    simpleTrigger.setRepeatCount(jobTrigger.getRepeatCount());
                }
                simpleTrigger.setRepeatInterval(jobTrigger.getRepeatInterval().intValue());
                scheduler.rescheduleJob(triggerKey, oldTrigger);
            }
        } else if (oldTrigger instanceof DailyTimeIntervalTriggerImpl) {//每日固定间隔
            if (jobTrigger.getRepeatInterval() == null && jobTrigger.getRepeatIntervalUnit() == null && jobTrigger.getRepeatCount() == null) {
                throw new IllegalArgumentException("repeatInterval or repeatIntervalUnit or repeatCount is null");
            } else if (jobTrigger.getRepeatInterval() != null && jobTrigger.getRepeatInterval() < 1L) {
                throw new IllegalArgumentException("repeatInterval is less than 1");
            } else {
                DailyTimeIntervalTriggerImpl dtiTrigger = (DailyTimeIntervalTriggerImpl) oldTrigger;
                IntervalUnit repeatIntervalUnit = IntervalUnit.valueOf(jobTrigger.getRepeatIntervalUnit());
                if (repeatIntervalUnit != null) {
                    dtiTrigger.setRepeatIntervalUnit(repeatIntervalUnit);
                }
                if (jobTrigger.getRepeatInterval() != null) {
                    dtiTrigger.setRepeatInterval(jobTrigger.getRepeatInterval().intValue());
                }
                if (jobTrigger.getRepeatCount() != null) {
                    if (jobTrigger.getRepeatCount() < -1) {
                        jobTrigger.setRepeatCount(-1);
                    }
                    dtiTrigger.setRepeatCount(jobTrigger.getRepeatCount());
                }
                scheduler.rescheduleJob(triggerKey, dtiTrigger);
            }
        } else if (oldTrigger instanceof CalendarIntervalTriggerImpl) {//日历固定间隔
            if (jobTrigger.getRepeatInterval() == null && jobTrigger.getRepeatIntervalUnit() == null) {
                throw new IllegalArgumentException("repeatInterval or repeatIntervalUnit is null");
            } else if (jobTrigger.getRepeatInterval() != null && jobTrigger.getRepeatInterval() < 1L) {
                throw new IllegalArgumentException("repeatInterval is less than 1");
            } else {
                CalendarIntervalTriggerImpl ciTrigger = (CalendarIntervalTriggerImpl) oldTrigger;
                IntervalUnit repeatIntervalUnit = IntervalUnit.valueOf(jobTrigger.getRepeatIntervalUnit());
                if (repeatIntervalUnit != null) {
                    ciTrigger.setRepeatIntervalUnit(repeatIntervalUnit);
                }
                if (jobTrigger.getRepeatInterval() != null) {
                    ciTrigger.setRepeatInterval(jobTrigger.getRepeatInterval().intValue());
                }
                scheduler.rescheduleJob(triggerKey, ciTrigger);
            }
        } else {
            throw new IllegalArgumentException("不支持的定时器类型:" + oldTrigger.getClass());
        }
    }
}
