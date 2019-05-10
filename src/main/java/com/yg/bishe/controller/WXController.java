package com.yg.bishe.controller;

import com.alibaba.fastjson.JSON;
import com.tls.tls_sigature.tls_sigature;
import com.yg.bishe.bean.Activity;
import com.yg.bishe.bean.ResBody;
import com.yg.bishe.bean.User;
import com.yg.bishe.bean.WXSession;
import com.yg.bishe.service.ActivityService;
import com.yg.bishe.service.MessageService;
import com.yg.bishe.service.UserService;
import com.yg.bishe.util.DateUtil;
import com.yg.bishe.util.HttpClientUtil;
import com.yg.bishe.util.UserSigUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class WXController {

    @Autowired
    MessageService messageService;
    @Autowired
    ActivityService activityService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserService userService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/wxGetUserSig")
    public String getUserSig(String name){
        return UserSigUtil.getUserSig(name);
    }

    @GetMapping("/wxLogin")
    public ResBody wxLogin(String code,String nickname,int gender,String avatarUrl) {
        ResBody resBody = new ResBody();
        System.out.println(nickname+"---"+gender+"---"+avatarUrl);
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> param = new HashMap<>();
        param.put("appid", "wx8e9fc06d8c8edd1d");
        param.put("secret", "ecd81e270bad7b0e74a1d1a975e47ba9");
        param.put("js_code", code);
        param.put("grant_type", "authorization_code");
        String wxResult = HttpClientUtil.doGet(url, param);
        WXSession wxSession = JSON.parseObject(wxResult, WXSession.class);
        String openid = wxSession.getOpenid();
        User user = userService.findUserByOpenId(openid,nickname,gender,avatarUrl);
        System.out.println(openid+ UUID.randomUUID());
        if (user != null){
            resBody.setMsg(user.getUuid());
            resBody.setCode(200);
        }else{
            String uuid = userService.addUserByWX(openid,nickname,gender,avatarUrl);
            messageService.addMessage("您有新的用户注册了","您有新的用户注册了,请注意即时查看。", DateUtil.getDate());
            rabbitTemplate.convertAndSend("exchange.direct","user","new User");
            resBody.setMsg(uuid);
            resBody.setCode(200);
        }
        return resBody;
    }

    @GetMapping("/sign")
    public ResBody sign(String roomid,String uuid) {
        ResBody resBody = new ResBody();
        Activity activity = activityService.findActivityByRoomId(Integer.parseInt(roomid));
        int i = userService.isSign(String.valueOf(activity.getId()),uuid);
        if (i==0){
            //未签到
            userService.sign(String.valueOf(activity.getId()),uuid);
            resBody.setCode(200);
        }else{
            resBody.setCode(500);
        }
        return resBody;
    }



}
