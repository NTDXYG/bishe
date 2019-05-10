package com.yg.bishe.service;

import com.yg.bishe.bean.Admin;
import com.yg.bishe.bean.Menu;
import com.yg.bishe.bean.Role;

import java.util.List;

public interface RoleService {
    Role findRoleByAdmin(Admin admin);

    List<Role> getRoleList();

    Role findRoleByName(String role);

    Role findRoleById(int role_id);

    List<Menu> getMenusById(Integer id);

    void updateMenu(String role_id, String id);

    int updateRole(int id, String name, String describe);

    int deleteRoleById(int i);

    int addRole(String name, String describe);
}
