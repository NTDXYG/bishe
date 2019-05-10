package com.yg.bishe.controller;

import com.yg.bishe.bean.*;
import com.yg.bishe.service.AdminService;
import com.yg.bishe.service.LogService;
import com.yg.bishe.service.RoleService;
import com.yg.bishe.util.FastDFSClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "管理员接口")
public class AdminController {
    private final Logger logger = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    AdminService adminService;
    @Autowired
    RoleService roleService;

    @ApiOperation("修改个人信息")
    @ResponseBody
    @PutMapping(value = "/admin/updateAdmin", produces = "application/json;charset=UTF-8")
    public ResBody updateAdmin(@RequestBody Map<String, Object> params) {
        ResBody resBody = new ResBody();
        int role = Integer.parseInt(params.get("role").toString());
        String name = params.get("name").toString();
        String sex = params.get("sex").toString();
        String src = params.get("src").toString();
        String phone = params.get("phone").toString();
        String email = params.get("email").toString();
        logger.info(role+","+name+","+sex+","+src+","+phone+","+email);
        Admin admin = adminService.findAdminByName(name);
        Role rol = roleService.findRoleById(role);
        int i = adminService.updateAdmin(sex,src,phone,email,name);
        adminService.updateRole(admin.getId(),rol.getId());
        if (i == 1) {
            resBody.setCode(200);
            Subject subject = SecurityUtils.getSubject();
            subject.getSession().setAttribute("Admin", adminService.findAdminByName(name));
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("修改个人密码")
    @ResponseBody
    @PutMapping(value = "/admin/updatePassword", produces = "application/json;charset=UTF-8")
    public ResBody updatePassword(@RequestBody Map<String, Object> params) {
        Subject subject = SecurityUtils.getSubject();
        Admin admin = (Admin) subject.getSession().getAttribute("Admin");
        ResBody resBody = new ResBody();
        String oldPassword = params.get("oldPassword").toString();
        String password = params.get("password").toString();
        if (adminService.findAdminByPassword(admin.getName(),DigestUtils.md5DigestAsHex(oldPassword.getBytes())) != null){
            int i = adminService.updateAdmin(password,admin.getName());
            if (i == 1) {
                Admin adm = adminService.findAdminByName(admin.getName());
                subject.getSession().setAttribute("Admin",adm);
                resBody.setCode(200);
            } else {
                resBody.setCode(500);
                resBody.setMsg("后台出错");
            }
        }else {
            resBody.setCode(500);
            resBody.setMsg("密码不正确！");
        }
        return resBody;
    }

    @GetMapping("/getAdmins")
    @ResponseBody
    @ApiOperation("获取管理员的table接口")
    public ResBody getAdmins(@ApiParam("当前页码") @RequestParam int page,
                           @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = adminService.getCount();
        List<Admin_Role>  admin_roles= adminService.findAllAdmin(page, limit);
        resBody.setCount(count);
        resBody.setData(admin_roles);
        resBody.setCode(0);
        return resBody;
    }

    @GetMapping("/getAdminsByRoleId")
    @ResponseBody
    @ApiOperation("获取管理员的table接口")
    public ResBody getAdminsByRoleId(@ApiParam("角色Id") @RequestParam int role_id,
                                     @ApiParam("当前页码") @RequestParam int page,
                                     @ApiParam("每页数据量") @RequestParam int limit) {
        ResBody resBody = new ResBody();
        int count = adminService.getCount(role_id);
        List<Admin_Role>  admin_roles= adminService.findAllAdmin(role_id, page, limit);
        resBody.setCount(count);
        resBody.setData(admin_roles);
        resBody.setCode(0);
        return resBody;
    }

    @ApiOperation("添加管理员")
    @ResponseBody
    @PostMapping(value = "/addAdminByAjax", produces = "application/json;charset=UTF-8")
    public ResBody addLogByAjax(@RequestBody Map<String, Object> params) {
        ResBody resBody = new ResBody();
        String role = params.get("role").toString();
        String name = params.get("name").toString();
        String sex = params.get("sex").toString();
        String src = params.get("src").toString();
        String phone = params.get("phone").toString();
        String email = params.get("email").toString();
        adminService.addAdmin(role,name,email,phone,src,sex);
        //int i = adminService.addAdmin(log);
        int i = 1;
        if (i == 1) {
            resBody.setCode(200);
        } else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @DeleteMapping("/delAdmin/{id}")
    @ResponseBody
    @ApiOperation("删除管理员")
    public ResBody moreLog(@PathVariable("id") @ApiParam("员工ID") String id) {
        ResBody resBody = new ResBody();
        String[] s = id.split(",");
        for (int i = 0; i < s.length; i++) {
            adminService.deleteAdminById(Integer.parseInt(s[i]));
        }
        resBody.setCode(200);
        return resBody;
    }


    @GetMapping("/addAdmin")
    @ApiOperation("添加管理员页面")
    public String addAdmin(){
        return "user/administrators/adminform";
    }

    @GetMapping("/moreAdmin/{id}")
    @ApiOperation("查看管理员详情页面")
    public String moreAdmin(@PathVariable("id")@ApiParam("管理员ID") int id, Model model){
        Admin admin = adminService.findAdminById(id);
        Role role = roleService.findRoleByAdmin(admin);
        model.addAttribute("adm",admin);
        model.addAttribute("role",role);
        return "user/administrators/adminform";
    }
}
