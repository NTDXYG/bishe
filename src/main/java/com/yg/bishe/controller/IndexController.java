package com.yg.bishe.controller;

import com.yg.bishe.bean.*;
import com.yg.bishe.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Api(tags="首页菜单栏跳转")
public class IndexController {

    @Autowired
    BannerService bannerService;
    @Autowired
    ActivityService activityService;
    @Autowired
    RoleService roleService;
    @Autowired
    MessageService messageService;

    @GetMapping("/index")
    @ApiOperation("跳转后台首页")
    public String index(){
        return "index";
    }

    @GetMapping("/console")
    @ApiOperation("跳转控制台")
    public String console(){
        return "home/console";
    }

    @GetMapping("/message")
    @ApiOperation("跳转消息中心")
    public String message(Model model){
        int count = messageService.getCount(0);
        if (count > 0){
            model.addAttribute("count",count);
        }
        return "app/message/index";
    }


    @GetMapping("/meetingList")
    @ApiOperation("跳转活动列表")
    public String meetingList(){
        return "app/activity/activity/list";
    }

    @GetMapping("/tag")
    @ApiOperation("跳转分类管理")
    public String tag(){
        return "app/activity/tag/list";
    }

    @GetMapping("/comment")
    @ApiOperation("跳转评论管理")
    public String comment(){
        return "app/activity/comment/list";
    }

    @GetMapping("/topicList")
    @ApiOperation("跳转话题列表")
    public String topicList(){
        return "app/topic/topic/list";
    }

    @GetMapping("/replyList")
    @ApiOperation("跳转回帖列表")
    public String replyList(){
        return "app/topic/reply/list";
    }

    @GetMapping("/banner")
    @ApiOperation("跳转轮播图")
    public String banner(Model model){
        List<Banner> bannerList = bannerService.findAllBanner(1,1,10);
        model.addAttribute("banner",bannerList);
        return "app/banner/list";
    }

    @GetMapping("/popRecommend")
    @ApiOperation("跳转热门推荐")
    public String popRecommend(Model model){
        Activity activity = activityService.findActivityById(1);
        List<Tag> tagList = activityService.findAllTag(1,100);
        List<Tag> tags = activityService.findTagByActivityId(1);
        StringBuffer s = new StringBuffer();
        for (Tag tag:tags){
            s.append(tag.getName()+";");
        }
        model.addAttribute("activity",activity);
        model.addAttribute("tagList",tagList);
        model.addAttribute("tags",s);
        return "app/recommend/hot";
    }

    @GetMapping("/priRecommend")
    @ApiOperation("跳转个性化推荐")
    public String priRecommend(){
        return "app/recommend/list";
    }

    @GetMapping("/springbootadmin")
    @ApiOperation("跳转springboot监控")
    public String springbootadmin(){
        return "redirect:http://127.0.0.1:8000";
    }

    @GetMapping("/druidMonitor")
    @ApiOperation("跳转数据库监控")
    public String druidMonitor(){
        return "redirect:http://ntdxyg.mynatapp.cc/druid/index.html";
    }

    @GetMapping("/swagger")
    @ApiOperation("跳转文档")
    public String swagger(){
        return "redirect:http://ntdxyg.mynatapp.cc/swagger-ui.html";
    }

    @GetMapping("/logMonitor")
    @ApiOperation("跳转日志监控")
    public String logMonitor(){
        return "monitor/log/log_list";
    }

    @GetMapping("/rabbitmqMonitor")
    @ApiOperation("跳转信息监控")
    public String rabbitmqMonitor(){
        return "redirect:http://118.25.55.210:15672/#/";
    }

    @GetMapping("/404")
    @ApiOperation("跳转404")
    public String noFound(){
        return "template/tips/404";
    }

    @GetMapping("/401")
    @ApiOperation("跳转权限不够")
    public String error(){
        return "template/tips/401";
    }

    @GetMapping("/user/list")
    @ApiOperation("跳转用户列表")
    public String userList(){
        return "user/user/list";
    }

    @GetMapping("/admin/list")
    @ApiOperation("跳转管理员列表")
    public String adminList(){
        return "user/administrators/list";
    }

    @GetMapping("/role/list")
    @ApiOperation("跳转角色列表")
    public String roleList(){
        return "user/administrators/role";
    }


    @GetMapping("/webrtc")
    @ApiOperation("webrtc")
    public String webrtc(){
        return "webrtc/index";
    }

    @GetMapping("/rtmp")
    @ApiOperation("rtmp")
    public String rtmp(){
        return "webrtc/rtmpList";
    }


    @GetMapping("/basicData")
    @ApiOperation("基本资料")
    public String basicData(){
        return "set/user/info";
    }

    @GetMapping("/updateAdminPass")
    @ApiOperation("修改密码")
    public String updateAdminPass(){
        return "set/user/password";
    }

    @GetMapping("/webSetting")
    @ApiOperation("网站设置")
    public String webSetting(){
        return "set/system/website";
    }


}
