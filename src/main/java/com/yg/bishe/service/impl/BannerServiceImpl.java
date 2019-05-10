package com.yg.bishe.service.impl;

import com.yg.bishe.bean.Banner;
import com.yg.bishe.dao.BannerDao;
import com.yg.bishe.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    BannerDao bannerDao;

    @Override
    public int getCount() {
        return bannerDao.getCount();
    }

    @Override
    public List<Banner> findAllBanner(int page, int limit) {
        return bannerDao.findAllBanner(page, limit);
    }

    @Override
    public List<Banner> findAllBanner(int status, int page, int limit) {
        return bannerDao.findAllBanner(status, page, limit);
    }

    @Override
    public int getCount(int status) {
        return bannerDao.getCount(status);
    }

    @Override
    public void deleteBannerById(int id) {
        bannerDao.deleteBannerById(id);
    }

    @Override
    public Banner findBannerById(int id) {
        return bannerDao.findBannerById(id);
    }

    @Override
    public int addBannerByAjax(Banner banner) {
        return bannerDao.addBannerByAjax(banner);
    }

    @Override
    public int updateBanner(Banner banner) {
        return bannerDao.updateBanner(banner);
    }
}
