package com.yg.bishe.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    public int id;
    public String title;
    public String content;
    public String img;
    public String begin;
    public String end;
    public int people;
    public String local;
    public String organization;
    public int ishot;
    public int status;
    public int category;
}
