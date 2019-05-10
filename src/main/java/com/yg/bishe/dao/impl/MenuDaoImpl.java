package com.yg.bishe.dao.impl;

import com.yg.bishe.bean.Menu;
import com.yg.bishe.bean.Role;
import com.yg.bishe.dao.MenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MenuDaoImpl implements MenuDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public List<Menu> getMenus() {
        return jdbcTemplate.query("select * from menu ", new BeanPropertyRowMapper(Menu.class));
    }

    @Override
    public List<Menu> getMenusId(int id) {
        return jdbcTemplate.query("SELECT menu_id as id FROM role_menu WHERE role_id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(Menu.class));
    }

    @Override
    public List<Menu> findMenuByRole(Role role) {
        return jdbcTemplate.query("SELECT menu.`permission` FROM menu,role,role_menu WHERE menu.`id` = role_menu.`menu_id`" +
                " AND role.`id` = role_menu.`role_id`\n" +
                "AND role.`id` = ?", new Object[]{role.id}, new BeanPropertyRowMapper<>(Menu.class));
    }
}
