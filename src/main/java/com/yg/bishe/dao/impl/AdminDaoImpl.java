package com.yg.bishe.dao.impl;

import com.yg.bishe.bean.Admin;
import com.yg.bishe.bean.Admin_Role;
import com.yg.bishe.bean.Role;
import com.yg.bishe.dao.AdminDao;
import com.yg.bishe.service.AdminService;
import com.yg.bishe.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;

import java.util.LinkedList;
import java.util.List;

@Repository
public class AdminDaoImpl implements AdminDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    RoleService roleService;
    @Autowired
    AdminService adminService;

    @Override
    public Admin findAdminByPassword(String name, String password) {
        List<Admin> list = jdbcTemplate.query("select * from admin where name = ? && password = ?" ,
                new Object[]{name,password}, new BeanPropertyRowMapper(Admin.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Admin findAdminByPhone(String phone) {
        List<Admin> list = jdbcTemplate.query("select * from admin where phone" +
                " = ?" ,new Object[]{phone}, new BeanPropertyRowMapper(Admin.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<Admin> getAdmin(String page, String limit) {
        return null;
    }

    @Override
    public int getCount() {
        int count = jdbcTemplate.queryForObject("select count(*) from admin", Integer.class);
        return count;
    }

    @Override
    public Admin findAdminByName(String name) {
        List<Admin> list = jdbcTemplate.query("select * from admin where name" +
                " = ?" ,new Object[]{name}, new BeanPropertyRowMapper(Admin.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public int updateAdmin(String sex, String src, String phone, String email, String name) {
        return jdbcTemplate.update("update admin set sex = ?,src = ?,phone = ?,email = ? where name = ?",
                sex,src,phone,email,name);
    }

    @Override
    public int updateAdmin(String password, String name) {
        return jdbcTemplate.update("update admin set password = ?where name = ?",
                DigestUtils.md5DigestAsHex(password.getBytes()),name);
    }

    @Override
    public List<Admin_Role> findAllAdmin(int page, int limit) {
        List<Admin> admins = jdbcTemplate.query("select * from admin limit ?,?" ,new Object[]{(page-1)*limit,limit}, new BeanPropertyRowMapper(Admin.class));
        List<Admin_Role> list = new LinkedList<>();
        if (admins!=null){
            for (Admin admin:admins){
                Role role = roleService.findRoleByAdmin(admin);
                list.add(new Admin_Role(admin,role));
            }
            return list;
        }else{
            return null;
        }
    }

    @Override
    public int getCount(int role_id) {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM admin,admin_role WHERE admin.`id` = admin_role.`admin_id` AND admin_role.`role_id` = ?"
                ,new Object[]{role_id} , Integer.class);
        return count;
    }

    @Override
    public List<Admin_Role> findAllAdmin(int role_id, int page, int limit) {
        List<Admin> admins = jdbcTemplate.query("select * from admin,admin_role WHERE admin.`id` = admin_role.`admin_id` AND admin_role.`role_id` = ? limit ?,?"
                ,new Object[]{role_id,(page-1)*limit,limit}, new BeanPropertyRowMapper(Admin.class));
        List<Admin_Role> list = new LinkedList<>();
        if (admins!=null){
            for (Admin admin:admins){
                Role role = roleService.findRoleByAdmin(admin);
                list.add(new Admin_Role(admin,role));
            }
            return list;
        }else{
            return null;
        }
    }

    @Override
    public Admin findAdminById(int id) {
        List<Admin> list = jdbcTemplate.query("select * from admin where id" +
                " = ?" ,new Object[]{id}, new BeanPropertyRowMapper(Admin.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public int deleteAdminById(int id) {
        jdbcTemplate.update("delete from log where `name` = ?",adminService.findAdminById(id).getName());
        return jdbcTemplate.update("DELETE from admin where id=?",id);
    }

    @Override
    public int addAdmin(String role, String name, String email, String phone, String src, String sex) {
        jdbcTemplate.update("insert into admin values(null, ?, ?, ?, ?, ?, ?)",
                name,DigestUtils.md5DigestAsHex("123456".getBytes()),email,phone,src,sex);
        Admin admin = adminService.findAdminByName(name);
        Role rol = roleService.findRoleById(Integer.parseInt(role));
        return jdbcTemplate.update("insert into admin_role values(?, ?)",admin.getId(),rol.getId());
    }

    @Override
    public int updateRole(int admin_id, int role_id) {
        return jdbcTemplate.update("UPDATE admin_role SET role_id = ? WHERE admin_id = ?",role_id,admin_id);
    }
}