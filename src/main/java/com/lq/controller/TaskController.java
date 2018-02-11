/**
 *
 */
package com.lq.controller;

import com.lq.po.TaskTrigger;
import com.lq.server.SchedulerService;
import org.quartz.DateBuilder.IntervalUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangxiaoran-ds
 */
@Controller
@RequestMapping("/task")
public class TaskController {

    private final static Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private SchedulerService schedulerService;

    /**
     * 任务列表页面
     *
     * @return
     */
    @RequestMapping("/manage")
    public ModelAndView manage() {
        ModelAndView mav = new ModelAndView("task/task_manage");
        mav.addObject("intervalUnitList", Arrays.asList(IntervalUnit.values()));
        return mav;
    }

    /**
     * 暂停任务
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/list")
    public List<TaskTrigger> list() throws Exception {
        return this.schedulerService.queryTaskList();
    }

    /**
     * 暂停任务
     *
     * @param jobTrigger
     * @return
     */
    @ResponseBody
    @RequestMapping("/pause")
    public Map<String, Object> pause(TaskTrigger jobTrigger) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (jobTrigger == null || StringUtils.isEmpty(jobTrigger.getGroupName()) ||
                StringUtils.isEmpty(jobTrigger.getTriggerName())) {
            result.put("code", -1);
        } else {
            try {
                this.schedulerService.pause(jobTrigger);
                result.put("code", 1);
            } catch (Exception e) {
                result.put("code", -500);
                result.put("message", e.getMessage());
                logger.error("暂停任务发生异常:" + e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 恢复任务
     *
     * @param jobTrigger
     * @return
     */
    @ResponseBody
    @RequestMapping("/resume")
    public Map<String, Object> resume(TaskTrigger jobTrigger) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (jobTrigger == null || StringUtils.isEmpty(jobTrigger.getGroupName()) ||
                StringUtils.isEmpty(jobTrigger.getTriggerName())) {
            result.put("code", -1);
        } else {
            try {
                this.schedulerService.resume(jobTrigger);
                result.put("code", 1);
            } catch (Exception e) {
                result.put("code", -500);
                result.put("message", e.getMessage());
                logger.error("恢复任务发生异常:" + e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 立即运行任务
     *
     * @param jobTrigger
     * @return
     */
    @ResponseBody
    @RequestMapping("/run")
    public Map<String, Object> run(TaskTrigger jobTrigger) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (jobTrigger == null || StringUtils.isEmpty(jobTrigger.getGroupName()) ||
                StringUtils.isEmpty(jobTrigger.getJobName())) {
            result.put("code", -1);
        } else {
            try {
                this.schedulerService.run(jobTrigger);
                result.put("code", 1);
            } catch (Exception e) {
                result.put("code", -500);
                result.put("message", e.getMessage());
                logger.error("运行任务发生异常:" + e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 重新配置任务
     *
     * @param jobTrigger
     * @return
     */
    @ResponseBody
    @RequestMapping("/reset")
    public Map<String, Object> reset(TaskTrigger jobTrigger) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (jobTrigger == null || StringUtils.isEmpty(jobTrigger.getGroupName()) ||
                StringUtils.isEmpty(jobTrigger.getTriggerName())) {
            result.put("code", -1);
        } else {
            try {
                this.schedulerService.reset(jobTrigger);
                result.put("code", 1);
            } catch (Exception e) {
                result.put("code", -500);
                result.put("message", e.getMessage());
                logger.error("重新配置任务发生异常:" + e.getMessage(), e);
            }
        }
        return result;
    }
}
