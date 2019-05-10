package com.yg.bishe.service;

import com.yg.bishe.bean.EchartsData;
import com.yg.bishe.bean.Log;

import java.util.List;

public interface LogService {
    int deleteLogById(int id);

    int addLog(Log log);

    List<Log> findAllLog(int page, int limit);

    int getCount();

    int getCount(String begin, String end);

    List<Log> findAllLog(int page, int limit, String begin, String end);

    Log findLogById(Integer id);

    int updateById(Log log);

    List<EchartsData> getData();

    List<EchartsData> getLoginData(String mode);
}
