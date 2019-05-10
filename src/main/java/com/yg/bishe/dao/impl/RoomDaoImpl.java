package com.yg.bishe.dao.impl;

import com.yg.bishe.bean.Banner;
import com.yg.bishe.bean.Room;
import com.yg.bishe.dao.RoomDao;
import com.yg.bishe.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDaoImpl implements RoomDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    RoomService roomService;

    @Override
    public int getCount() {
        int count = jdbcTemplate.queryForObject("select count(*) from room", Integer.class);
        return count;
    }

    @Override
    public List<Room> findAllRooms(int page, int limit) {
        List<Room> list = jdbcTemplate.query("select * from room limit ?,?" ,new Object[]{(page-1)*limit,limit}, new BeanPropertyRowMapper(Room.class));
        if (list!=null && list.size()>0){
            return list;
        }else{
            return null;
        }
    }

    @Override
    public Room findRoomById(int id) {
        List<Room> list = jdbcTemplate.query("select * from room where id = ?" ,new Object[]{id}, new BeanPropertyRowMapper(Room.class));
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public void deleteRoomById(int id) {
        jdbcTemplate.update("DELETE from room where id=?",id);
    }

    @Override
    public Room findRoomByActivityId(int id) {
        int roomid = jdbcTemplate.queryForObject("SELECT room_id FROM `activity_room` WHERE activity_id = ?",new Object[]{id}, Integer.class);
        Room room = roomService.findRoomById(String.valueOf(roomid));
        return room;
    }
}
