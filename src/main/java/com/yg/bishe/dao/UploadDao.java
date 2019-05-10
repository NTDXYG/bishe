package com.yg.bishe.dao;

import com.yg.bishe.bean.File;

import java.util.List;

public interface UploadDao {
    int addFile(String url,String name);

    List<File> getFile(int page, int limit);

    int deleteById(int id);

    int getCount();
}
