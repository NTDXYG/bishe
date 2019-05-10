package com.yg.bishe.service.impl;

import com.yg.bishe.bean.Message;
import com.yg.bishe.dao.MessageDao;
import com.yg.bishe.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageDao messageDao;

    @Override
    public int getCount() {
        return messageDao.getCount();
    }

    @Override
    public List<Message> findAllMessage(int page, int limit) {
        return messageDao.findAllMessage(page, limit);
    }

    @Override
    public Message findMessageById(Integer id) {
        return messageDao.findMessageById(id);
    }

    @Override
    public void changeRead(int id) {
        messageDao.changeRead(id);
    }

    @Override
    public void addMessage(String title, String content, String time) {
        messageDao.addMessage(title,content,time);
    }

    @Override
    public int getCount(int i) {
        return messageDao.getCount(i);
    }
}
