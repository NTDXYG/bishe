package com.yg.bishe.controller;

import com.hengyi.dzfilter.utils.TextUtils;
import com.yg.bishe.bean.Log;
import com.yg.bishe.bean.ResBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Api(tags = {"敏感词接口"})
public class SensitiveController {

    @GetMapping("/getSensitive")
    @ResponseBody
    @ApiOperation("获取敏感词的table接口")
    public ResBody getLogs(@ApiParam("当前页码") @RequestParam int page,
                           @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = TextUtils.getFilterDataTotal();
        List<Map<String,Object>> list = TextUtils.getDataPage(page,limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    @ApiOperation("添加敏感词")
    @ResponseBody
    @PostMapping(value = "/addSensitiveByAjax", produces = "application/json;charset=UTF-8")
    public ResBody addLogByAjax(@RequestBody Map<String, Object> params) {
        ResBody resBody = new ResBody();
        String keywords = params.get("keywords").toString();
        int i = TextUtils.addFilter(keywords);
        if (i == 1) {
            resBody.setCode(200);
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @DeleteMapping("/delSensitive/{keywords}")
    @ResponseBody
    @ApiOperation("删除敏感词")
    public ResBody moreLog(@PathVariable("keywords") @ApiParam("敏感词") String keywords) {
        ResBody resBody = new ResBody();
        int i = TextUtils.delFilter(keywords);
        if (i == 1) {
            resBody.setCode(200);
        } else {
            resBody.setCode(500);
        }
        resBody.setCode(200);
        return resBody;
    }

    @ApiOperation("跳转敏感词管理")
    @GetMapping(value = "/sensitiveWord")
    public String sensitiveWord() {
        return "/set/system/sensitiveManage";
    }

    @ApiOperation("跳转敏感词增加页面")
    @GetMapping(value = "/addSensitive")
    public String addSensitive() {
        return "/set/system/addSensitive";
    }

}
