package com.yg.bishe.service.impl;

import com.yg.bishe.bean.EchartsData;
import com.yg.bishe.bean.Log;
import com.yg.bishe.dao.LogDao;
import com.yg.bishe.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    LogDao dao;
    @Override
    public int deleteLogById(int id) {
        return dao.deleteLogById(id);
    }

    @Override
    public int addLog(Log log) {
        return dao.addLog(log);
    }

    @Override
    public List<Log> findAllLog(int page, int limit) {
        return dao.findAllLog(page,limit);
    }

    @Override
    public int getCount() {
        return dao.getCount();
    }

    @Override
    public int getCount(String begin, String end) {
        return dao.getCount(begin, end);
    }

    @Override
    public List<Log> findAllLog(int page, int limit, String begin, String end) {
        return dao.findAllLog(page,limit,begin,end);
    }

    @Override
    public Log findLogById(Integer id) {
        return dao.findLogById(id);
    }

    @Override
    public int updateById(Log log) {
        return dao.updateById(log);
    }

    @Override
    public List<EchartsData> getData() {
        return dao.getData();
    }

    @Override
    public List<EchartsData> getLoginData(String mode) {
        return dao.getLoginData(mode);
    }
}
