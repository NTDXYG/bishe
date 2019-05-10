package com.yg.bishe.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    public int id;
    //登录名
    public String name;
    //IP地址
    public String ip;
    //运营商
    public String operator;
    //归属地
    public String location;
    //用户名密码、手机登陆
    public String mode;
    //登录时间
    public String logintime;
}