package com.yg.bishe.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yg.bishe.bean.ResBody;
import com.yg.bishe.bean.Tag;
import com.yg.bishe.bean.User;
import com.yg.bishe.service.ActivityService;
import com.yg.bishe.service.MessageService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/getUsers")
    @ResponseBody
    @ApiOperation("获取用户的table接口")
    public ResBody getTags(@ApiParam("当前页码") @RequestParam int page,
                           @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = userService.getCount();
        List<User> users = userService.findAllUser(page, limit);
        resBody.setCount(count);
        resBody.setData(users);
        resBody.setCode(0);
        return resBody;
    }

    @GetMapping("/getUserByUUID")
    @ResponseBody
    @ApiOperation("获取用户")
    public User getUser(@ApiParam("用户UUID") @RequestParam String uuid) {
        return userService.findUserById(uuid);
    }

    @DeleteMapping("/delUser/{uuid}")
    @ResponseBody
    @ApiOperation("删除用户")
    public ResBody delTag(@PathVariable("uuid") @ApiParam("用户id") String uuid) {
        ResBody resBody = new ResBody();
        String[] s = uuid.split(",");
        for (String s1 : s) {
            userService.deleteUserById(s1);
        }
        resBody.setCode(200);
        return resBody;
    }

    @ApiOperation("获取用户列表")
    @ResponseBody
    @GetMapping(value = "/user/selectUserList")
    public List<User> selectUserList() {
        List<User> list = userService.getUserList();
        return list;
    }

    @ApiOperation("用户认证")
    @ResponseBody
    @GetMapping(value = "/uploadUserByWx", produces = "application/json;charset=UTF-8")
    public ResBody uploadUserByWx(String uuid,String data) {
        ResBody resBody = new ResBody();
        JSONObject jsonObject = JSON.parseObject(data);
        String email = jsonObject.getString("email");
        String card = jsonObject.getString("card");
        String card_back = jsonObject.getString("card_back");
        String qrcode = jsonObject.getString("qrcode");
        int i = userService.uploadUserByWx(uuid,email,card,card_back,qrcode);
        if (i == 1) {
            resBody.setCode(200);
            messageService.addMessage("您有新的用户需要认证","您有新的用户需要认证,请注意即时查看。", DateUtil.getDate());
            rabbitTemplate.convertAndSend("exchange.direct","user","new Card");
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("拒绝认证")
    @ResponseBody
    @GetMapping(value = "/user/sendMessage", produces = "application/json;charset=UTF-8")
    public ResBody sendMessage(String uuid,String value) {
        ResBody resBody = new ResBody();
        User user = userService.findUserById(uuid);
        if (user!=null) {
            resBody.setCode(200);
            rabbitTemplate.convertAndSend("exchange.direct","user","NoUser:"+user.getEmail()+":"+value);
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("同意认证用户")
    @ResponseBody
    @GetMapping(value = "/user/updateUser", produces = "application/json;charset=UTF-8")
    public ResBody updateTagById(String uuid) {
        ResBody resBody = new ResBody();
        int i = userService.updateUser(uuid);
        User user = userService.findUserById(uuid);
        if (i == 1) {
            resBody.setCode(200);
            rabbitTemplate.convertAndSend("exchange.direct","user","successUser"+user.getEmail());
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @GetMapping("/moreUser/{uuid}")
    @ApiOperation("认证用户页面")
    public String moreUser(@PathVariable("uuid")@ApiParam("用户UUID") String uuid, Model model){
        User user = userService.findUserById(uuid);
        model.addAttribute("user",user);
        return "user/user/userform";
    }
}
