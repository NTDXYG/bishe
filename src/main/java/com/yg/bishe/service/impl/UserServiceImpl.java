package com.yg.bishe.service.impl;

import com.yg.bishe.bean.User;
import com.yg.bishe.dao.UserDao;
import com.yg.bishe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao dao;

    @Override
    public int getCount() {
        return dao.getCount();
    }

    @Override
    public List<User> findAllUser(int page, int limit) {
        return dao.findAllUser(page,limit);
    }

    @Override
    public void deleteUserById(String uuid) {
        dao.deleteUserById(uuid);
    }

    @Override
    public int addUserByAjax(User user) {
        return dao.addUserByAjax(user);
    }

    @Override
    public int updateUser(User user) {
        return dao.updateUser(user);
    }

    @Override
    public User findUserById(String uuid) {
        return dao.findUserById(uuid);
    }

    @Override
    public User findUserByOpenId(String openid, String nickname, int gender, String avatarUrl) {
        return dao.findUserByOpenId(openid,nickname,gender,avatarUrl);
    }

    @Override
    public String addUserByWX(String openid, String nickname, int gender, String avatarUrl) {
        return dao.addUserByWX(openid, nickname, gender, avatarUrl);
    }

    @Override
    public int uploadUserByWx(String uuid, String email, String card, String card_back, String qrcode) {
        return dao.uploadUserByWx(uuid,email,card,card_back,qrcode);
    }

    @Override
    public int updateUser(String uuid) {
        return dao.updateUser(uuid);
    }

    @Override
    public List<User> getUserList() {
        return dao.getUserList();
    }

    @Override
    public int isSign(String roomid, String uuid) {
        return dao.isSign(roomid, uuid);
    }

    @Override
    public void sign(String roomid, String uuid) {
        dao.sign(roomid,uuid);
    }

}
