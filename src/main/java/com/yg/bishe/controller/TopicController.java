package com.yg.bishe.controller;

import com.hengyi.dzfilter.utils.TextUtils;
import com.yg.bishe.bean.*;
import com.yg.bishe.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "话题接口")
public class TopicController {
    private static final Logger logger = LoggerFactory.getLogger(TopicController.class);
    @Autowired
    TopicService topicService;

    @GetMapping("/wx/getTopics")
    @ResponseBody
    @ApiOperation("获取话题列表的api接口")
    public ResBody getActivities(@ApiParam("当前页码") @RequestParam int page,
                                 @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = topicService.getTopicCount();
        List<Topic_Reply> list = topicService.findAllTopics(page,limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    @GetMapping("/wx/getReplies")
    @ResponseBody
    @ApiOperation("获取评论列表的api接口")
    public ResBody getReplies(@ApiParam("当前页码") @RequestParam int page,
                              @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = topicService.getReplyCount();
        List<Reply_Topic> list = topicService.findAllReplies(page,limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    @GetMapping("/wx/getTopicById")
    @ResponseBody
    @ApiOperation("获取具体话题的api接口")
    public Topic_Reply getTopicById(int topic_id) {
        Topic_Reply topic_reply = topicService.findTopic_ReplyById(topic_id);
        return topic_reply;
    }

    @ApiOperation("添加评论")
    @ResponseBody
    @PostMapping(value = "/wx/sendReply", produces = "application/json;charset=UTF-8")
    public ResBody sendReply(@RequestBody Map<String, Object> params) {
        logger.info(params.toString());
        ResBody resBody = new ResBody();
        String content = TextUtils.filter(params.get("content").toString());
        String time = params.get("time").toString();
        String nickname = params.get("nickname").toString();
        String avatar = params.get("avatar").toString();
        int topic_id = (int) params.get("topic_id");
        Reply reply = new Reply();
        reply.setAvatar(avatar);
        reply.setContent(content);
        reply.setNickname(nickname);
        reply.setTime(time);
        reply.setTopic_id(topic_id);
        int i = topicService.addReply(reply);
        if (i == 1) {
            resBody.setCode(200);
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("添加话题")
    @ResponseBody
    @PostMapping(value = "/addTopicByAjax", produces = "application/json;charset=UTF-8")
    public ResBody addTopicByAjax(@RequestBody Map<String, Object> params) {
        ResBody resBody = new ResBody();
        String nickname = params.get("nickname").toString();
        String avatar = params.get("avatar").toString();
        String time = params.get("time").toString();
        String img = params.get("img").toString();
        String content = params.get("content").toString();
        Topic topic = new Topic(0,avatar,nickname,content,img,time);
        int i = topicService.addTopic(topic);
        if (i == 1) {
            resBody.setCode(200);
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @GetMapping("/getTopicsByDate")
    @ResponseBody
    @ApiOperation("获取条件话题的table接口")
    public ResBody getTopicsByDate(@ApiParam("当前页码") @RequestParam int page,
                                 @ApiParam("每页数据量") @RequestParam int limit,
                                 @ApiParam("开始时间") @RequestParam String begin,
                                 @ApiParam("结束时间") @RequestParam String end) {
        ResBody resBody = new ResBody();
        int count = topicService.getTopicCount(begin, end);
        List<Topic_Reply> topics = topicService.findAllTopics(page, limit, begin, end);
        resBody.setCount(count);
        resBody.setData(topics);
        resBody.setCode(0);
        return resBody;
    }

    @GetMapping("/getRepliesByDate")
    @ResponseBody
    @ApiOperation("获取条件评论的table接口")
    public ResBody getRepliesByDate(@ApiParam("当前页码") @RequestParam int page,
                                   @ApiParam("每页数据量") @RequestParam int limit,
                                   @ApiParam("开始时间") @RequestParam String begin,
                                   @ApiParam("结束时间") @RequestParam String end) {
        ResBody resBody = new ResBody();
        int count = topicService.getReplyCount(begin, end);
        List<Reply_Topic> topics = topicService.findAllReplies(page, limit, begin, end);
        resBody.setCount(count);
        resBody.setData(topics);
        resBody.setCode(0);
        return resBody;
    }

    @DeleteMapping("/delTopic/{id}")
    @ResponseBody
    @ApiOperation("删除话题")
    public ResBody delTopic(@PathVariable("id") @ApiParam("话题ID") String id) {
        ResBody resBody = new ResBody();
        String[] s = id.split(",");
        for (int i = 0; i < s.length; i++) {
            topicService.deleteTopicById(Integer.parseInt(s[i]));
        }
        resBody.setCode(200);
        return resBody;
    }

    @DeleteMapping("/delReply/{id}")
    @ResponseBody
    @ApiOperation("删除评论")
    public ResBody delReply(@PathVariable("id") @ApiParam("评论ID") String id) {
        ResBody resBody = new ResBody();
        String[] s = id.split(",");
        for (int i = 0; i < s.length; i++) {
            topicService.deleteReplyById(Integer.parseInt(s[i]));
        }
        resBody.setCode(200);
        return resBody;
    }

    @GetMapping("/addTopic")
    @ApiOperation("添加话题页面")
    public String addTopic(){
        return "app/topic/topic/listform";
    }

    @GetMapping("/moreTopic/{id}")
    @ApiOperation("查看话题详情页面")
    public String moreAdmin(@PathVariable("id")@ApiParam("话题ID") int id, Model model){
        Topic topic = topicService.findTopicById(id);
        model.addAttribute("topic",topic);
        return "app/topic/topic/listform";
    }
}
