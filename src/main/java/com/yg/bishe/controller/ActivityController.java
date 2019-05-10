package com.yg.bishe.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yg.bishe.bean.*;
import com.yg.bishe.dao.MessageDao;
import com.yg.bishe.service.ActivityService;
import com.yg.bishe.service.UserService;
import com.yg.bishe.util.DateUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {
    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    ActivityService activityService;
    @Autowired
    UserService userService;
    @Autowired
    MessageDao messageService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @ApiOperation("获取角色列表")
    @ResponseBody
    @GetMapping(value = "/activity/selectActivityList")
    public List<Activity> selectRoleList() {
        List<Activity> list = activityService.getActivityList();
        return list;
    }

    @GetMapping("/wx/getTags")
    @ResponseBody
    @ApiOperation("微信获取标签列表的api接口")
    public List getTags() {
        List list = activityService.findTags();
        return list;
    }

    @ResponseBody
    @ApiOperation("热门推荐更新")
    @PostMapping(value = "/updateHotActivity", produces = "application/json;charset=UTF-8")
    public ResBody loginByPhone(@RequestParam String title,
                                @RequestParam String img,
                                @RequestParam String begin,
                                @RequestParam String end,
                                @RequestParam String local,
                                @RequestParam String people,
                                @RequestParam String tag,
                                @RequestParam String content){
        ResBody resBody = new ResBody();
        Activity activity = new Activity();
        activity.setTitle(title);
        activity.setBegin(begin);
        activity.setEnd(end);
        activity.setContent(content);
        activity.setImg(img);
        activity.setLocal(local);
        activity.setPeople(Integer.parseInt(people));
        int i = activityService.updateHotActivity(activity,tag);
        if (i==1){
            resBody.setCode(200);
        }else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("申请活动")
    @ResponseBody
    @GetMapping(value = "/addActivityByWx", produces = "application/json;charset=UTF-8")
    public ResBody addActivityByWx(String uuid,String data) {
        ResBody resBody = new ResBody();
        System.out.println(data);
        JSONObject jsonObject = JSON.parseObject(data);
        String begin_time = jsonObject.getString("begin_time");
        String begin_date = jsonObject.getString("begin_date");
        String begin = begin_date+" "+begin_time;
        int category = Integer.parseInt(jsonObject.getString("category"));
        String content = jsonObject.getString("content");
        String end_time = jsonObject.getString("end_time");
        String end_date = jsonObject.getString("end_date");
        String end = end_date+" "+end_time;
        String img = jsonObject.getString("img");
        String local = jsonObject.getString("local");
        int people = Integer.parseInt(jsonObject.getString("people"));
        String tags = jsonObject.getString("tag");
        String title = jsonObject.getString("title");
        User user = userService.findUserById(uuid);
        Activity activity = new Activity(0,title,content,img,begin,end,people,local,user.getNickname(),0,0,category);
        int i = activityService.addActivityAndTagAndUser(activity,tags,uuid);
        if (i == 1) {
            resBody.setCode(200);
            messageService.addMessage("您有新的活动需要审核","您有新的活动需要审核,请注意即时查看。", DateUtil.getDate());
            rabbitTemplate.convertAndSend("exchange.direct","user","new Activity");
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @GetMapping("/tag/getTags")
    @ResponseBody
    @ApiOperation("获取标签分类的table接口")
    public ResBody getTags(@ApiParam("当前页码") @RequestParam int page,
                           @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = activityService.getTagCount();
        List<Tag> tags = activityService.findAllTag(page, limit);
        resBody.setCount(count);
        resBody.setData(tags);
        resBody.setCode(0);
        return resBody;
    }

    @GetMapping("/comment/getComments")
    @ResponseBody
    @ApiOperation("获取活动文字评论的table接口")
    public ResBody getComments(@ApiParam("当前页码") @RequestParam int page,
                           @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = activityService.getCommentCount();
        List<Comment> comments = activityService.findAllComment(page, limit);
        resBody.setCount(count);
        resBody.setData(comments);
        resBody.setCode(0);
        return resBody;
    }

    @GetMapping("/comment/getImgComments")
    @ResponseBody
    @ApiOperation("获取活动图片评论的table接口")
    public ResBody getImgComments(@ApiParam("当前页码") @RequestParam int page,
                               @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = activityService.getCommentImgCount();
        List<Comment> comments = activityService.findAllCommentImg(page, limit);
        resBody.setCount(count);
        resBody.setData(comments);
        resBody.setCode(0);
        return resBody;
    }

    @GetMapping("/hot")
    @ResponseBody
    @ApiOperation("微信获取热门活动的接口")
    public Map getHot(){
        Map map = new HashMap();
        Activity activity = activityService.findActivityById(1);
        List<Tag> tagList = activityService.findTagByActivityId(1);
        map.put("tagList",tagList);
        map.put("activity",activity);
        return map;
    }


    @GetMapping("/wx/getActivityList")
    @ResponseBody
    @ApiOperation("微信活动的接口")
    public List getActivityLis(){
        List<Map> activityList = activityService.findAllActivityByWx();
        return activityList;
    }

    @GetMapping("/wx/getHotActivity")
    @ResponseBody
    @ApiOperation("微信热门活动的接口")
    public Activity getHotActivity(){
        Activity activity = activityService.findActivityById(1);
        return activity;
    }

    @GetMapping("/wx/getJoinActivityList")
    @ResponseBody
    @ApiOperation("微信参与的活动的接口")
    public List getJoinActivityList(@ApiParam("用户ID") @RequestParam String uuid){
        List<Map> activityList = activityService.findAllJoinActivityByWx(uuid);
        return activityList;
    }

    @GetMapping("/wx/getApplyActivityList")
    @ResponseBody
    @ApiOperation("微信申请的活动的接口")
    public List getApplyActivityList(@ApiParam("用户ID") @RequestParam String uuid){
        List<Map> activityList = activityService.findAllApplyActivityByWx(uuid);
        return activityList;
    }

    @GetMapping("/wx/getLoveActivityList")
    @ResponseBody
    @ApiOperation("微信活动的接口")
    public List getLoveActivityList(@ApiParam("用户ID") @RequestParam String uuid){
        List<Map> activityList = activityService.findAllLoveActivityByWx(uuid);
        return activityList;
    }

    @GetMapping("/wx/getActivityDetail")
    @ResponseBody
    @ApiOperation("微信活动详情的接口")
    public ResBody getActivityDetail(@ApiParam("活动id") @RequestParam int id,@ApiParam("用户UUID") @RequestParam String uuid){
        ResBody resBody = new ResBody();
        List list = new ArrayList();
        Map activityList = activityService.findActivityDetailByWx(id);
        User_Activity user_activity = activityService.findUserActivityByActivityId(id,uuid);
        list.add(activityList);
        resBody.setData(list);
        if (user_activity==null){
            resBody.setMsg("0");
            resBody.setCode(0);
        }else {
            resBody.setMsg(String.valueOf(user_activity.getIsLove()));
            resBody.setCode(user_activity.getIsJoin());
        }

        return resBody;
    }

    @GetMapping("/getActivities")
    @ResponseBody
    @ApiOperation("获取活动列表的table接口")
    public ResBody getActivities(@ApiParam("当前页码") @RequestParam int page,
                           @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = activityService.getActivityCount();
        List<Activity> activities = activityService.findAllActivity(page, limit);
        resBody.setCount(count);
        resBody.setData(activities);
        resBody.setCode(0);
        return resBody;
    }

    @GetMapping("/getActivitiesByTime")
    @ResponseBody
    @ApiOperation("按时间搜索活动列表的table接口")
    public ResBody getActivitiesByTime(@ApiParam("当前页码") @RequestParam int page,
                                 @ApiParam("每页数据量") @RequestParam int limit,
                                       @ApiParam("开始时间") @RequestParam String begin,
                                       @ApiParam("结束时间") @RequestParam String end) {
        ResBody resBody = new ResBody();
        int count = activityService.getActivityCount(begin, end);
        List<Activity> activities = activityService.findAllActivity(page, limit, begin, end);
        resBody.setCount(count);
        resBody.setData(activities);
        resBody.setCode(0);
        return resBody;
    }

    @DeleteMapping("/delActivity/{id}")
    @ResponseBody
    @ApiOperation("删除活动")
    public ResBody delActivity(@PathVariable("id") @ApiParam("活动ID") String id) {
        ResBody resBody = new ResBody();
        String[] s = id.split(",");
        for (int i = 0; i < s.length; i++) {
            activityService.deleteActivityById(Integer.parseInt(s[i]));
        }
        resBody.setCode(200);
        return resBody;
    }

    @DeleteMapping("/delTag/{id}")
    @ResponseBody
    @ApiOperation("删除标签")
    public ResBody delTag(@PathVariable("id") @ApiParam("标签ID") String id) {
        ResBody resBody = new ResBody();
        String[] s = id.split(",");
        for (int i = 0; i < s.length; i++) {
            activityService.deleteTagById(Integer.parseInt(s[i]));
        }
        resBody.setCode(200);
        return resBody;
    }

    @ApiOperation("添加标签")
    @ResponseBody
    @PostMapping(value = "/addTagByAjax", produces = "application/json;charset=UTF-8")
    public ResBody addTagByAjax(@RequestBody Map<String, Object> params) {
        ResBody resBody = new ResBody();
        String name = params.get("name").toString();
        String color = params.get("color").toString();
        Tag tag = new Tag(0,name,color);
        int i = activityService.addTagByAjax(tag);
        if (i == 1) {
            resBody.setCode(200);
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("更新标签")
    @ResponseBody
    @PutMapping(value = "/tag/updateTag", produces = "application/json;charset=UTF-8")
    public ResBody updateTag(@RequestBody Map<String, Object> params) {
        ResBody resBody = new ResBody();
        int id = Integer.parseInt(params.get("id").toString());
        String name = params.get("name").toString();
        String color = params.get("color").toString();
        Tag tag = new Tag(id,name,color);
        int i = activityService.updateTag(tag);
        if (i == 1) {
            resBody.setCode(200);
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("活动不通过审核")
    @ResponseBody
    @GetMapping(value = "/activity/sendActivityMessage", produces = "application/json;charset=UTF-8")
    public ResBody sendMessage(String activityId,String value) {
        ResBody resBody = new ResBody();
        User user = activityService.findUserByActivityId(Integer.parseInt(activityId));
        if (user!=null) {
            resBody.setCode(200);
            rabbitTemplate.convertAndSend("exchange.direct","user","NoActivity:"+user.getEmail()+":"+value);
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("活动通过审核")
    @ResponseBody
    @GetMapping(value = "/activity/updateActivityAndRoom", produces = "application/json;charset=UTF-8")
    public ResBody updateActivityAndRoom(String activityId,String roomId) {
        ResBody resBody = new ResBody();
        User user = activityService.findUserByActivityId(Integer.parseInt(activityId));
        Room room = activityService.findRoomById(Integer.parseInt(roomId));
        Activity activity = activityService.findActivityById(Integer.parseInt(activityId));
        if (room != null){
            //房间存在
            resBody.setCode(500);
        }else{
            //房间不存在，新建房间并关联
            activityService.updateActivityAndRoom(activity,Integer.parseInt(roomId));
            resBody.setCode(200);
            rabbitTemplate.convertAndSend("exchange.direct","user","successActivity"+user.getEmail()+":"+roomId+":"+activity.getTitle());
        }
        return resBody;
    }

    @ApiOperation("收藏活动")
    @ResponseBody
    @GetMapping(value = "/wx/setIsLove", produces = "application/json;charset=UTF-8")
    public ResBody setIsLove(String uuid,String activity_id) {
        ResBody resBody = new ResBody();
       int i = activityService.setIsLove(uuid,Integer.parseInt(activity_id));
        if (i == 1){
            resBody.setCode(200);
        }else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("取消收藏活动")
    @ResponseBody
    @GetMapping(value = "/wx/cancelIsLove", produces = "application/json;charset=UTF-8")
    public ResBody cancelIsLove(String uuid,String activity_id) {
        ResBody resBody = new ResBody();
        int i = activityService.cancelIsLove(uuid,Integer.parseInt(activity_id));
        if (i == 1){
            resBody.setCode(200);
        }else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("参与活动")
    @ResponseBody
    @GetMapping(value = "/wx/joinActivity", produces = "application/json;charset=UTF-8")
    public ResBody joinActivity(String uuid,String activity_id) {
        ResBody resBody = new ResBody();
        Activity activity = activityService.findActivityById(Integer.parseInt(activity_id));
        int count = activityService.findPeopleByActivityId(Integer.parseInt(activity_id));
        if (activity.people <= count){
            resBody.setCode(500);
        }else {
            activityService.joinActivity(uuid,Integer.parseInt(activity_id));
            resBody.setCode(200);
        }
        return resBody;
    }

    @GetMapping("/addTag")
    @ApiOperation("添加标签页面")
    public String addTag(){
        return "app/activity/tag/content";
    }

    @GetMapping("/moreTag/{id}")
    @ApiOperation("查看标签详情页面")
    public String moreTag(@PathVariable("id")@ApiParam("标签ID") int id, Model model){
        Tag tag = activityService.findTagById(id);
        model.addAttribute("tag",tag);
        return "app/activity/tag/content";
    }

    @GetMapping("/moreActivity/{id}")
    @ApiOperation("审核活动页面")
    public String moreActivity(@PathVariable("id")@ApiParam("活动ID") int id, Model model){
        Activity activity = activityService.findActivityById(id);
        List<Tag> tags = activityService.findTagByActivityId(id);
        StringBuffer tag = new StringBuffer();
        for (Tag t:tags){
            tag.append("#"+t.getName()+";");
        }
        model.addAttribute("activity",activity);
        model.addAttribute("tag",tag);
        return "app/activity/activity/content";
    }
}
