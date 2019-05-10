package com.yg.bishe.controller;

import com.alibaba.fastjson.JSON;
import com.yg.bishe.bean.Admin;
import com.yg.bishe.bean.EchartsData;
import com.yg.bishe.bean.Log;
import com.yg.bishe.bean.ResBody;
import com.yg.bishe.service.AdminService;
import com.yg.bishe.service.LogService;
import com.yg.bishe.util.IpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@Api(tags = "日志接口")
public class LogController {
    private final Logger logger = LoggerFactory.getLogger(LogController.class);
    @Autowired
    LogService logService;
    @Autowired
    AdminService adminService;

    @GetMapping("/getLogs")
    @ResponseBody
    @ApiOperation("获取日志的table接口")
    public ResBody getLogs(@ApiParam("当前页码") @RequestParam int page,
                           @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = logService.getCount();
        List<Log> logs = logService.findAllLog(page, limit);
        resBody.setCount(count);
        resBody.setData(logs);
        resBody.setCode(0);
        return resBody;
    }

    @GetMapping("/getLogsByDate")
    @ResponseBody
    @ApiOperation("获取日志的table接口")
    public ResBody getLogsByDate(@ApiParam("当前页码") @RequestParam int page,
                                 @ApiParam("每页数据量") @RequestParam int limit,
                                 @ApiParam("开始时间") @RequestParam String begin,
                                 @ApiParam("结束时间") @RequestParam String end) {
        ResBody resBody = new ResBody();
        int count = logService.getCount(begin, end);
        List<Log> logs = logService.findAllLog(page, limit, begin, end);
        resBody.setCount(count);
        resBody.setData(logs);
        resBody.setCode(0);
        return resBody;
    }

    @ApiOperation("用户名是否存在")
    @ResponseBody
    @GetMapping(value = "/checkAdminName", produces = "application/json;charset=UTF-8")
    public ResBody checkAdminName(@ApiParam("管理员用户名") @RequestParam String name) {
        ResBody resBody = new ResBody();
        Admin admin = adminService.findAdminByName(name);
        if (admin != null) {
            resBody.setCode(200);
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("获取运营商和所在位置")
    @ResponseBody
    @GetMapping(value = "/getOperateAndLocation", produces = "application/json;charset=UTF-8")
    public ResBody getOperateAndLocation(@ApiParam("ip地址") @RequestParam String ip) {
        ResBody resBody = new ResBody();
        try {
            String location = IpUtil.getAddress(ip);
            String operator = IpUtil.getOperator(ip);
            Map map = new HashMap();
            map.put("location", location);
            map.put("operator", operator);
            String s = JSON.toJSONString(map);
            resBody.setCode(200);
            resBody.setMsg(s);
        } catch (Exception e) {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("添加日志")
    @ResponseBody
    @PostMapping(value = "/addLogByAjax", produces = "application/json;charset=UTF-8")
    public ResBody addLogByAjax(@RequestBody Map<String, Object> params) {
        ResBody resBody = new ResBody();
        String name = params.get("name").toString();
        String ip = params.get("ip").toString();
        String operator = params.get("operator").toString();
        String location = params.get("location").toString();
        String mode = params.get("mode").toString();
        String logintime = params.get("logintime").toString();
        Log log = new Log(0, name, ip, operator, location, mode, logintime);
        int i = logService.addLog(log);
        if (i == 1) {
            resBody.setCode(200);
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("更新日志")
    @ResponseBody
    @PutMapping(value = "/updateLogById", produces = "application/json;charset=UTF-8")
    public ResBody updateLogById(@RequestBody Map<String, Object> params) {
        ResBody resBody = new ResBody();
        int id = Integer.parseInt(params.get("id").toString());
        String name = params.get("name").toString();
        String ip = params.get("ip").toString();
        String operator = params.get("operator").toString();
        String location = params.get("location").toString();
        String mode = params.get("mode").toString();
        String logintime = params.get("logintime").toString();
        Log log = new Log(id, name, ip, operator, location, mode, logintime);
        int i = logService.updateById(log);
        if (i == 1) {
            resBody.setCode(200);
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @DeleteMapping("/delLog/{id}")
    @ResponseBody
    @ApiOperation("删除日志")
    public ResBody moreLog(@PathVariable("id") @ApiParam("日志ID") String id) {
        ResBody resBody = new ResBody();
        String[] s = id.split(",");
        for (int i = 0; i < s.length; i++) {
            logService.deleteLogById(Integer.parseInt(s[i]));
        }
        resBody.setCode(200);
        return resBody;
    }


    @GetMapping("/addLog")
    @ApiOperation("添加日志")
    public String addLog(){
        return "monitor/log/log_content";
    }

    @GetMapping("/moreLog/{id}")
    @ApiOperation("查看日志详情")
    public String moreLog(@PathVariable("id")@ApiParam("日志ID") Integer id, Model model){
        Log log = logService.findLogById(id);
        model.addAttribute("log",log);
        return "monitor/log/log_content";
    }

    @GetMapping("/echartsLog")
    @ApiOperation("日志分析")
    public String echartsLog(Model model){
        List name = new LinkedList();
        List admin = new LinkedList();
        List password = new LinkedList();
        List phone = new LinkedList();
        List<EchartsData> data = logService.getData();
        for(EchartsData d:data){
            name.add(d.getName());
        }
        model.addAttribute("name",JSON.toJSONString(name));
        model.addAttribute("data",JSON.toJSONString(data));
        List<EchartsData> bar_password = logService.getLoginData("用户名/密码登录");
        List<EchartsData> bar_phone = logService.getLoginData("手机登录");
        for (EchartsData d:bar_password){
            admin.add(d.getName());
            password.add(d.getValue());
        }
        for (EchartsData d:bar_phone){
            phone.add(d.getValue());
        }
        model.addAttribute("admin",JSON.toJSONString(admin));
        model.addAttribute("password",JSON.toJSONString(password));
        model.addAttribute("phone",JSON.toJSONString(phone));
        return "monitor/log/log_echarts";
    }
}
