package com.yg.bishe.service.impl;

import com.yg.bishe.bean.File;
import com.yg.bishe.dao.UploadDao;
import com.yg.bishe.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    UploadDao uploadDao;

    @Override
    public int addFile(String url, String name) {
        return uploadDao.addFile(url, name);
    }

    @Override
    public List<File> getFile(int page, int limit) {
        return uploadDao.getFile(page,limit);
    }

    @Override
    public int deleteById(int id) {
        return uploadDao.deleteById(id);
    }

    @Override
    public int getCount() {
        return uploadDao.getCount();
    }
}
