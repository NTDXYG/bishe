package com.yg.bishe.controller;

import com.yg.bishe.bean.Admin;
import com.yg.bishe.bean.Menu;
import com.yg.bishe.bean.ResBody;
import com.yg.bishe.bean.Role;
import com.yg.bishe.service.RoleService;
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
@Api(tags = {"角色接口"})
public class RoleController {

    @Autowired
    RoleService roleService;
    private final static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @ApiOperation("获取角色列表")
    @ResponseBody
    @GetMapping(value = "/role/selectRoleList")
    public List<Role> selectRoleList() {
        List<Role> list = roleService.getRoleList();
        return list;
    }

    @ApiOperation("获取角色的table接口")
    @ResponseBody
    @GetMapping(value = "/role/getAllRoles")
    public ResBody<Role> getAllRoles() {
        ResBody resBody = new ResBody();
        List<Role> list = roleService.getRoleList();
        resBody.setCode(0);
        resBody.setData(list);
        return resBody;
    }

    @ApiOperation("更新角色的权限菜单")
    @ResponseBody
    @PutMapping(value = "/role/updateMenu/{id}")
    public ResBody updateMenu(@PathVariable("id")@ApiParam("角色id") String id,
                              @RequestParam("role_id") String role_id) {
        roleService.updateMenu(role_id,id);
        ResBody resBody = new ResBody();
        resBody.setCode(200);
        return resBody;
    }

    @ApiOperation("添加角色信息")
    @ResponseBody
    @PostMapping(value = "/addRoleByAjax")
    public ResBody addRoleByAjax(@RequestBody Map<String, Object> params) {
        ResBody resBody = new ResBody();
        String name = params.get("role_name").toString();
        String describe = params.get("role_describe").toString();
        int i = roleService.addRole(name, describe);
        if (i == 1){
            resBody.setCode(200);
        }else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @ApiOperation("更新角色信息")
    @ResponseBody
    @PutMapping(value = "/role/updateRole")
    public ResBody updateRole(@RequestBody Map<String, Object> params) {
        ResBody resBody = new ResBody();
        int id = Integer.parseInt(params.get("role_id").toString());
        String name = params.get("role_name").toString();
        String describe = params.get("role_describe").toString();
        int i = roleService.updateRole(id, name, describe);
        if (i == 1){
            resBody.setCode(200);
        }else {
            resBody.setCode(500);
        }
        return resBody;
    }

    @DeleteMapping("/delRole/{id}")
    @ResponseBody
    @ApiOperation("删除角色")
    public ResBody moreLog(@PathVariable("id") @ApiParam("角色ID") String id) {
        ResBody resBody = new ResBody();
        String[] s = id.split(",");
        for (int i = 0; i < s.length; i++) {
            roleService.deleteRoleById(Integer.parseInt(s[i]));
        }
        resBody.setCode(200);
        return resBody;
    }


    @ApiOperation("跳转权限列表")
    @GetMapping(value = "/getMenu/{id}")
    public String getMenu(@PathVariable("id")@ApiParam("角色id") Integer id, Model model) {
        model.addAttribute("id",id);
        return "/user/administrators/menu";
    }

    @ApiOperation("跳转编辑角色页面")
    @GetMapping(value = "/moreRole/{id}")
    public String moreRole(@PathVariable("id")@ApiParam("角色id") Integer id, Model model) {
        Role role = roleService.findRoleById(id);
        model.addAttribute("rol",role);
        return "/user/administrators/roleform";
    }

    @ApiOperation("跳转添加角色页面")
    @GetMapping(value = "/addRole")
    public String addRole() {
        return "/user/administrators/roleform";
    }
}
