package com.yg.bishe.dao.impl;

import com.yg.bishe.bean.Reply;
import com.yg.bishe.bean.Reply_Topic;
import com.yg.bishe.bean.Topic;
import com.yg.bishe.bean.Topic_Reply;
import com.yg.bishe.dao.TopicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class TopicDaoImpl implements TopicDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int getTopicCount() {
        int count = jdbcTemplate.queryForObject("select count(*) from `topic`", Integer.class);
        return count;
    }

    @Override
    public List<Topic_Reply> findAllTopics(int page, int limit) {
        List<Topic> topics = jdbcTemplate.query("select * from `topic` limit ?,?", new Object[]{(page-1)*limit,limit}, new BeanPropertyRowMapper(Topic.class));
        if (topics != null) {
            List<Topic_Reply> list = new LinkedList<>();
            for (Topic topic : topics) {
                Topic_Reply topic_reply = new Topic_Reply();
                topic_reply.setTopic(topic);
                List<Reply> replies = jdbcTemplate.query("select * from `reply` where topic_id = ?",new Object[]{topic.getId()} ,new BeanPropertyRowMapper(Reply.class));
                topic_reply.setReplyList(replies);
                list.add(topic_reply);
            }
            return list;
        } else {
            return null;
        }
    }

    @Override
    public int addReply(Reply reply) {
        return jdbcTemplate.update("insert into reply values(null, ?, ?, ?, ?, ?)",
                reply.getAvatar(), reply.getNickname(), reply.getContent(), reply.getTime(), reply.getTopic_id());
    }

    @Override
    public Topic_Reply findTopic_ReplyById(int topic_id) {
        List<Topic> topics = jdbcTemplate.query("select * from topic where id" +
                " = ?" ,new Object[]{topic_id}, new BeanPropertyRowMapper(Topic.class));
        if (topics!=null && topics.size()>0){
            Topic_Reply topic_reply = new Topic_Reply();
            topic_reply.setTopic(topics.get(0));
            List<Reply> replies = jdbcTemplate.query("select * from `reply` where topic_id = ?",new Object[]{topic_id} ,new BeanPropertyRowMapper(Reply.class));
            topic_reply.setReplyList(replies);
            return topic_reply;
        }else{
            return null;
        }
    }

    @Override
    public int getReplyCount() {
        int count = jdbcTemplate.queryForObject("select count(*) from `reply`", Integer.class);
        return count;
    }

    @Override
    public List<Reply_Topic> findAllReplies(int page, int limit) {
        List<Reply> replyList = jdbcTemplate.query("select * from `reply` limit ?,?", new Object[]{(page-1)*limit,limit}, new BeanPropertyRowMapper(Reply.class));
        if (replyList != null && replyList.size() > 0) {
            List<Reply_Topic> list = new LinkedList<>();
            for (Reply reply : replyList) {
                Reply_Topic reply_topic = new Reply_Topic();
                reply_topic.setReply(reply);
                List<Topic> topics = jdbcTemplate.query("select * from `topic` where id = ?",new Object[]{reply.getTopic_id()} ,new BeanPropertyRowMapper(Topic.class));
                if (topics != null && topics.size()>0){
                    reply_topic.setTopic(topics.get(0));
                }
                list.add(reply_topic);
            }
            return list;
        } else {
            return null;
        }
    }

    @Override
    public Topic findTopicById(int id) {
        List<Topic> list = jdbcTemplate.query("select * from `topic` where id" +
                " = ?" ,new Object[]{id}, new BeanPropertyRowMapper(Topic.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public int getTopicCount(String begin, String end) {
        int count = jdbcTemplate.queryForObject("select count(*) from `topic` where `time` between ? and ?", new Object[]{begin,end}, Integer.class);
        return count;
    }

    @Override
    public List<Topic_Reply> findAllTopics(int page, int limit, String begin, String end) {
        List<Topic> topics = jdbcTemplate.query("select * from `topic` where `time`between ? and ? limit ?,? " ,new Object[]{begin,end,(page-1)*limit,limit}, new BeanPropertyRowMapper(Topic.class));
        if (topics!=null){
            List<Topic_Reply> list = new LinkedList<>();
            for (Topic topic:topics){
                Topic_Reply topic_reply = new Topic_Reply();
                topic_reply.setTopic(topic);
                list.add(topic_reply);
            }
            return list;
        }else{
            return null;
        }
    }

    @Override
    public void deleteTopicById(int id) {
        jdbcTemplate.update("DELETE from `topic` where id=?",id);
    }

    @Override
    public int getReplyCount(String begin, String end) {
        int count = jdbcTemplate.queryForObject("select count(*) from `reply` where `time` between ? and ?", new Object[]{begin,end}, Integer.class);
        return count;
    }

    @Override
    public List<Reply_Topic> findAllReplies(int page, int limit, String begin, String end) {
        List<Reply> replyList = jdbcTemplate.query("select * from `reply` where `time` between ? and ? limit ?,?", new Object[]{begin, end,(page-1)*limit,limit}, new BeanPropertyRowMapper(Reply.class));
        if (replyList != null && replyList.size() > 0) {
            List<Reply_Topic> list = new LinkedList<>();
            for (Reply reply : replyList) {
                Reply_Topic reply_topic = new Reply_Topic();
                reply_topic.setReply(reply);
                List<Topic> topics = jdbcTemplate.query("select * from `topic` where id = ?",new Object[]{reply.getTopic_id()} ,new BeanPropertyRowMapper(Topic.class));
                if (topics != null && topics.size()>0){
                    reply_topic.setTopic(topics.get(0));
                }
                list.add(reply_topic);
            }
            return list;
        } else {
            return null;
        }
    }

    @Override
    public void deleteReplyById(int id) {
        jdbcTemplate.update("DELETE from `reply` where id=?",id);
    }

    @Override
    public int addTopic(Topic topic) {
        return jdbcTemplate.update("insert into topic values(null, ?, ?, ?, ? ,?)",
                topic.getAvatar(),topic.getNickname(),topic.getContent(),topic.getImg(),topic.getTime());
    }

}
