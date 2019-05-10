package com.yg.bishe.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.yg.bishe.component.AdminRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    /*
     * ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //添加过滤器
        /**
         * anon：无需认证（登录）可以访问
         * authc：必须认证（登录）才可以访问
         * user：使用rememberme可以直接访问
         * perms：必须得到资源权限才可以访问
         * role：必须得到角色权限才可以访问
         */
        shiroFilterFactoryBean.setLoginUrl("/login");
        Map<String, String> filterMap = new LinkedHashMap<String, String>();
        //登录页相关URI无需认证
        filterMap.put("/logout", "logout");
        filterMap.put("/login", "anon");
        filterMap.put("/getCode", "anon");
        filterMap.put("/SendSMS", "anon");
        filterMap.put("/admin/loginByPassword", "anon");
        filterMap.put("/admin/loginByPhone", "anon");

        //授权过滤器
        filterMap.put("/logMonitor", "perms[admin:logMonitor]");
        //filterMap.put("/*", "authc");
        //菜单权限
        filterMap.put("/", "authc");
        filterMap.put("/index", "authc");
        filterMap.put("/console", "authc");
        filterMap.put("/swagger", "authc");
        filterMap.put("/meetingList", "authc");
        filterMap.put("/tag", "authc");
        filterMap.put("/comment", "authc");
        filterMap.put("/topicList", "authc");
        filterMap.put("/replayList", "authc");
        filterMap.put("/popRecommend", "authc");
        filterMap.put("/priRecommend", "authc");
        filterMap.put("/message", "authc");
        filterMap.put("/banner", "authc");
        filterMap.put("/rtmp", "authc");
        filterMap.put("/webrtc", "authc");
        filterMap.put("/user/list", "authc");
        filterMap.put("/admin/list", "authc");
        filterMap.put("/role/list", "authc");
        filterMap.put("/springbootadmin", "authc");
        filterMap.put("/rabbitmqMonitor", "authc");
        filterMap.put("/druidMonitor", "authc");
        filterMap.put("/logMonitor", "authc");
        filterMap.put("/webSetting", "authc");
        filterMap.put("/fileManagement", "authc");
        filterMap.put("/sensitiveWord", "authc");
        filterMap.put("/basicData", "authc");
        filterMap.put("/updateAdminPass", "authc");
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/401");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }


    /**
     * DefaultWebSecurityManager
     */

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("adminRealm") AdminRealm adminRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联realm
        securityManager.setRealm(adminRealm);
        return securityManager;
    }

    /**
     * Realm
     */

    @Bean(name = "adminRealm")
    public AdminRealm getRealm() {
        return new AdminRealm();
    }

    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
}
