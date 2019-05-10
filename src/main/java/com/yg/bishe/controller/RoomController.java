package com.yg.bishe.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yg.bishe.bean.*;
import com.yg.bishe.service.ActivityService;
import com.yg.bishe.service.RoomService;
import com.yg.bishe.service.UserService;
import com.yg.bishe.util.UserSigUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@Api(tags = {"rtmp接口"})
public class RoomController {
    private final static Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    RoomService roomService;
    @Autowired
    UserService userService;
    @Autowired
    ActivityService activityService;

    @GetMapping("/getRoomList")
    @ResponseBody
    @ApiOperation("获取房间的table接口")
    public ResBody getTags(@ApiParam("当前页码") @RequestParam int page,
                           @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = roomService.getCount();
        List<Room> list = roomService.findAllRooms(page, limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    @GetMapping("/getRoomById")
    @ResponseBody
    @ApiOperation("根据ID查询房间的table接口")
    public ResBody getTags(@ApiParam("房间号") @RequestParam String roomid) {
        ResBody resBody = new ResBody();
        Room room = roomService.findRoomById(roomid);
        if (room == null){
            resBody.setCount(0);
        }else {
            resBody.setCount(1);
            List<Room> list = new LinkedList<>();
            list.add(room);
            resBody.setData(list);
        }
        resBody.setCode(0);
        return resBody;
    }

    @GetMapping("/wx/getRoomId")
    @ResponseBody
    @ApiOperation("根据活动ID查询rtmp房间接口")
    public Room getRoomId(@ApiParam("活动id") @RequestParam String activity_id) {
        Room room = roomService.findRoomByActivityId(activity_id);
        return room;
    }

    @GetMapping("/wx/getWebrtcRoomId")
    @ResponseBody
    @ApiOperation("根据活动ID查询webrtc房间接口")
    public List getWebrtcRoomId(@ApiParam("活动id") @RequestParam String activity_id,@ApiParam("用户id") @RequestParam String uuid) {
        List list = new LinkedList();
        Room room = roomService.findRoomByActivityId(activity_id);
        User user = userService.findUserById(uuid);
        list.add(uuid);
        list.add(UserSigUtil.getUserSig(uuid));
        list.add(user.getNickname());
        list.add(room.getId());
        return list;
    }

    @DeleteMapping("/delRoom/{id}")
    @ResponseBody
    @ApiOperation("删除房间")
    public ResBody moreLog(@PathVariable("id") @ApiParam("房间ID") String id) {
        ResBody resBody = new ResBody();
        String[] s = id.split(",");
        for (int i = 0; i < s.length; i++) {
            roomService.deleteRoomById(Integer.parseInt(s[i]));
        }
        resBody.setCode(200);
        return resBody;
    }

    @GetMapping("/rtmp/{roomid}")
    @ApiOperation("房间页面")
    public String rtmp(Model model,@PathVariable("roomid") @ApiParam("房间id") String id){
        Room room = roomService.findRoomById(id);
        model.addAttribute("room",room);
        return "webrtc/rtmp";
    }

    @GetMapping("/choujiang/{roomid}")
    @ApiOperation("抽奖页面")
    public String choujiang(Model model,@PathVariable("roomid") @ApiParam("房间id") String id){
        Room room = roomService.findRoomById(id);
        Activity activity = activityService.findActivityByRoomId(room.getId());
        List<Map> member = new LinkedList();
        List<User_Activity> list = activityService.findAllUserByActivityId(activity.getId());
        int i = 0;
        for (User_Activity user_activity : list){
            Map map = new HashMap();
            i++;
            map.put("name",user_activity.getUser().getNickname());
            map.put("userno",user_activity.getUser().getUuid());
            map.put("index",i);
            member.add(map);
        }
        String str = JSON.toJSONString(member);
        model.addAttribute("member",str);
        return "webrtc/choujiang";
    }
}
