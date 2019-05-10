package com.yg.bishe.dao.impl;

import com.yg.bishe.bean.Admin;
import com.yg.bishe.bean.Menu;
import com.yg.bishe.bean.Role;
import com.yg.bishe.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Role findRoleByAdmin(Admin admin) {
        List<Role> list = jdbcTemplate.query("SELECT role.`id`,role.`name` \n" +
                "FROM role,admin,admin_role \n" +
                "WHERE admin.`id`=admin_role.`admin_id` AND role.`id` = admin_role.`role_id`AND admin.`id` = ?" ,
                new Object[]{admin.getId()}, new BeanPropertyRowMapper(Role.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<Role> getRoleList() {
        List<Role> list = jdbcTemplate.query("select * from role" , new BeanPropertyRowMapper(Role.class));
        if (list!=null){
            return list;
        }else{
            return null;
        }
    }

    @Override
    public Role findRoleByName(String role) {
        List<Role> list = jdbcTemplate.query("SELECT * FROM role WHERE role.`name` = ?" ,
                new Object[]{role}, new BeanPropertyRowMapper(Role.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Role findRoleById(int role_id) {
        List<Role> list = jdbcTemplate.query("SELECT * FROM role WHERE role.`id` = ?" ,
                new Object[]{role_id}, new BeanPropertyRowMapper(Role.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<Menu> getMenusById(Integer id) {
        return jdbcTemplate.query("SELECT menu_id FROM role_menu WHERE role_id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Menu.class));
    }

    @Override
    public void updateMenu(String role_id, String id) {
        String[] s = id.split(",");
        jdbcTemplate.update("DELETE FROM role_menu WHERE role_id = ?",role_id);
        for (int i = 0; i < s.length; i++) {
            jdbcTemplate.update("INSERT INTO role_menu VALUES (?, ?)",role_id,Integer.parseInt(s[i]));
        }
    }

    @Override
    public int updateRole(int id, String name, String describe) {
        return jdbcTemplate.update("update role set name = ?,`describe` = ? where id = ?",
                name,describe,id);
    }

    @Override
    public int deleteRoleById(int i) {
        return jdbcTemplate.update("DELETE from role where id=?",i);
    }

    @Override
    public int addRole(String name, String describe) {
        return jdbcTemplate.update("insert into role values(null, ?, ?)",
                name,describe);
    }
}
