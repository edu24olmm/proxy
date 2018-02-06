package com.lq.po;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.List;

/**
 * {
 * "ERRORCODE":"0",
 * "RESULT":[
 * {"port":"43617","ip":"222.85.5.118"},
 * {"port":"43569","ip":"180.122.20.108"},
 * {"port":"20443","ip":"221.230.254.73"}
 * ]}
 */
public class ResultIPsPo {

    public static void main(String[] args) {
        ResultIPsPo res = JSONObject.parseObject("{\"ERRORCODE\":\"0\",\"RESULT\":[{\"port\":\"43617\",\"ip\":\"222.85.5.118\"},{\"port\":\"43569\",\"ip\":\"180.122.20.108\"},{\"port\":\"20443\",\"ip\":\"221.230.254.73\"}]}", ResultIPsPo.class);
        System.out.println(res);
        String url = "http://api.xdaili.cn/xdaili-api//greatRecharge/getGreatIp?spiderId=80f33066216b483b9e935d982e428289&orderno=YZ2018238955QxghNP&returnType=2&count=1";
        Connection conn = Jsoup.connect(url);
        try {
            System.out.println(conn.get().text());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String ERRORCODE;
    private List<ResIp> RESULT;

    public String getERRORCODE() {
        return ERRORCODE;
    }

    public void setERRORCODE(String ERRORCODE) {
        this.ERRORCODE = ERRORCODE;
    }

    public List<ResIp> getRESULT() {
        return RESULT;
    }

    public void setRESULT(List<ResIp> RESULT) {
        this.RESULT = RESULT;
    }

    public ResultIPsPo() {
    }

    public ResultIPsPo(String ERRORCODE, List<ResIp> RESULT) {
        this.ERRORCODE = ERRORCODE;
        this.RESULT = RESULT;
    }

}