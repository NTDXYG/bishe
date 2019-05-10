package com.yg.bishe.dao;

import com.yg.bishe.bean.Reply;
import com.yg.bishe.bean.Reply_Topic;
import com.yg.bishe.bean.Topic;
import com.yg.bishe.bean.Topic_Reply;

import java.util.List;

public interface TopicDao {
    int getTopicCount();

    List<Topic_Reply> findAllTopics(int page, int limit);

    int addReply(Reply reply);

    Topic_Reply findTopic_ReplyById(int topic_id);

    int getReplyCount();

    List<Reply_Topic> findAllReplies(int page, int limit);

    Topic findTopicById(int id);

    int getTopicCount(String begin, String end);

    List<Topic_Reply> findAllTopics(int page, int limit, String begin, String end);

    void deleteTopicById(int id);

    int getReplyCount(String begin, String end);

    List<Reply_Topic> findAllReplies(int page, int limit, String begin, String end);

    void deleteReplyById(int id);

    int addTopic(Topic topic);
}
