package com.lq.utils;

import com.alibaba.fastjson.JSONObject;
import com.lq.po.ResIp;
import com.lq.po.ResultIPsPo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 检查ip是否过期
     *
     * @param ip
     * @return
     */
    public static Boolean chechISTimeOut(ResIp ip) {
        String url = "http://www.xdaili.cn/ipagent//checkIp/ipList?ip_ports%5B%5D=" + ip.getIp() + "%3A" + ip.getPort();
        Connection conn = Jsoup.connect(url);
        try {
            String res = conn.get().text();
            ResultIPsPo resultIPsPo = JSONObject.parseObject(res, ResultIPsPo.class);
            if (resultIPsPo.getRESULT().get(0).getTime() == null) {
                LOGGER.info("这个ip过期了");
                return false;
            } else {
                LOGGER.info("这个ip可以使用,响应时间为" + resultIPsPo.getRESULT().get(0).getTime());
                return true;
            }
        } catch (Exception e) {
            LOGGER.info("检查ip是否过期,异常" + e.toString());
            return false;
        }
    }

    public static ResultIPsPo getIp() {
        LOGGER.info("initIP...........");
        String url = "http://api.xdaili.cn/xdaili-api//greatRecharge/getGreatIp?spiderId=80f33066216b483b9e935d982e428289&orderno=YZ2018238955QxghNP&returnType=2&count=1";
        Connection conn = Jsoup.connect(url);
        try {
            String res = conn.get().text();
            LOGGER.info(res);
            return JSONObject.parseObject(res, ResultIPsPo.class);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return new ResultIPsPo();
        }
    }
}
