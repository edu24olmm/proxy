package com.lq.server;

import com.lq.job.AbstractBaseJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("runOneTimeService")
public class RunOneTimeService extends AbstractBaseJob {


    @Autowired
    HttpProxyServer httpProxyServer;


    @Override
    public void doWork() {
        try {
            httpProxyServer.runserver2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
