package com.yg.bishe.service;

import com.yg.bishe.bean.Menu;
import com.yg.bishe.bean.Role;

import java.util.List;

public interface MenuService {
    List<Menu> getMenus();

    List<Menu> getMenusId(int id);

    List<Menu> findMenuByRole(Role role);
}
