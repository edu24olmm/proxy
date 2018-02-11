/**
 *
 */
package com.lq.conf;

import com.lq.job.AbstractBaseJob;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.util.List;

/**
 * 配置quart定时任务
 * 使用quart管理定时任务,可以方便进行管理,使用spring定时任务则无接口进行管理
 *
 * @author admin
 */
@Configuration
//@Order(2)
public class SchedledConfiguration {

    // 配置中设定了
    // ① targetMethod: 指定需要定时执行scheduleInfoAction中的simpleJobTest()方法
    // ② concurrent：对于相同的JobDetail，当指定多个Trigger时, 很可能第一个job完成之前，
    // 第二个job就开始了。指定concurrent设为false，多个job不会并发运行，第二个job将不会在第一个job完成之前开始。
    // ③ cronExpression：0/10 * * * * ?表示每10秒执行一次，具体可参考附表。
    // ④ triggers：通过再添加其他的ref元素可在list中放置多个触发器。

    /**
     * @param xjob
     * @return
     */
    @Bean(name = "runOneTimeJob")
    public MethodInvokingJobDetailFactoryBean runOneTimeJob(@Qualifier("runOneTimeService") AbstractBaseJob xjob) {
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setTargetObject(xjob);
        bean.setTargetMethod("run");
        bean.setConcurrent(true);
        return bean;
    }

    /**
     * @param detailFactoryBean
     * @return
     */
    @Bean
    public SimpleTriggerFactoryBean runOneTimeJobTrigger(@Qualifier("runOneTimeJob") MethodInvokingJobDetailFactoryBean detailFactoryBean) {
        SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
        simpleTriggerFactoryBean.setJobDetail(detailFactoryBean.getObject());
        simpleTriggerFactoryBean.setStartDelay(500);
        simpleTriggerFactoryBean.setRepeatInterval(0);
        simpleTriggerFactoryBean.setRepeatCount(0);
        return simpleTriggerFactoryBean;
    }


    /**
     * @param xjob
     * @return
     */
    @Bean(name = "checkJob")
    public MethodInvokingJobDetailFactoryBean checkJob(@Qualifier("checkJobService") AbstractBaseJob xjob) {
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setTargetObject(xjob);
        bean.setTargetMethod("run");
        bean.setConcurrent(true);
        return bean;
    }

    /**
     * @param detailFactoryBean
     * @return
     */
    @Bean
    public SimpleTriggerFactoryBean checkJobTrigger(@Qualifier("checkJob") MethodInvokingJobDetailFactoryBean detailFactoryBean) {
        SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
        simpleTriggerFactoryBean.setJobDetail(detailFactoryBean.getObject());
        simpleTriggerFactoryBean.setStartDelay(1000);
        simpleTriggerFactoryBean.setRepeatInterval(3000);
        return simpleTriggerFactoryBean;
    }

    //-----------------------------------------------------------------


    /**
     * !!! 这个bean需要在所有计划任务配置Bean的下面,如此才能保证所有计划任务被执行
     * 配置计划任务管理容器
     *
     * @param triggers
     * @return
     */
    @Autowired
    @Bean(name = "jobScheduler")
    public SchedulerFactoryBean schedulerFactory(@Autowired List<Trigger> triggerList) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        if (triggerList.size() > 0) {
            bean.setTriggers(triggerList.toArray(new Trigger[triggerList.size()]));
        }
        return bean;
    }

}
