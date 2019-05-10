package com.yg.bishe.service.impl;

import com.yg.bishe.bean.*;
import com.yg.bishe.dao.ActivityDao;
import com.yg.bishe.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    ActivityDao dao;

    @Override
    public int getTagCount() {
        return dao.getTagCount();
    }

    @Override
    public List<Tag> findAllTag(int page, int limit) {
        return dao.findAllTag(page,limit);
    }

    @Override
    public void deleteTagById(int id) {
        dao.deleteTagById(id);
    }

    @Override
    public int addTagByAjax(Tag tag) {
        return dao.addTagByAjax(tag);
    }

    @Override
    public int updateTag(Tag tag) {
        return dao.updateTag(tag);
    }

    @Override
    public Tag findTagById(int id) {
        return dao.findTagById(id);
    }

    @Override
    public int getActivityCount() {
        return dao.getActivityCount();
    }

    @Override
    public List<Activity> findAllActivity(int page, int limit) {
        return dao.findAllActivity(page,limit);
    }

    @Override
    public List<Activity> getActivityList() {
        return dao.getActivityList();
    }

    @Override
    public int getCommentCount() {
        return dao.getCommentCount();
    }

    @Override
    public List<Comment> findAllComment(int page, int limit) {
        return dao.findAllComment(page,limit);
    }

    @Override
    public int getCommentImgCount() {
        return dao.getCommentImgCount();
    }

    @Override
    public List<Comment> findAllCommentImg(int page, int limit) {
        return dao.findAllCommentImg(page,limit);
    }

    @Override
    public List findTags() {
        return dao.findTags();
    }

    @Override
    public int addActivityAndTagAndUser(Activity activity, String tags, String uuid) {
        return dao.addActivityAndTagAndUser(activity,tags,uuid);
    }

    @Override
    public List<Activity> findAllActivity(int page, int limit, String begin, String end) {
        return dao.findAllActivity(page, limit, begin, end);
    }

    @Override
    public int getActivityCount(String begin, String end) {
        return dao.getActivityCount(begin, end);
    }

    @Override
    public void deleteActivityById(int id) {
        dao.deleteActivityById(id);
    }

    @Override
    public Activity findActivityById(int id) {
        return dao.findActivityById(id);
    }

    @Override
    public List<Tag> findTagByActivityId(int id) {
        return dao.findTagByActivityId(id);
    }

    @Override
    public User findUserByActivityId(int activityId) {
        return dao.findUserByActivityId(activityId);
    }

    @Override
    public Room findRoomById(int roomId) {
        return dao.findRoomById(roomId);
    }

    @Override
    public void updateActivityAndRoom(Activity activity, int roomId) {
        dao.updateActivityAndRoom(activity,roomId);
    }

    @Override
    public List<Map> findAllActivityByWx() {
        return dao.findAllActivityByWx();
    }

    @Override
    public Map findActivityDetailByWx(int id) {
        return dao.findActivityDetailByWx(id);
    }

    @Override
    public List<User_Activity> findAllUserByActivityId(int id) {
        return dao.findAllUserByActivityId(id);
    }

    @Override
    public List<Comment> findAllCommentByActivityId(int id) {
        return dao.findAllCommentByActivityId(id);
    }

    @Override
    public User_Activity findUserActivityByActivityId(int id, String uuid) {
        return dao.findUserActivityByActivityId(id, uuid);
    }

    @Override
    public List<Map> findAllJoinActivityByWx(String uuid) {
        return dao.findAllJoinActivityByWx(uuid);
    }

    @Override
    public List<Map> findAllApplyActivityByWx(String uuid) {
        return dao.findAllApplyActivityByWx(uuid);
    }

    @Override
    public List<Map> findAllLoveActivityByWx(String uuid) {
        return dao.findAllLoveActivityByWx(uuid);
    }

    @Override
    public int updateHotActivity(Activity activity, String tag) {
        return dao.updateHotActivity(activity, tag);
    }

    @Override
    public int setIsLove(String uuid, int id) {
        return dao.setIsLove(uuid,id);
    }

    @Override
    public int cancelIsLove(String uuid, int id) {
        return dao.cancelIsLove(uuid,id);
    }

    @Override
    public int findPeopleByActivityId(int id) {
        return dao.findPeopleByActivityId(id);
    }

    @Override
    public void joinActivity(String uuid, int id) {
        dao.joinActivity(uuid,id);
    }

    @Override
    public Activity findActivityByRoomId(int roomid) {
        return dao.findActivityByRoomId(roomid);
    }
}
