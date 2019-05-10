package com.yg.bishe.service.impl;

import com.yg.bishe.bean.Room;
import com.yg.bishe.dao.RoomDao;
import com.yg.bishe.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomDao dao;

    @Override
    public int getCount() {
        return dao.getCount();
    }

    @Override
    public List<Room> findAllRooms(int page, int limit) {
        return dao.findAllRooms(page, limit);
    }

    @Override
    public Room findRoomById(String id) {
        return dao.findRoomById(Integer.parseInt(id));
    }

    @Override
    public void deleteRoomById(int id) {
        dao.deleteRoomById(id);
    }

    @Override
    public Room findRoomByActivityId(String activity_id) {
        return dao.findRoomByActivityId(Integer.parseInt(activity_id));
    }
}
