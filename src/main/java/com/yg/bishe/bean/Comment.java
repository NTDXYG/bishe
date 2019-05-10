package com.yg.bishe.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    public int id;
    public String content;
    public String img;
    public String time;
    public int isimg;
    public String user_id;
    public String activity_id;
    public User user;
    public Activity activity;
}
