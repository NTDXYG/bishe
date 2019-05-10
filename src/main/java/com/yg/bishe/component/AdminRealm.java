package com.yg.bishe.component;

import com.yg.bishe.bean.Admin;
import com.yg.bishe.bean.Menu;
import com.yg.bishe.bean.Role;
import com.yg.bishe.service.AdminService;
import com.yg.bishe.service.MenuService;
import com.yg.bishe.service.RoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AdminRealm extends AuthorizingRealm {

    private final Logger logger = LoggerFactory.getLogger(AdminRealm.class);

    @Autowired
    AdminService adminService;
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;

    /**
     * 授权逻辑
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        Admin admin = (Admin) subject.getPrincipal();
        Role role = roleService.findRoleByAdmin(admin);
        List<Menu> list = menuService.findMenuByRole(role);
        for (Menu menu:list){
            info.addStringPermission(menu.getPermission());
        }
        return info;
    }

    /**
     * 执行认证逻辑
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        Admin admin = adminService.findAdminByName(token.getUsername());
        if (!token.getUsername().equals(admin.getName())){
            //用户名不存在
            return null;
        }
        //2、判断密码
        return new SimpleAuthenticationInfo(admin,admin.getPassword(),"");
    }
}