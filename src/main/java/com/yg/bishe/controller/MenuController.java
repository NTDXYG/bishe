package com.yg.bishe.controller;

import com.yg.bishe.bean.Menu;
import com.yg.bishe.bean.ResBody;
import com.yg.bishe.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;

@Controller
@Api(tags = {"菜单接口"})
public class MenuController {
    @Autowired
    MenuService menuService;

    @ApiOperation("获取菜单列表")
    @ResponseBody
    @GetMapping("/menu/getMenu")
    public ResBody getMenu(){
        ResBody resBody = new ResBody();
        List<Menu> menus = menuService.getMenus();
        List<Menu> list = new LinkedList<>();
        for (int i = 0;i<menus.size();i++){
            if (menus.get(i).getParent_id() == 0){
                List<Menu> list2 = new LinkedList<>();
                for (int j = 0;j<menus.size();j++){
                    if (menus.get(j).getParent_id() == menus.get(i).getId()){
                        List<Menu> list3 = new LinkedList<>();
                        for (int k = 0;k<menus.size();k++){
                            if (menus.get(k).getParent_id() == menus.get(j).getId()){
                                list3.add(menus.get(k));
                            }
                        }
                        menus.get(j).setChildren(list3);
                        list2.add(menus.get(j));
                    }
                }
                menus.get(i).setChildren(list2);
                list.add(menus.get(i));
            }
        }
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

    @ApiOperation("获取角色的菜单列表")
    @ResponseBody
    @GetMapping("/menu/getMenu/{id}")
    public ResBody getMenuByAdminId(@PathVariable("id")@ApiParam("角色id") Integer id){
        ResBody resBody = new ResBody();
        List<Menu> menus = menuService.getMenus();
        List<Menu> list = new LinkedList<>();
        List<Menu> menus_ids = menuService.getMenusId(id);
        List<Integer> ids = new LinkedList();
        for (Menu m:menus_ids){
            ids.add(m.getId());
        }
        for (int i = 0;i<menus.size();i++){
            if (menus.get(i).getParent_id() == 0){
                List<Menu> list2 = new LinkedList<>();
                for (int j = 0;j<menus.size();j++){
                    if (menus.get(j).getParent_id() == menus.get(i).getId()){
                        List<Menu> list3 = new LinkedList<>();
                        for (int k = 0;k<menus.size();k++){
                            if (menus.get(k).getParent_id() == menus.get(j).getId()){
                                if (ids.contains(menus.get(k).getId())){
                                    menus.get(k).setChecked(true);
                                }
                                list3.add(menus.get(k));
                            }
                        }
                        menus.get(j).setChildren(list3);
                        if (menus.get(j).getChildren().isEmpty() && ids.contains(menus.get(j).getId())){
                            menus.get(j).setChecked(true);
                        }
                        list2.add(menus.get(j));
                    }
                }
                menus.get(i).setChildren(list2);
                list.add(menus.get(i));
            }
        }
        resBody.setData(list);
        resBody.setCode(0);
        return resBody;
    }

}
