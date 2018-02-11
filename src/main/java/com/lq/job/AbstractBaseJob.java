package com.lq.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础job
 * @author admin
 *
 */
public abstract class AbstractBaseJob {

	private final static Logger logger = LoggerFactory.getLogger(AbstractBaseJob.class);

	public void run(){
		final String jobClassName = "job["+this.getClass()+"]";
		try{
			this.doWork();
		}catch(Exception e){
			logger.error(jobClassName+":执行失败,参考信息"+e.getMessage(), e);
		}
	}

	public abstract void doWork();
}
