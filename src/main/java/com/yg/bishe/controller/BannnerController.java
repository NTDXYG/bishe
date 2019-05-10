package com.yg.bishe.controller;

import com.yg.bishe.bean.Admin;
import com.yg.bishe.bean.Banner;
import com.yg.bishe.bean.ResBody;
import com.yg.bishe.bean.Role;
import com.yg.bishe.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "轮播图接口")
public class BannnerController {
    private final Logger logger = LoggerFactory.getLogger(BannnerController.class);

    @Autowired
    BannerService bannerService;

    @GetMapping("banner/getBanners")
    @ResponseBody
    @ApiOperation("获取轮播图的table接口")
    public ResBody getBanners(@ApiParam("当前页码") @RequestParam int page,
                             @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = bannerService.getCount();
        List<Banner> bannerList= bannerService.findAllBanner(page, limit);
        resBody.setCount(count);
        resBody.setData(bannerList);
        resBody.setCode(0);
        return resBody;
    }

    @GetMapping("banner/getBannersByWX")
    @ResponseBody
    @ApiOperation("获取轮播图的table接口")
    public ResBody getBannersByWX(@ApiParam("是否展示") @RequestParam int status,
                                  @ApiParam("当前页码") @RequestParam int page,
                                  @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = bannerService.getCount(status);
        List<Banner> bannerList= bannerService.findAllBanner(status,page, limit);
        resBody.setCount(count);
        resBody.setData(bannerList);
        resBody.setCode(0);
        return resBody;
    }

    @DeleteMapping("/delBanner/{id}")
    @ResponseBody
    @ApiOperation("删除轮播图")
    public ResBody moreLog(@PathVariable("id") @ApiParam("轮播图ID") String id) {
        ResBody resBody = new ResBody();
        String[] s = id.split(",");
        for (int i = 0; i < s.length; i++) {
            bannerService.deleteBannerById(Integer.parseInt(s[i]));
        }
        resBody.setCode(200);
        return resBody;
    }

    @ApiOperation("添加轮播图")
    @ResponseBody
    @PostMapping(value = "/addBannerByAjax", produces = "application/json;charset=UTF-8")
    public ResBody addLogByAjax(@RequestBody Map<String, Object> params) {
        ResBody resBody = new ResBody();
        String src = params.get("src").toString();
        String url = params.get("url").toString();
        String describe = params.get("describe").toString();
        int status = Integer.parseInt(params.get("status").toString());
        Banner banner = new Banner(0,src,url,describe,status);
        int i = bannerService.addBannerByAjax(banner);
        if (i == 1) {
            resBody.setCode(200);
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("更新轮播图")
    @ResponseBody
    @PutMapping(value = "/banner/updateBanner", produces = "application/json;charset=UTF-8")
    public ResBody updateLogById(@RequestBody Map<String, Object> params) {
        ResBody resBody = new ResBody();
        int id = Integer.parseInt(params.get("id").toString());
        String src = params.get("src").toString();
        String url = params.get("url").toString();
        String describe = params.get("describe").toString();
        int status = Integer.parseInt(params.get("status").toString());
        Banner banner = new Banner(id,src,url,describe,status);
        int i = bannerService.updateBanner(banner);
        if (i == 1) {
            resBody.setCode(200);
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }


    @GetMapping("/addBanner")
    @ApiOperation("添加轮播图页面")
    public String addLog(){
        return "app/banner/content";
    }

    @GetMapping("/moreBanner/{id}")
    @ApiOperation("查看轮播图详情页面")
    public String moreAdmin(@PathVariable("id")@ApiParam("轮播图ID") int id, Model model){
        Banner banner = bannerService.findBannerById(id);
        model.addAttribute("banner",banner);
        return "app/banner/content";
    }
}
