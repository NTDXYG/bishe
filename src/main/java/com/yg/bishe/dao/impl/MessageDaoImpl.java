package com.yg.bishe.dao.impl;

import com.yg.bishe.bean.Message;
import com.yg.bishe.dao.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDaoImpl implements MessageDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int getCount() {
        int count = jdbcTemplate.queryForObject("select count(*) from message", Integer.class);
        return count;
    }

    @Override
    public List<Message> findAllMessage(int page, int limit) {
        List<Message> messages = jdbcTemplate.query("select * from message order by id desc limit ?,?" ,new Object[]{(page-1)*limit,limit}, new BeanPropertyRowMapper(Message.class));
        if (messages!=null){
            return messages;
        }else{
            return null;
        }
    }

    @Override
    public Message findMessageById(Integer id) {
        List<Message> list = jdbcTemplate.query("select * from message where id" +
                " = ?" ,new Object[]{id}, new BeanPropertyRowMapper(Message.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public void changeRead(int id) {
        jdbcTemplate.update("update message set `read` = 1 where id = ?",
                id);
    }

    @Override
    public void addMessage(String title, String content, String time) {
        jdbcTemplate.update("insert into message values(null, ?, ?, ?, 0)",
                title,content,time);
    }

    @Override
    public int getCount(int i) {
        int count = jdbcTemplate.queryForObject("select count(*) from message where `read`=" +i ,Integer.class);
        return count;
    }
}
