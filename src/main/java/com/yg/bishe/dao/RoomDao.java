package com.yg.bishe.dao;

import com.yg.bishe.bean.Room;

import java.util.List;

public interface RoomDao {
    int getCount();

    List<Room> findAllRooms(int page, int limit);

    Room findRoomById(int parseInt);

    void deleteRoomById(int id);

    Room findRoomByActivityId(int id);
}
