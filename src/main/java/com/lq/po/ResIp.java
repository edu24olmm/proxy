package com.lq.po;


public class ResIp {
    private String port;
    private String ip;
    private String time;
    private String anony;
    private Long createTime;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAnony() {
        return anony;
    }

    public void setAnony(String anony) {
        this.anony = anony;
    }

    public String getPort() {
        return port;
    }

    public ResIp() {

    }

    public ResIp(String port, String ip) {
        this.port = port;
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}