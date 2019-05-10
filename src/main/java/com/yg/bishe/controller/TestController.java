package com.yg.bishe.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hengyi.dzfilter.utils.TextUtils;
import com.yg.bishe.bean.*;
import com.yg.bishe.listener.MyWebSocket;
import com.yg.bishe.service.AdminService;
import com.yg.bishe.service.BannerService;
import com.yg.bishe.service.MenuService;
import com.yg.bishe.service.RoleService;
import com.yg.bishe.util.FastDFSClient;
import com.yg.bishe.util.QRCodeUtil;
import com.yg.bishe.util.SendMessageUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
public class TestController {
    @Autowired
    AdminService adminService;
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;
    @Autowired
    BannerService bannerService;

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test")
    public Stack test(){
        Stack stack = new Stack();
        for (int i = 0;i<10;i++){
            stack.add(i);
        }
        logger.info(stack.toString());
        return stack;
    }

    @GetMapping("/testAdmin")
    public Admin_Role get(){
        Admin_Role admin_role = new Admin_Role();
        Admin admin = adminService.findAdminByName("admin");
        admin_role.setAdmin(admin);
        admin_role.setRole(roleService.findRoleByAdmin(admin));

        return admin_role;
    }

//    @GetMapping("/hot")
//    public Map getHot(){
//        Map map = new HashMap();
//        map.put("title","学院召开领导班子专题民主生活会");
//        map.put("img","https://image.weilanwl.com/img/4x3-3.jpg");
//        map.put("content","1 月11日 下午，学院召开领导班子专题民主生活会。\n" +
//                "\n" +
//                "会上，学院党总支书记杨益彬首先带领大家重温了《习近平新时代中国特" +
//                "色社会主义思想三十讲》中第二十九讲“努力掌握马克思主义思想方法和工作方法”，" +
//                "随后代表学院领导班子按照专题民主生活会的要求认真进行了对照检查，围绕学校第三次党代会提出的目标和" +
//                "实施方案、学院目标考核和民主测评成绩，认真查摆学院发展中存在的问题，深刻剖析问题产生的深层次原因，" +
//                "提出改进措施。与会领导班子成员结合自己分管的工作逐一作个人对照检查，主动剖析问题和不足，深挖产生问题的原因，" +
//                "提出整改措施。领导班子成员之间开展了批评与自我批评，并对同志们的批评帮助做出了积极表态。最后，杨益彬书记代" +
//                "表班子做了表态发言，他表示今后要进一步加强学习，提高政治站位，更加自觉地把思想和行动统一到中央和校党委的各项" +
//                "部署要求上来，支持和配合学校机构调整工作，无论在怎样的工作岗位上，都要切实围绕“四大战略”和“八项工程”开展" +
//                "工作，为8050计划的实现，为把学校建设成为特色鲜明的高水平大学贡献力量。");
//        List tag = new ArrayList();
//        tag.add("学院会议");
//        tag.add("通大风采");
//        map.put("tag",tag);
//        return map;
//    }

    @GetMapping("/hotDetail")
    public Map getHotDetail(String title){
        if (title.equals("学院召开领导班子专题民主生活会"));
        Map map = new HashMap();
        map.put("img","https://image.weilanwl.com/img/4x3-3.jpg");
        map.put("content","");
        List tag = new ArrayList();
        tag.add("学院会议");
        tag.add("通大风采");
        map.put("tag",tag);
        map.put("time","2019-04-27 09:00至2019-04-27 17:00");
        map.put("local","江苏省南通市啬园路9号南通大学主校区计算机楼6楼");
        map.put("organization","南通大学计算机与科学技术学院");
        map.put("people",100);
        return map;
    }

    @GetMapping("/user/loginByWX")
    public ResBody login( @RequestParam String nickname){
        ResBody resBody = new ResBody();
        System.out.println(nickname);
        if (nickname.isEmpty()||nickname.equals("undefined")){
            resBody.setCode(500);
            resBody.setMsg("参数为空");
        }else {
            resBody.setCode(200);
            resBody.setMsg("uid:"+nickname);
        }
        return resBody;
    }
    @GetMapping("/qrcode")
    public ResBody qrcode(){
        ResBody resBody = new ResBody();
        List list = new LinkedList();
        List<Role> roles = roleService.getRoleList();
        List<Banner> banner = bannerService.findAllBanner(1,10);
        list.add(roles);
        list.add(banner);
        resBody.setData(list);
        return resBody;
    }

    @GetMapping("/test/mini_message")
    public ResBody mess() throws Exception {
        ResBody resBody = new ResBody();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","message");
        jsonObject.put("message","您有一条新消息");
        MyWebSocket.broadcast("mini",jsonObject.toJSONString());
        return resBody;
    }
}
