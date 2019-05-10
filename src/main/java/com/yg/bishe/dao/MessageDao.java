package com.yg.bishe.dao;

import com.yg.bishe.bean.Message;

import java.util.List;

public interface MessageDao {
    int getCount();

    List<Message> findAllMessage(int page,int limit);

    Message findMessageById(Integer id);

    void changeRead(int id);

    void addMessage(String title, String content, String time);

    int getCount(int i);
}
