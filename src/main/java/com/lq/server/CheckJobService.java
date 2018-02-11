package com.lq.server;

import com.lq.conf.RedisUtil;
import com.lq.job.AbstractBaseJob;
import com.lq.po.ResIp;
import com.lq.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("checkJobService")
public class CheckJobService extends AbstractBaseJob {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    HttpProxyServer httpProxyServer;


    @Override
    public void doWork() {
        ResIp ip = new ResIp();
        ip.setIp(redisUtil.get("ip"));
        ip.setPort(redisUtil.get("port"));
        Boolean boo = HttpUtils.chechISTimeOut(ip);
        if (!boo) {
            httpProxyServer.getIp();
        }
    }
}
