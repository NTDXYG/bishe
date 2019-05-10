package com.yg.bishe.service.impl;

import com.yg.bishe.bean.Reply;
import com.yg.bishe.bean.Reply_Topic;
import com.yg.bishe.bean.Topic;
import com.yg.bishe.bean.Topic_Reply;
import com.yg.bishe.dao.TopicDao;
import com.yg.bishe.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    TopicDao topicDao;

    @Override
    public int getTopicCount() {
        return topicDao.getTopicCount();
    }

    @Override
    public List<Topic_Reply> findAllTopics(int page, int limit) {
        return topicDao.findAllTopics(page,limit);
    }

    @Override
    public int addReply(Reply reply) {
        return topicDao.addReply(reply);
    }

    @Override
    public Topic_Reply findTopic_ReplyById(int topic_id) {
        return topicDao.findTopic_ReplyById(topic_id);
    }

    @Override
    public int getReplyCount() {
        return topicDao.getReplyCount();
    }

    @Override
    public List<Reply_Topic> findAllReplies(int page, int limit) {
        return topicDao.findAllReplies(page,limit);
    }

    @Override
    public Topic findTopicById(int id) {
        return topicDao.findTopicById(id);
    }

    @Override
    public int getTopicCount(String begin, String end) {
        return topicDao.getTopicCount(begin, end);
    }

    @Override
    public List<Topic_Reply> findAllTopics(int page, int limit, String begin, String end) {
        return topicDao.findAllTopics(page, limit, begin, end);
    }

    @Override
    public void deleteTopicById(int id) {
        topicDao.deleteTopicById(id);
    }

    @Override
    public int getReplyCount(String begin, String end) {
        return topicDao.getReplyCount(begin, end);
    }

    @Override
    public List<Reply_Topic> findAllReplies(int page, int limit, String begin, String end) {
        return topicDao.findAllReplies(page, limit, begin, end);
    }

    @Override
    public void deleteReplyById(int id) {
        topicDao.deleteReplyById(id);
    }

    @Override
    public int addTopic(Topic topic) {
        return topicDao.addTopic(topic);
    }
}
