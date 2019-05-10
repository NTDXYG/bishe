package com.yg.bishe.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User_Activity {
    public String user_id;
    public User user;
    public Activity activity;
    public int isManage;
    public int isSign;
    public int isLove;
    public int isJoin;
}
