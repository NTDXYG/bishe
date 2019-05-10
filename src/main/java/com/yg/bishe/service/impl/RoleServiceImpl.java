package com.yg.bishe.service.impl;

import com.yg.bishe.bean.Admin;
import com.yg.bishe.bean.Menu;
import com.yg.bishe.bean.Role;
import com.yg.bishe.dao.RoleDao;
import com.yg.bishe.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDao roleDao;

    @Override
    public Role findRoleByAdmin(Admin admin) {
        return roleDao.findRoleByAdmin(admin);
    }

    @Override
    public List<Role> getRoleList() {
        return roleDao.getRoleList();
    }

    @Override
    public Role findRoleByName(String role) {
        return roleDao.findRoleByName(role);
    }

    @Override
    public Role findRoleById(int role_id) {
        return roleDao.findRoleById(role_id);
    }

    @Override
    public List<Menu> getMenusById(Integer id) {
        return roleDao.getMenusById(id);
    }

    @Override
    public void updateMenu(String role_id, String id) {
        roleDao.updateMenu(role_id,id);
    }

    @Override
    public int updateRole(int id, String name, String describe) {
        return roleDao.updateRole(id,name,describe);
    }

    @Override
    public int deleteRoleById(int i) {
        return roleDao.deleteRoleById(i);
    }

    @Override
    public int addRole(String name, String describe) {
        return roleDao.addRole(name,describe);
    }
}
