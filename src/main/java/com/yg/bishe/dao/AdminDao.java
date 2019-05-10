package com.yg.bishe.dao;

import com.yg.bishe.bean.Admin;
import com.yg.bishe.bean.Admin_Role;

import java.util.List;

public interface AdminDao {
    Admin findAdminByPassword(String name,String password);

    Admin findAdminByPhone(String phone);

    List<Admin> getAdmin(String page,String limit);

    int getCount();

    Admin findAdminByName(String name);

    int updateAdmin(String sex, String src, String phone, String email, String name);

    int updateAdmin(String password, String name);

    List<Admin_Role> findAllAdmin(int page, int limit);

    int getCount(int role_id);

    List<Admin_Role> findAllAdmin(int role_id, int page, int limit);

    Admin findAdminById(int id);

    int deleteAdminById(int id);

    int addAdmin(String role, String name, String email, String phone, String src, String sex);

    int updateRole(int admin_id, int role_id);
}