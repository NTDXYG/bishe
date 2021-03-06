package com.yg.bishe.dao;

import com.yg.bishe.bean.Banner;

import java.util.List;

public interface BannerDao {
    int getCount();

    List<Banner> findAllBanner(int page, int limit);

    List<Banner> findAllBanner(int status,int page, int limit);

    int getCount(int status);

    void deleteBannerById(int id);

    Banner findBannerById(int id);

    int addBannerByAjax(Banner banner);

    int updateBanner(Banner banner);
}
