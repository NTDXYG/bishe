package com.yg.bishe.bean;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    public int id;//id
    public String label;//名字
    public boolean checked;//是否启用
    public String permission;//Shiro权限
    public List<Menu> children;//子菜单
    public int parent_id;
}
