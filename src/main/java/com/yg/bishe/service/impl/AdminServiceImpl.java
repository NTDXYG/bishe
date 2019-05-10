package com.yg.bishe.service.impl;

import com.yg.bishe.bean.Admin;
import com.yg.bishe.bean.Admin_Role;
import com.yg.bishe.dao.AdminDao;
import com.yg.bishe.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminDao adminDao;

    @Override
    public Admin findAdminByPassword(String name, String password) {
        Admin admin = adminDao.findAdminByPassword(name, password);
        return admin;
    }

    @Override
    public Admin findAdminByPhone(String phone) {
        Admin admin = adminDao.findAdminByPhone(phone);
        return admin;
    }

    @Override
    public int getCount() {
        return adminDao.getCount();
    }

    @Override
    public Admin findAdminByName(String name) {
        Admin admin = adminDao.findAdminByName(name);
        return admin;
    }

    @Override
    public int updateAdmin(String sex, String src, String phone, String email, String name) {
        return adminDao.updateAdmin(sex,src,phone,email,name);
    }

    @Override
    public int updateAdmin(String password, String name) {
        return adminDao.updateAdmin(password,name);
    }

    @Override
    public List<Admin_Role> findAllAdmin(int page, int limit) {
        return adminDao.findAllAdmin(page,limit);
    }

    @Override
    public int getCount(int role_id) {
        return adminDao.getCount(role_id);
    }

    @Override
    public List<Admin_Role> findAllAdmin(int role_id, int page, int limit) {
        return adminDao.findAllAdmin(role_id,page,limit);
    }

    @Override
    public Admin findAdminById(int id) {
        return adminDao.findAdminById(id);
    }

    @Override
    public int deleteAdminById(int id) {
        return adminDao.deleteAdminById(id);
    }

    @Override
    public int addAdmin(String role, String name, String email, String phone, String src, String sex) {
        return adminDao.addAdmin(role,name,email,phone,src,sex);
    }

    @Override
    public int updateRole(int admin_id, int role_id) {
        return adminDao.updateRole(admin_id,role_id);
    }
}