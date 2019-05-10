package com.yg.bishe.dao;

import com.yg.bishe.bean.User;

import java.util.List;

public interface UserDao {
    int getCount();

    List<User> findAllUser(int page, int limit);

    void deleteUserById(String uuid);

    int addUserByAjax(User user);

    int updateUser(User user);

    User findUserById(String uuid);


    String addUserByWX(String openid, String nickname, int gender, String avatarUrl);

    int uploadUserByWx(String uuid, String email, String card, String card_back, String qrcode);

    int updateUser(String uuid);

    List<User> getUserList();

    User findUserByOpenId(String openid, String nickname, int gender, String avatarUrl);

    int isSign(String roomid, String uuid);

    void sign(String roomid, String uuid);
}
