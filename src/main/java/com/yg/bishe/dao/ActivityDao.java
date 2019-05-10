package com.yg.bishe.dao;

import com.yg.bishe.bean.*;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int getTagCount();

    List<Tag> findAllTag(int page, int limit);

    void deleteTagById(int id);

    int addTagByAjax(Tag tag);

    int updateTag(Tag tag);

    Tag findTagById(int id);

    int getActivityCount();

    List<Activity> findAllActivity(int page, int limit);

    List<Activity> getActivityList();

    int getCommentCount();

    List<Comment> findAllComment(int page, int limit);

    int getCommentImgCount();

    List<Comment> findAllCommentImg(int page, int limit);

    List findTags();

    int addActivityAndTagAndUser(Activity activity, String tags, String uuid);

    List<Activity> findAllActivity(int page, int limit, String begin, String end);

    int getActivityCount(String begin, String end);

    void deleteActivityById(int id);

    Activity findActivityById(int id);

    List<Tag> findTagByActivityId(int id);

    User findUserByActivityId(int activityId);

    Room findRoomById(int roomId);

    void updateActivityAndRoom(Activity activity, int roomId);

    List<Map> findAllActivityByWx();

    Map findActivityDetailByWx(int id);

    List<User_Activity> findAllUserByActivityId(int id);

    List<Comment> findAllCommentByActivityId(int id);

    User_Activity findUserActivityByActivityId(int id, String uuid);

    List<Map> findAllJoinActivityByWx(String uuid);

    List<Map> findAllApplyActivityByWx(String uuid);

    List<Map> findAllLoveActivityByWx(String uuid);

    int updateHotActivity(Activity activity, String tag);

    int setIsLove(String uuid, int id);

    int cancelIsLove(String uuid, int id);

    int findPeopleByActivityId(int id);

    void joinActivity(String uuid, int id);

    Activity findActivityByRoomId(int roomid);
}
