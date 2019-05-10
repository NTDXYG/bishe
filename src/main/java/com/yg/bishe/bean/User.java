package com.yg.bishe.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public String uuid;
    public String openid;
    public String nickname;
    public String avatarUrl;
    public int gender;
    public String qrcode;
    public String email;
    public String card;
    public String card_back;
    public int isuse;
}
