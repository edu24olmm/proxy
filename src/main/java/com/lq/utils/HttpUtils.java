package com.lq.utils;

import com.alibaba.fastjson.JSONObject;
import com.lq.po.ResIp;
import com.lq.po.ResultIPsPagePo;
import com.lq.po.ResultIPsPo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
                LOGGER.debug("这个ip过期了");
                return false;
            } else {
                LOGGER.debug("这个ip可以使用,响应时间为" + resultIPsPo.getRESULT().get(0).getTime());
                return true;
            }
        } catch (Exception e) {
            LOGGER.debug("检查ip是否过期,异常" + e.toString());
            return false;
        }
    }

    public static ResultIPsPo getIp() {
        LOGGER.debug("getFeeIp......begin.....");
        String feeIpUrl = "http://www.xdaili.cn/ipagent//freeip/getFreeIps?page=1&rows=10";
        Connection feeConn = Jsoup.connect(feeIpUrl);
        ResultIPsPo resultIPsPo = new ResultIPsPo();
        try {
            String feeRes = feeConn.get().text();
            LOGGER.debug(feeRes);
            ResultIPsPagePo feePagePo = JSONObject.parseObject(feeRes, ResultIPsPagePo.class);
            ResultIPsPo feePo = JSONObject.parseObject(feePagePo.getRESULT(), ResultIPsPo.class);
            if (feePagePo != null && feePagePo.getERRORCODE().equalsIgnoreCase("0") && feePo != null) {
                List<ResIp> feeResList = feePo.getRows();
                for (int i = 0; i < feeResList.size(); i++) {
                    if (chechISTimeOut(feeResList.get(i))) {
                        ResIp resIp = feeResList.get(i);
                        List<ResIp> resIpList = new ArrayList<>();
                        resIpList.add(resIp);
                        resultIPsPo.setRESULT(resIpList);
                        return resultIPsPo;
                    }
                }

            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        LOGGER.debug("getFeeIp...end,res..is null,get payMoney ip...........");
        String url = "http://api.xdaili.cn/xdaili-api//greatRecharge/getGreatIp?spiderId=80f33066216b483b9e935d982e428289&orderno=YZ2018238955QxghNP&returnType=2&count=1";
        Connection conn = Jsoup.connect(url);
        try {
            String res = conn.get().text();
            LOGGER.debug(res);
            return JSONObject.parseObject(res, ResultIPsPo.class);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return new ResultIPsPo();
        }
    }
}
