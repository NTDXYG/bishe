package com.yg.bishe.dao.impl;

import com.yg.bishe.bean.User;
import com.yg.bishe.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public int getCount() {
        int count = jdbcTemplate.queryForObject("select count(*) from `user`", Integer.class);
        return count;
    }

    @Override
    public List<User> findAllUser(int page, int limit) {
            List<User> users = jdbcTemplate.query("select * from `user` limit ?,?" ,new Object[]{(page-1)*limit,limit}, new BeanPropertyRowMapper(User.class));
        if (users!=null){
            return users;
        }else{
            return null;
        }
    }

    @Override
    public void deleteUserById(String uuid) {
        jdbcTemplate.update("delete from `user` where uuid=?",uuid);
    }

    @Override
    public int addUserByAjax(User user) {
        return 0;
    }

    @Override
    public int updateUser(User user) {
        return 0;
    }

    @Override
    public User findUserById(String uuid) {
        List<User> list = jdbcTemplate.query("select * from `user` where uuid" +
                " = ?" ,new Object[]{uuid}, new BeanPropertyRowMapper(User.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public User findUserByOpenId(String openid, String nickname, int gender, String avatarUrl) {
        List<User> list = jdbcTemplate.query("select * from `user` where openid" +
                " = ?" ,new Object[]{openid}, new BeanPropertyRowMapper(User.class));
        if (list!=null && list.size()>0){
            jdbcTemplate.update("update `user` set nickname = ?, gender = ?,avatarUrl = ? where openid = ?",
                    nickname,gender,avatarUrl,openid);
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public int isSign(String roomid, String uuid) {
        int count = jdbcTemplate.queryForObject("select count(*) from `user_activity` where user_id = ? and activity_id = ? and isSign = 1;",
                new Object[]{uuid,roomid},Integer.class);
        return count;
    }

    @Override
    public void sign(String roomid, String uuid) {
        jdbcTemplate.update("UPDATE `user_activity` SET isSign = 1 WHERE user_id = ? and activity_id = ?;", uuid, roomid);
    }


    @Override
    public String addUserByWX(String openid, String nickname, int gender, String avatarUrl) {
        String uuid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into `user`(openid,uuid,nickname,gender,avatarUrl,isuse) values(?, ?, ?, ?, ?,0)",
                openid,uuid,nickname,gender,avatarUrl);
        return uuid;
    }

    @Override
    public int uploadUserByWx(String uuid, String email, String card, String card_back, String qrcode) {
        return jdbcTemplate.update("update `user` set email = ?, card = ?,card_back = ?, qrcode = ?, isuse = 1 where uuid = ?",
                email,card,card_back,qrcode,uuid);
    }

    @Override
    public int updateUser(String uuid) {
        return jdbcTemplate.update("update `user` set isuse = 2 where uuid = ?",
                uuid);
    }

    @Override
    public List<User> getUserList() {
        List<User> list = jdbcTemplate.query("select * from user" , new BeanPropertyRowMapper(User.class));
        if (list!=null){
            return list;
        }else{
            return null;
        }
    }




}
