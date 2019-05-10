package com.yg.bishe.service;

import com.yg.bishe.bean.Room;

import java.util.List;

public interface RoomService {
    int getCount();

    List<Room> findAllRooms(int page, int limit);

    Room findRoomById(String id);

    void deleteRoomById(int parseInt);

    Room findRoomByActivityId(String activity_id);
}
