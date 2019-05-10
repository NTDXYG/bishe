package com.yg.bishe.dao.impl;

import com.yg.bishe.bean.Banner;
import com.yg.bishe.dao.BannerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BannerDaoImpl implements BannerDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int getCount() {
        int count = jdbcTemplate.queryForObject("select count(*) from banner", Integer.class);
        return count;
    }

    @Override
    public List<Banner> findAllBanner(int page, int limit) {
        List<Banner> logs = jdbcTemplate.query("select * from banner limit ?,?" ,new Object[]{(page-1)*limit,limit}, new BeanPropertyRowMapper(Banner.class));
        if (logs!=null){
            return logs;
        }else{
            return null;
        }
    }

    @Override
    public List<Banner> findAllBanner(int status, int page, int limit) {
        List<Banner> logs = jdbcTemplate.query("select * from banner where status = ? limit ?,?" ,new Object[]{status,(page-1)*limit,limit}, new BeanPropertyRowMapper(Banner.class));
        if (logs!=null){
            return logs;
        }else{
            return null;
        }
    }

    @Override
    public int getCount(int status) {
        int count = jdbcTemplate.queryForObject("select count(*) from banner where status = ?",new Object[]{status}, Integer.class);
        return count;
    }

    @Override
    public void deleteBannerById(int id) {
        jdbcTemplate.update("DELETE from banner where id=?",id);
    }

    @Override
    public Banner findBannerById(int id) {
        List<Banner> list = jdbcTemplate.query("select * from banner where id" +
                " = ?" ,new Object[]{id}, new BeanPropertyRowMapper(Banner.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public int addBannerByAjax(Banner banner) {
        return jdbcTemplate.update("insert into banner values(null, ?, ?, ?, ?)",
                banner.getSrc(),banner.getUrl(),banner.getDescribe(),banner.getStatus());
    }

    @Override
    public int updateBanner(Banner banner) {
        return jdbcTemplate.update("update banner set src = ?,url = ?,`describe` = ?,status = ? where id = ?",
                banner.getSrc(),banner.getUrl(),banner.getDescribe(),banner.getStatus(),banner.getId());
    }
}
