package com.yg.bishe.dao.impl;

import com.yg.bishe.bean.EchartsData;
import com.yg.bishe.bean.Log;
import com.yg.bishe.dao.LogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogDaoImpl implements LogDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int deleteLogById(int id) {
        return jdbcTemplate.update("DELETE from log where id=?",id);
    }

    @Override
    public int addLog(Log log) {
        return jdbcTemplate.update("insert into log values(null, ?, ?, ?, ?, ?, ?)",
                log.getName(),log.getIp(),log.getOperator(),log.getLocation(),log.getMode(),log.getLogintime());
    }

    @Override
    public List<Log> findAllLog(int page, int limit) {
        List<Log> logs = jdbcTemplate.query("select * from log order by id desc limit ?,?" ,new Object[]{(page-1)*limit,limit}, new BeanPropertyRowMapper(Log.class));
        if (logs!=null){
            return logs;
        }else{
            return null;
        }
    }

    @Override
    public int getCount() {
        int count = jdbcTemplate.queryForObject("select count(*) from log", Integer.class);
        return count;
    }

    @Override
    public int getCount(String begin, String end) {
        int count = jdbcTemplate.queryForObject("select count(*) from log where logintime between ? and ?", new Object[]{begin,end}, Integer.class);
        return count;
    }

    @Override
    public List<Log> findAllLog(int page, int limit, String begin, String end) {
        List<Log> logs = jdbcTemplate.query("select * from log where logintime between ? and ? order by id desc limit ?,? " ,new Object[]{begin,end,(page-1)*limit,limit}, new BeanPropertyRowMapper(Log.class));
        if (logs!=null){
            return logs;
        }else{
            return null;
        }
    }

    @Override
    public Log findLogById(Integer id) {
        List<Log> list = jdbcTemplate.query("select * from log where id" +
                " = ?" ,new Object[]{id}, new BeanPropertyRowMapper(Log.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public int updateById(Log log) {
        return jdbcTemplate.update("update log set name = ?,ip = ?,operator = ?,location = ?,mode = ?,logintime = ? where id = ?",
                log.getName(),log.getIp(),log.getOperator(),log.getLocation(),log.getMode(),log.getLogintime(),log.getId());
    }

    @Override
    public List<EchartsData> getData() {
        List<EchartsData> list = jdbcTemplate.query("SELECT operator AS NAME,COUNT(*) AS VALUE FROM log GROUP BY operator" , new BeanPropertyRowMapper(EchartsData.class));
        if (list!=null){
            return list;
        }else{
            return null;
        }
    }

    @Override
    public List<EchartsData> getLoginData(String mode) {
        List<EchartsData> list = jdbcTemplate.query("SELECT `name`,COUNT(*) AS VALUE FROM `log` WHERE `mode`= ? GROUP BY `name`" ,
                new Object[]{mode}, new BeanPropertyRowMapper(EchartsData.class));
        if (list!=null){
            return list;
        }else{
            return null;
        }
    }
}
