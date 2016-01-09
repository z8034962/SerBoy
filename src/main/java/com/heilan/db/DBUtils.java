package com.heilan.db;

import org.springframework.jdbc.core.JdbcTemplate;

import com.heilan.context.ContextHolder;

public class DBUtils
{

    public static JdbcTemplate getJdbcTemplate()
    {
        return ContextHolder.getContext().getBean(JdbcTemplate.class);
    }
    
    public static String getSeq()
    {
        return  getJdbcTemplate().queryForObject("select sys_doc_id.nextval from dual",String.class);
    }
    
    public static String getIpByCode(String code)
    {
        return getJdbcTemplate().queryForObject("select IP from kf_server where code=?", String.class,code);
    }
    
    public static String getTempPathByCode(String code)
    {
        return getJdbcTemplate().queryForObject("select TEMP_PATH from kf_server where code=?", String.class,code);
    }
    
    public static String getSrcPathByCode(String code)
    {
        return getJdbcTemplate().queryForObject("select SRC_PATH from kf_server where code=?", String.class,code);
    }
    
}
