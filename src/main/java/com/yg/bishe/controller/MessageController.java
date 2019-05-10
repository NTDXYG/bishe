package com.yg.bishe.controller;

import com.yg.bishe.bean.Message;
import com.yg.bishe.bean.ResBody;
import com.yg.bishe.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Api(tags="消息接口")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/message/getMessages")
    @ResponseBody
    @ApiOperation("获取用户的table接口")
    public ResBody getTags(@ApiParam("当前页码") @RequestParam int page,
                           @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = messageService.getCount();
        List<Message> messages = messageService.findAllMessage(page, limit);
        resBody.setCount(count);
        resBody.setData(messages);
        resBody.setCode(0);
        return resBody;
    }

    @ApiOperation("获取消息详情")
    @GetMapping("/message/detail/{id}")
    public String getMenuByAdminId(@PathVariable("id")@ApiParam("消息id") Integer id, Model model){
        Message message = messageService.findMessageById(id);
        rabbitTemplate.convertAndSend("exchange.direct","atguigu.news",String.valueOf(id));
        model.addAttribute("mess",message);
        return "app/message/content";
    }
}
