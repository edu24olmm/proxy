package com.lq.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.MultipartConfigElement;

@Configuration
@PropertySource(value = {
        "classpath:quartz.properties"})
@ImportResource()
//@Order(1)
public class SpringConfig {
    private final static Logger logger = LoggerFactory.getLogger(SpringConfig.class);
    private static Environment env;

    /**
     * 启动时注入,方便子类调用
     *
     * @param env1
     */
    @Autowired
    public void setEnv(Environment env1) {
        env = env1;
    }

    protected static Environment getEnv() {
        return env;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //// 设置文件大小限制 ,超了，页面会抛出异常信息，这时候就需要进行异常信息的处理了;
        factory.setMaxFileSize("5MB"); // KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("5MB");
        // Sets the directory location wherefiles will be stored.
        // factory.setLocation("路径地址");
        return factory.createMultipartConfig();
    }

    @Bean(name = "taskExecutor", initMethod = "initialize", destroyMethod = "destroy")
    public TaskExecutor buildTaskExecutor() {
        ThreadPoolTaskExecutor tdte = new ThreadPoolTaskExecutor();
        tdte.setCorePoolSize(10);
        tdte.setKeepAliveSeconds(300);
        tdte.setMaxPoolSize(200);
        tdte.setQueueCapacity(500);
        return tdte;
    }
}
