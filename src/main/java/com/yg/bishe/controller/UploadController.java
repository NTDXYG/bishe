package com.yg.bishe.controller;

import com.alibaba.fastjson.JSON;
import com.hengyi.dzfilter.utils.TextUtils;
import com.yg.bishe.bean.File;
import com.yg.bishe.bean.ResBody;
import com.yg.bishe.service.UploadService;
import com.yg.bishe.util.FastDFSClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UploadController {
    private static Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    UploadService uploadService;

    @GetMapping("/getFile")
    @ResponseBody
    @ApiOperation("获取文件的table接口")
    public ResBody getLogs(@ApiParam("当前页码") @RequestParam int page,
                           @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = uploadService.getCount();
        List<File> list = uploadService.getFile(page,limit);
        resBody.setCount(count);
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    @DeleteMapping("/delFile/{id}")
    @ResponseBody
    @ApiOperation("删除文件")
    public ResBody moreLog(@PathVariable("id") @ApiParam("文件ID") int id) {
        ResBody resBody = new ResBody();
        int i = uploadService.deleteById(id);
        if (i==1){
            resBody.setCode(200);
        }else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @PostMapping(value = "/upload", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ApiOperation("上传文件")
    public ResBody singleFileUpload(@RequestParam("file") MultipartFile file){
        ResBody resBody = new ResBody();
        if (file.isEmpty()) {
            resBody.setCode(500);
            resBody.setMsg("文件为空");
        }else {
            String name = file.getOriginalFilename();
            String str = FastDFSClient.uploadFile(file);
            String url = FastDFSClient.getResAccessUrl(str);
            uploadService.addFile(url,name);
            resBody.setCode(200);
            resBody.setMsg(url);
        }
        return resBody;
    }

    @PostMapping(value = "/uploadByEdit", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ApiOperation("富文本上传文件")
    public HashMap uploadByEdit(@RequestParam("file") MultipartFile file){
        HashMap map = new HashMap();
        if (file.isEmpty()) {
            map.put("code",0);
            map.put("msg","文件为空");
        }else {
            HashMap data = new HashMap();
            String name = file.getOriginalFilename();
            String str = FastDFSClient.uploadFile(file);
            String url = FastDFSClient.getResAccessUrl(str);
            uploadService.addFile(url,name);
            data.put("src",url);
            data.put("title",name);
            map.put("code",0);
            map.put("msg","");
            map.put("data",data);
        }
        return map;
    }

    @PostMapping("/uploadByWX")
    @ResponseBody
    @ApiOperation("微信上传文件")
    public String uploadByWX(@RequestParam("file") MultipartFile file){
        String url = "";
        if (file.isEmpty()) {
        }else {
            String name = file.getOriginalFilename();
            String str = FastDFSClient.uploadFile(file);
            url = FastDFSClient.getResAccessUrl(str);
            uploadService.addFile(url,name);
        }
        return url;
    }

    @ApiOperation("跳转文件管理")
    @GetMapping(value = "/fileManagement")
    public String fileManagement() {
        return "/set/system/fileManage";
    }

}
