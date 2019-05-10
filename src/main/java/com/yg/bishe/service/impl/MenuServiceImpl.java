package com.yg.bishe.service.impl;

import com.yg.bishe.bean.Menu;
import com.yg.bishe.bean.Role;
import com.yg.bishe.dao.MenuDao;
import com.yg.bishe.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    MenuDao menuDao;

    @Override
    public List<Menu> getMenus() {
        return menuDao.getMenus();
    }

    @Override
    public List<Menu> getMenusId(int id) {
        return menuDao.getMenusId(id);
    }

    @Override
    public List<Menu> findMenuByRole(Role role) {
        return menuDao.findMenuByRole(role);
    }
}
