package com.yg.bishe.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
    public int id;
    public String avatar;
    public String nickname;
    public String content;
    public String img;
    public String time;
}
