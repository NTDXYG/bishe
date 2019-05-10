package com.yg.bishe.dao.impl;

import com.yg.bishe.bean.File;
import com.yg.bishe.bean.Log;
import com.yg.bishe.dao.UploadDao;
import com.yg.bishe.util.FastDFSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UploadDaoImpl implements UploadDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int addFile(String url,String name) {
        return jdbcTemplate.update("insert into file values(null, ?, ?)",
                url,name);
    }

    @Override
    public List<File> getFile(int page, int limit) {
        List<File> list = jdbcTemplate.query("select * from file limit ?,?" ,new Object[]{(page-1)*limit,limit}, new BeanPropertyRowMapper(File.class));
        if (list!=null){
            return list;
        }else{
            return null;
        }
    }

    @Override
    public int deleteById(int id) {
        File file;
        List<File> list = jdbcTemplate.query("select * from file where id" +
                " = ?" ,new Object[]{id}, new BeanPropertyRowMapper(File.class));
        if (list!=null && list.size()>0){
            file = list.get(0);
        }else{
            return 0;
        }
        FastDFSClient.deleteFile(file.getUrl());
        return jdbcTemplate.update("DELETE from file where id=?",id);
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject("select count(*) from file", Integer.class);
    }

}
