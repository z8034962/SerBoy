package com.heilan.vo;

import java.io.Serializable;
import java.util.Date;

public class FileBean implements Serializable
{  	
    private static final long serialVersionUID = -764857024878161799L;
    private String  sys_doc_id;//打包文件的主键
    private  Date exeTime;//执行时间
    private String serverID;//执行主机id
    private boolean isReset;//是否重启
    private boolean isTiming;//是否定时重启
    public String getSys_doc_id()
    {
        return sys_doc_id;
    }
    public void setSys_doc_id(String sys_doc_id)
    {
        this.sys_doc_id = sys_doc_id;
    }
    public Date getExeTime()
    {
        return exeTime;
    }
    public void setExeTime(Date exeTime)
    {
        this.exeTime = exeTime;
    }
    public String getServerID()
    {
        return serverID;
    }
    public void setServerID(String serverID)
    {
        this.serverID = serverID;
    }
    public boolean isReset()
    {
        return isReset;
    }
    public void setReset(boolean isReset)
    {
        this.isReset = isReset;
    }
    public boolean isTiming()
    {
        return isTiming;
    }
    public void setTiming(boolean isTiming)
    {
        this.isTiming = isTiming;
    }
    
}
