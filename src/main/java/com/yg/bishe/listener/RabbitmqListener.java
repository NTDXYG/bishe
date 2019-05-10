package com.yg.bishe.listener;

import com.yg.bishe.bean.Log;
import com.yg.bishe.service.LogService;
import com.yg.bishe.service.MessageService;
import com.yg.bishe.util.DateUtil;
import com.yg.bishe.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class RabbitmqListener {
    @Autowired
    LogService logService;
    @Autowired
    MessageService messageService;
    @Autowired
    JavaMailSenderImpl mailSender;
    private static final Logger logger = LoggerFactory.getLogger(RabbitListener.class);

    @RabbitListener(queues = "admin.log")
    public void addLog(String s){
        String[] message = s.split(";");
        String name = message[0];
        String ip = message[1];
        String mode = message[2];
        Log log = new Log();
        String location = "";
        String operator = "";
        try {
            location = IpUtil.getAddress(ip);
            operator = IpUtil.getOperator(ip);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("ip解析失败");
        }
        log.setName(name);
        log.setIp(ip);
        log.setLocation(location);
        log.setOperator(operator);
        log.setMode(mode);
        log.setLogintime(DateUtil.getDate());
        logService.addLog(log);
    }

    @RabbitListener(queues = "atguigu.news")
    public void getNews(String id){
        messageService.changeRead(Integer.parseInt(id));
    }

    @RabbitListener(queues = "user")
    public void newUser(String s) throws Exception {
        if (s.equals("new User")){
            MyWebSocket.broadcast("index","您有新的用户注册了");
        }else if (s.equals("new Card")){
            MyWebSocket.broadcast("index","您有新的用户需要认证");
        }else if(s.equals("new Activity")) {
            MyWebSocket.broadcast("index","您有新的活动需要审核");
        }else if (s.contains("NoActivity:")){
            String[] mess = s.split(":");
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("通知-您申请的活动没通过审核");
            message.setText(mess[2]);
            message.setTo(mess[1]);
            message.setFrom("2492019053@qq.com");
            mailSender.send(message);
        }else if (s.contains("NoUser:")){
            String[] mess = s.split(":");
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("通知-您的身份认证已被拒绝");
            message.setText(mess[2]);
            message.setTo(mess[1]);
            message.setFrom("2492019053@qq.com");
            mailSender.send(message);
        }else if (s.contains("successUser")){
            SimpleMailMessage message = new SimpleMailMessage();
            //邮件设置
            //标题
            message.setSubject("通知-您的身份认证已通过");
            //内容
            message.setText("您的实名认证已通过，感谢您的使用");
            //发送地址
            message.setTo(s.substring("successUser".length()));
            message.setFrom("2492019053@qq.com");
            mailSender.send(message);
        }else if (s.contains("successActivity")){
            SimpleMailMessage message = new SimpleMailMessage();
            //邮件设置
            String[] mess = s.split(":");
            //标题
            message.setSubject("通知-您的活动申请已通过");
            //内容
            message.setText("您的活动《"+mess[2]+"》申请已通过！您的推流地址为：rtmp://118.25.55.210/live/"+mess[1]+"；您的直播页面为：http://ntdxyg.mynatapp.cc/rtmp/"+mess[1]);
            //发送地址
            message.setTo(mess[0].substring("successActivity".length()));
            message.setFrom("2492019053@qq.com");
            mailSender.send(message);
        }
    }
}
