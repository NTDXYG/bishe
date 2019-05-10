package com.yg.bishe.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    public int id;
    public String name;
    public String password;
    public String email;
    public String phone;
    public String src;
    public String sex;

}