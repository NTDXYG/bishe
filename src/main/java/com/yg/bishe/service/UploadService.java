package com.yg.bishe.service;

import com.yg.bishe.bean.File;

import java.util.List;

public interface UploadService {
    int addFile(String url, String name);

    List<File> getFile(int page, int limit);

    int deleteById(int id);

    int getCount();
}
