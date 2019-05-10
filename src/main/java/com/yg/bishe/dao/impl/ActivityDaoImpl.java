package com.yg.bishe.dao.impl;

import com.yg.bishe.bean.*;
import com.yg.bishe.dao.ActivityDao;
import com.yg.bishe.service.ActivityService;
import com.yg.bishe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class ActivityDaoImpl implements ActivityDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserService userService;
    @Autowired
    ActivityService activityService;

    @Override
    public int getTagCount() {
        int count = jdbcTemplate.queryForObject("select count(*) from tag", Integer.class);
        return count;
    }

    @Override
    public List<Tag> findAllTag(int page, int limit) {
        List<Tag> tags = jdbcTemplate.query("select * from tag limit ?,?", new Object[]{(page - 1) * limit, limit}, new BeanPropertyRowMapper(Tag.class));
        if (tags != null) {
            return tags;
        } else {
            return null;
        }
    }

    @Override
    public void deleteTagById(int id) {
        jdbcTemplate.update("DELETE from tag where id=?", id);
    }

    @Override
    public int addTagByAjax(Tag tag) {
        return jdbcTemplate.update("insert into tag values(null, ?, ?)",
                tag.getName(), tag.getColor());
    }

    @Override
    public int updateTag(Tag tag) {
        return jdbcTemplate.update("update tag set `name` = ?,color = ? where id = ?",
                tag.getName(), tag.getColor(), tag.getId());
    }

    @Override
    public Tag findTagById(int id) {
        List<Tag> list = jdbcTemplate.query("select * from tag where id" +
                " = ?", new Object[]{id}, new BeanPropertyRowMapper(Tag.class));
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public int getActivityCount() {
        int count = jdbcTemplate.queryForObject("select count(*) from activity where ishot=0", Integer.class);
        return count;
    }

    @Override
    public List<Activity> findAllActivity(int page, int limit, String begin, String end) {
        List<Activity> activities = jdbcTemplate.query("select * from activity where ishot=0 " +
                "and `begin`between ? and ? limit ?,? ", new Object[]{begin, end, (page - 1) * limit, limit}, new BeanPropertyRowMapper(Activity.class));
        if (activities != null) {
            return activities;
        } else {
            return null;
        }
    }

    @Override
    public int getActivityCount(String begin, String end) {
        int count = jdbcTemplate.queryForObject("select count(*) from activity where ishot=0 and `begin` between  ? and ?",
                new Object[]{begin, end},Integer.class);
        return count;
    }

    @Override
    public void deleteActivityById(int id) {
        jdbcTemplate.update("DELETE from activity where id=?", id);
    }

    @Override
    public Activity findActivityById(int id) {
        List<Activity> list = jdbcTemplate.query("select * from activity where id" +
                " = ?", new Object[]{id}, new BeanPropertyRowMapper(Activity.class));
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Tag> findTagByActivityId(int id) {
        List<Tag> list = new LinkedList<>();
        List<Tag> tagList = jdbcTemplate.query("SELECT tag_id as id FROM `activity_tag` WHERE activity_id = ?"
                , new Object[]{id}, new BeanPropertyRowMapper(Tag.class));
        if (tagList != null && tagList.size() > 0) {
            for (Tag tagId : tagList){
                List<Tag> tags = jdbcTemplate.query("select * from tag where id" +
                        " = ?", new Object[]{tagId.getId()}, new BeanPropertyRowMapper(Tag.class));
                if (tags != null && tags.size() > 0) {
                    list.add(tags.get(0));
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
        return list;
    }

    @Override
    public User findUserByActivityId(int activityId) {
        List<User> users = jdbcTemplate.query("SELECT `user`.`uuid`,`user`.`nickname`,`user`.`avatarUrl`,`user`.`gender`,`user`.`email` FROM `user`,`user_activity` " +
                "WHERE `user`.`uuid` = `user_activity`.`user_id` AND `user_activity`.`activity_id` = ? " +
                "AND `user_activity`.`isManage` = 1 ", new Object[]{activityId}, new BeanPropertyRowMapper(User.class));
        if (users != null &&users.size()>0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Room findRoomById(int roomId) {
        List<Room> rooms = jdbcTemplate.query("SELECT * FROM `room`" +
                "WHERE id=? ", new Object[]{roomId}, new BeanPropertyRowMapper(Room.class));
        if (rooms != null && rooms.size()>0) {
            return rooms.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void updateActivityAndRoom(Activity activity, int roomId) {
        jdbcTemplate.update("insert into room values(?, ?, ?, ?, null)",
                roomId,activity.getTitle(),"rtmp://118.25.55.210/live/"+roomId,"wss://ntdxyg.mynatapp.cc/webSocket/chat/"+roomId);
        jdbcTemplate.update("insert into `activity_room` values(?, ?)",
                activity.getId(),roomId);
        jdbcTemplate.update("update activity set status = 1 where id = ?",
                activity.getId());
    }

    @Override
    public List<Map> findAllActivityByWx() {
        List<Map> list = new LinkedList<>();
        List<Activity> activities = jdbcTemplate.query("select * from activity where ishot=0 ", new BeanPropertyRowMapper(Activity.class));
        if (activities != null && activities.size()>0) {
            for (Activity activity:activities){
                Map map = new HashMap();
                map.put("activity",activity);
                List<Tag> tagList = activityService.findTagByActivityId(activity.getId());
                map.put("tagList",tagList);
                User user = activityService.findUserByActivityId(activity.getId());
                map.put("user",user);
                list.add(map);
            }
        } else {
            return null;
        }
        return list;
    }

    @Override
    public Map findActivityDetailByWx(int id) {
        Map map = new HashMap();
        List<Activity> activities = jdbcTemplate.query("select * from activity where ishot=0 and id=?",new Object[]{id}, new BeanPropertyRowMapper(Activity.class));
        if (activities != null && activities.size()>0) {
            map.put("activity",activities.get(0));
            List<User_Activity> users = activityService.findAllUserByActivityId(activities.get(0).getId());
            map.put("users",users);
            List<Comment> comments= activityService.findAllCommentByActivityId(activities.get(0).getId());
            map.put("comments",comments);
        } else {
            return null;
        }
        return map;
    }

    @Override
    public List<User_Activity> findAllUserByActivityId(int id) {
        List<User_Activity> list = jdbcTemplate.query("SELECT user_id,isManage,isSign,isLove,isJoin FROM `user_activity` WHERE activity_id = ? ", new Object[]{id}, new BeanPropertyRowMapper(User_Activity.class));
        if (list != null && list.size()>0) {
            for (User_Activity user_activity : list){
                user_activity.setUser(userService.findUserById(user_activity.getUser_id()));
                user_activity.setActivity(activityService.findActivityById(id));
            }
            return list;
        } else {
            return null;
        }
    }

    @Override
    public List<Comment> findAllCommentByActivityId(int id) {
        List<Comment> list = jdbcTemplate.query("SELECT * FROM `comment` WHERE activity_id = ? ", new Object[]{id}, new BeanPropertyRowMapper(Comment.class));
        if (list != null && list.size()>0) {
            for (Comment comment : list){
                comment.setUser(activityService.findUserByActivityId(id));
                comment.setActivity(activityService.findActivityById(id));
            }
            return list;
        } else {
            return null;
        }
    }

    @Override
    public User_Activity findUserActivityByActivityId(int id, String uuid) {
        List<User_Activity> list = jdbcTemplate.query("SELECT * FROM `user_activity` WHERE activity_id = ? AND user_id = ? ", new Object[]{id,uuid}, new BeanPropertyRowMapper(User_Activity.class));
        if (list != null && list.size()>0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Map> findAllJoinActivityByWx(String uuid) {
        List<Map> list = new LinkedList<>();
        List<Activity> activities = jdbcTemplate.query("SELECT * FROM `user_activity`,`activity` WHERE " +
                        "`user_activity`.`activity_id`=`activity`.`id` AND user_id = ? AND `user_activity`.`isManage`=0",
                new Object[]{uuid},new BeanPropertyRowMapper(Activity.class));
        if (activities != null && activities.size()>0) {
            for (Activity activity:activities){
                Map map = new HashMap();
                map.put("activity",activity);
                List<Tag> tagList = activityService.findTagByActivityId(activity.getId());
                map.put("tagList",tagList);
                User user = activityService.findUserByActivityId(activity.getId());
                map.put("user",user);
                list.add(map);
            }
        } else {
            return null;
        }
        return list;
    }

    @Override
    public List<Map> findAllApplyActivityByWx(String uuid) {
        List<Map> list = new LinkedList<>();
        List<Activity> activities = jdbcTemplate.query("SELECT * FROM `user_activity`,`activity` WHERE " +
                "`user_activity`.`activity_id`=`activity`.`id` AND user_id = ? AND `user_activity`.`isManage`=1",
                new Object[]{uuid},new BeanPropertyRowMapper(Activity.class));
        if (activities != null && activities.size()>0) {
            for (Activity activity:activities){
                Map map = new HashMap();
                map.put("activity",activity);
                List<Tag> tagList = activityService.findTagByActivityId(activity.getId());
                map.put("tagList",tagList);
                User user = activityService.findUserByActivityId(activity.getId());
                map.put("user",user);
                list.add(map);
            }
        } else {
            return null;
        }
        return list;
    }

    @Override
    public List<Map> findAllLoveActivityByWx(String uuid) {
        List<Map> list = new LinkedList<>();
        List<Activity> activities = jdbcTemplate.query("SELECT * FROM `user_activity`,`activity` WHERE " +
                        "`user_activity`.`activity_id`=`activity`.`id` AND user_id = ? AND `user_activity`.`isLove`=1",
                new Object[]{uuid},new BeanPropertyRowMapper(Activity.class));
        if (activities != null && activities.size()>0) {
            for (Activity activity:activities){
                Map map = new HashMap();
                map.put("activity",activity);
                List<Tag> tagList = activityService.findTagByActivityId(activity.getId());
                map.put("tagList",tagList);
                User user = activityService.findUserByActivityId(activity.getId());
                map.put("user",user);
                list.add(map);
            }
        } else {
            return null;
        }
        return list;
    }

    @Override
    public int updateHotActivity(Activity activity, String tag) {
        jdbcTemplate.update("DELETE FROM `activity_tag` WHERE `activity_tag`.`activity_id` = ?", 1);
        String[] tag_id = tag.split(",");
        for (String id : tag_id){
            jdbcTemplate.update("INSERT INTO `activity_tag` VALUES (? , ?);", 1,Integer.parseInt(id));
        }
        return jdbcTemplate.update("UPDATE `activity` SET title = ?,content = ?,img = ?,people = ?,`local` = ?,`begin` = ?,`end` = ? WHERE ishot = 1;",
                activity.getTitle(),activity.getContent(),activity.getImg(),activity.getPeople(),activity.getLocal(),activity.getBegin(),activity.getEnd());
    }

    @Override
    public int setIsLove(String uuid, int id) {
        int i = jdbcTemplate.queryForObject("select count(*) from `user_activity` where user_id = ? and activity_id = ?;",
                new Object[]{uuid, id},Integer.class);
        if (i == 0){
            jdbcTemplate.update("insert into `user_activity` values(?, ?, 0, 0, 1, 0)",uuid, id);
        }else{
            jdbcTemplate.update("UPDATE `user_activity` SET isLove = 1 WHERE user_id = ? and activity_id = ?;",
                    uuid,id);
        }
        return 1;
    }

    @Override
    public int cancelIsLove(String uuid, int id) {
        return jdbcTemplate.update("UPDATE `user_activity` SET isLove = 0 WHERE user_id = ? and activity_id = ?;",
                uuid,id);
    }

    @Override
    public List<Activity> findAllActivity(int page, int limit) {
        List<Activity> activities = jdbcTemplate.query("select * from activity where ishot=0 limit ?,? ", new Object[]{(page - 1) * limit, limit}, new BeanPropertyRowMapper(Activity.class));
        if (activities != null && activities.size()>0) {
            return activities;
        } else {
            return null;
        }
    }

    @Override
    public List<Activity> getActivityList() {
        List<Activity> activities = jdbcTemplate.query("select * from activity where ishot=0", new BeanPropertyRowMapper(Activity.class));
        if (activities != null && activities.size()>0) {
            return activities;
        } else {
            return null;
        }
    }

    @Override
    public int getCommentCount() {
        int count = jdbcTemplate.queryForObject("select count(*) from comment where isimg = 0", Integer.class);
        return count;
    }

    @Override
    public List<Comment> findAllComment(int page, int limit) {
        List<Comment> comments = jdbcTemplate.query("SELECT * FROM `comment` WHERE `isimg`=0 limit ?,?",
                new Object[]{(page - 1) * limit, limit}, new BeanPropertyRowMapper(Comment.class));
        if (comments != null && comments.size()>0) {
            for (Comment comment : comments) {
                List<User> users = jdbcTemplate.query("select * from `user` where uuid" +
                        " = ?", new Object[]{comment.getUser_id()}, new BeanPropertyRowMapper(User.class));
                if (users != null && users.size() > 0) {
                    comment.setUser(users.get(0));
                }
                List<Activity> activities = jdbcTemplate.query("select * from `activity` where id" +
                        " = ?", new Object[]{comment.getActivity_id()}, new BeanPropertyRowMapper(Activity.class));
                if (activities != null && activities.size() > 0) {
                    comment.setActivity(activities.get(0));
                }
            }
            return comments;
        } else {
            return null;
        }
    }

    @Override
    public int getCommentImgCount() {
        int count = jdbcTemplate.queryForObject("select count(*) from comment where isimg = 1", Integer.class);
        return count;
    }

    @Override
    public List<Comment> findAllCommentImg(int page, int limit) {
        List<Comment> comments = jdbcTemplate.query("SELECT * FROM `comment` WHERE `isimg`=1 limit ?,?",
                new Object[]{(page - 1) * limit, limit}, new BeanPropertyRowMapper(Comment.class));
        if (comments != null && comments.size()>0) {
            for (Comment comment : comments) {
                List<User> users = jdbcTemplate.query("select * from `user` where uuid" +
                        " = ?", new Object[]{comment.getUser_id()}, new BeanPropertyRowMapper(User.class));
                if (users != null && users.size() > 0) {
                    comment.setUser(users.get(0));
                }
                List<Activity> activities = jdbcTemplate.query("select * from `activity` where id" +
                        " = ?", new Object[]{comment.getActivity_id()}, new BeanPropertyRowMapper(Activity.class));
                if (activities != null && activities.size() > 0) {
                    comment.setActivity(activities.get(0));
                }
            }
            return comments;
        } else {
            return null;
        }
    }

    @Override
    public List findTags() {
        List<HashMap> tagList = new LinkedList<>();
        List<Tag> list = jdbcTemplate.query("SELECT * FROM tag WHERE `name`<>'直播会议' && `name`<>'视频会议'", new BeanPropertyRowMapper(Tag.class));
        if (list != null && list.size() > 0) {
            for (Tag tag : list) {
                HashMap map = new HashMap();
                map.put("value", tag.getId());
                map.put("name", tag.getName());
                map.put("color", tag.getColor());
                map.put("checked", false);
                tagList.add(map);
            }
        } else {
            return null;
        }
        return tagList;
    }

    @Override
    public int addActivityAndTagAndUser(Activity activity, String tags, String uuid) {
        int i = jdbcTemplate.update("insert into activity values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                activity.getTitle(), activity.getContent(), activity.getImg(), activity.getPeople(),
                activity.getLocal(), activity.getOrganization(), activity.getBegin(), activity.getEnd(),
                activity.getCategory(), activity.getIshot(), activity.getStatus());
        int activityId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        if (i == 1){
            jdbcTemplate.update("insert into activity_tag values(?, ?)",activityId,activity.getCategory()+1);
            String[] tagId = tags.split(",");
            for (String id:tagId ){
                jdbcTemplate.update("insert into activity_tag values(?, ?)",activityId,Integer.parseInt(id));
            }
            jdbcTemplate.update("insert into user_activity values (?, ?, ?, ?, ?, ?)",uuid,activityId,1,1,0,1);
        }
        return 1;
    }

    @Override
    public int findPeopleByActivityId(int id) {
        int count = jdbcTemplate.queryForObject("select count(*) from `user_activity` where activity_id = ?",new Object[]{id}, Integer.class);
        return count;
    }

    @Override
    public void joinActivity(String uuid, int id) {
        int i = jdbcTemplate.queryForObject("select count(*) from `user_activity` where user_id = ? and activity_id = ?;",
                new Object[]{uuid, id},Integer.class);
        if (i==0){
            jdbcTemplate.update("insert into `user_activity` values(?, ?, 0, 0, 0, 1)",uuid, id);
        }else {
            jdbcTemplate.update("UPDATE `user_activity` SET isJoin = 1 WHERE user_id = ? and activity_id = ?;",uuid, id);
        }
    }

    @Override
    public Activity findActivityByRoomId(int roomid) {
        List<Activity> activities = jdbcTemplate.query("SELECT * FROM `activity_room`,`activity` WHERE `activity`.`id` = `activity_room`.`activity_id` AND `activity_room`.`room_id` = ?",
                new Object[]{roomid},new BeanPropertyRowMapper(Activity.class));
        if (activities != null && activities.size()>0) {
            return activities.get(0);
        } else {
            return null;
        }
    }

}
