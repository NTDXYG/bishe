package com.yg.bishe.controller;

import com.tls.tls_sigature.tls_sigature;
import com.yg.bishe.bean.Admin;
import com.yg.bishe.bean.Log;
import com.yg.bishe.bean.ResBody;
import com.yg.bishe.bean.Role;
import com.yg.bishe.service.AdminService;
import com.yg.bishe.service.RoleService;
import com.yg.bishe.util.IpUtil;
import com.yg.bishe.util.SendMessageUtil;
import com.yg.bishe.util.UserSigUtil;
import com.yg.bishe.util.VerifyUtil;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@Api(value = "登录接口",tags = {"登录接口"})
public class LoginController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    AdminService adminService;

    @Autowired
    RoleService roleService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @ApiOperation("登录")
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @ApiOperation("退出")
    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        //注销
        subject.logout();
        return "user/login";
    }

    @ApiOperation("获取图片验证码")
    @GetMapping("/getCode")
    public void getCode(HttpServletResponse response) throws Exception {
        //利用图片工具生成图片
        //第一个参数是生成的验证码，第二个参数是生成的图片
        Object[] objs = VerifyUtil.createImage();
        //将验证码存入Redis,3分钟过期
        stringRedisTemplate.opsForValue().set(objs[0].toString(), objs[0].toString(), 3, TimeUnit.MINUTES);
        //将图片输出给浏览器
        BufferedImage image = (BufferedImage) objs[1];
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
    }

    @ResponseBody
    @ApiOperation("用户名/密码登录")
    @PostMapping(value = "/admin/loginByPassword", produces = "application/json;charset=UTF-8")
    public ResBody loginByPassword(@RequestBody Map<String, Object> params,
                                   HttpServletRequest request) throws IOException {
        String name = params.get("name").toString();
        String password = params.get("password").toString();
        String check = params.get("check").toString();
        ResBody resBody = new ResBody();
        logger.info(name + "---" + password + "---" + check);
        Log log = new Log();
        String checkRedis = stringRedisTemplate.opsForValue().get(check);
        //MD5加密
        String pass = DigestUtils.md5DigestAsHex(password.getBytes());
        if (checkRedis == null) {
            logger.info("验证码错误或已过期");
            resBody.setCode(500);
            resBody.setMsg("验证码错误或已过期");
        } else {
            if (checkRedis.equalsIgnoreCase(check)) {
                //1、获取Subject
                Subject subject = SecurityUtils.getSubject();
                //2、封装用户数据
                UsernamePasswordToken token = new UsernamePasswordToken(name, pass);
                //3、执行登录方法
                try {
                    subject.login(token);
                    logger.info(subject.getPrincipal().toString());
                    Role role = roleService.findRoleByAdmin((Admin) subject.getPrincipal());
                    //登录成功
                    subject.getSession().setAttribute("userSig", UserSigUtil.getUserSig(((Admin) subject.getPrincipal()).getName()));
                    subject.getSession().setAttribute("Admin", subject.getPrincipal());
                    subject.getSession().setAttribute("Role", role);
                    logger.info("登录成功");
                    resBody.setCode(200);
                    resBody.setMsg("登录成功");
                    String ip = IpUtil.getIPAddress(request);
                    String s = name+";"+ip+";"+"用户名/密码登录";
                    rabbitTemplate.convertAndSend("exchange.direct","admin.log",s);
                } catch (UnknownAccountException e) {
                    logger.info("用户不存在");
                    resBody.setCode(500);
                    resBody.setMsg("用户不存在");
                } catch (IncorrectCredentialsException e) {
                    logger.info("密码错误");
                    resBody.setCode(500);
                    resBody.setMsg("密码错误");
                } catch (Exception e) {
                    logger.info("IP地址解析错误");
                    resBody.setCode(500);
                    resBody.setMsg("IP地址解析错误");
                }
            } else {
                logger.info("验证码错误");
                resBody.setCode(500);
                resBody.setMsg("验证码错误");
            }
        }
        return resBody;
    }

    @ResponseBody
    @ApiOperation("手机登录")
    @PostMapping(value = "/admin/loginByPhone", produces = "application/json;charset=UTF-8")
    public ResBody loginByPhone(@RequestBody Map<String, Object> params,
                                HttpServletRequest request) throws IOException {
        String phone = params.get("phone").toString();
        String code = params.get("code").toString();
        ResBody resBody = new ResBody();
        logger.info(phone + ":" + code);
        Log log = new Log();
        String checkRedis = stringRedisTemplate.opsForValue().get(phone);
        if (checkRedis == null) {
            logger.info("验证码过期");
            resBody.setCode(500);
            resBody.setMsg("验证码过期");
        } else {
            if (checkRedis.equalsIgnoreCase(code)) {
                Admin admin = adminService.findAdminByPhone(phone);
                /**
                 * 使用shiro编写认证操作
                 */
                //1、获取Subject
                Subject subject = SecurityUtils.getSubject();
                //2、封装用户数据
                logger.info(admin.getName() + ":" + admin.getPassword());
                UsernamePasswordToken token = new UsernamePasswordToken(admin.getName(), admin.getPassword());
                //3、执行登录方法
                try {
                    subject.login(token);
                    logger.info(token.getCredentials() + "---" + token.getPrincipal());
                    //登录成功
                    logger.info("登录成功");
                    Role role = roleService.findRoleByAdmin((Admin) subject.getPrincipal());
                   //登录成功
                    subject.getSession().setAttribute("userSig", UserSigUtil.getUserSig(((Admin) subject.getPrincipal()).getName()));
                    //登录成功
                    subject.getSession().setAttribute("Admin", subject.getPrincipal());
                    subject.getSession().setAttribute("Role", role);
                    resBody.setCode(200);
                    resBody.setMsg("登录成功");
                    String ip = IpUtil.getIPAddress(request);
                    String s = admin.getName()+";"+ip+";"+"手机登录";
                    rabbitTemplate.convertAndSend("exchange.direct","admin.log",s);
                } catch (UnknownAccountException e) {
                    logger.info("用户不存在");
                    resBody.setCode(500);
                    resBody.setMsg("用户不存在");
                } catch (IncorrectCredentialsException e) {
                    logger.info("密码错误");
                    resBody.setCode(500);
                    resBody.setMsg("密码错误");
                } catch (Exception e) {
                    logger.info("IP地址解析错误");
                    resBody.setCode(500);
                    resBody.setMsg("IP地址解析错误");
                }
            } else {
                logger.info("验证码错误");
                resBody.setCode(500);
                resBody.setMsg("验证码错误");
            }
        }
        return resBody;
    }

    @ResponseBody
    @ApiOperation("获取手机验证码")
    @PostMapping(value = "/SendSMS", produces = "application/json;charset=UTF-8")
    public ResBody SendSMS(@RequestParam String phone) throws IOException {
        ResBody resBody = new ResBody();
        logger.info(phone);
        String code = SendMessageUtil.getCode(phone);
        if (code != null) {
            logger.info(code);
            stringRedisTemplate.opsForValue().set(phone, code, 3, TimeUnit.MINUTES);
            resBody.setCode(200);
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }
}