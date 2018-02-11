package com.lq.server;


import com.lq.po.TaskTrigger;

import java.util.List;

public interface SchedulerService {

    /**
     * 获取当前定时任务列表
     *
     * @return
     * @throws Exception
     */
    public List<TaskTrigger> queryTaskList() throws Exception;

    /**
     * 暂停定时任务
     *
     * @throws Exception
     */
    public void pause(TaskTrigger jobTrigger) throws Exception;

    /**
     * 恢复定时任务运行
     *
     * @throws Exception
     */
    public void resume(TaskTrigger jobTrigger) throws Exception;

    /**
     * 立即运行定时任务
     *
     * @throws Exception
     */
    public void run(TaskTrigger jobTrigger) throws Exception;

    /**
     * 重设定时任务
     * 目前支持 [CronTriggerImpl, SimpleTriggerImpl, DailyTimeIntervalTriggerImpl, CalendarIntervalTriggerImpl] 共四种触发器定时任务
     *
     * @param jobTrigger(groupName,triggerName,cronExpression||repeatInterval)
     * @throws Exception
     */
    public void reset(TaskTrigger jobTrigger) throws Exception;

}
