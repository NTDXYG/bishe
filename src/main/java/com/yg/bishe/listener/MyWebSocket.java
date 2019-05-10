package com.yg.bishe.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hengyi.dzfilter.utils.TextUtils;
import com.yg.bishe.bean.User;
import com.yg.bishe.controller.RoomController;
import com.yg.bishe.service.UserService;
import com.yg.bishe.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多房间实现
 */
@ServerEndpoint("/webSocket/chat/{roomid}")
@Component
public class MyWebSocket {


    // 使用map来收集session，key为roomName，value为同一个房间的用户集合
    // concurrentMap的key不存在时报错，不是返回null
    private static final Map<String, Set<Session>> rooms = new ConcurrentHashMap();
    private Map<String,Integer> people = new HashMap<>();
    private final static Logger logger = LoggerFactory.getLogger(MyWebSocket.class);


    @OnOpen
    public void connect(@PathParam("roomid") String roomid, Session session) throws Exception {
        // 将session按照房间名来存储，将各个房间的用户隔离
        if (!rooms.containsKey(roomid)) {
            Set<Session> room = new HashSet<>();
            room.add(session);
            rooms.put(roomid, room);
        } else {
            // 房间已存在，直接添加用户到相应的房间
            rooms.get(roomid).add(session);
            people.put("people",rooms.get(roomid).size());
            broadcast(roomid,JSON.toJSONString(people));
        }
        logger.info("a client has connected!"+roomid+":"+rooms.get(roomid).size()+"人");
    }

    @OnClose
    public void disConnect(@PathParam("roomid") String roomid, Session session) throws Exception {
        rooms.get(roomid).remove(session);
        people.put("people",rooms.get(roomid).size());
        broadcast(roomid,JSON.toJSONString(people));
        logger.info("a client has disconnected!"+roomid+":"+rooms.get(roomid).size()+"人");
    }

    @OnMessage
    public void receiveMsg(@PathParam("roomid") String roomid,
                           String msg, Session session) throws Exception {
        // 此处应该有html过滤
        //System.out.println(msg);
        broadcast(roomid, msg);
    }

    // 按照房间名进行广播
    public synchronized static void broadcast(String roomid, String msg) throws Exception {
        //System.out.println("after"+msg);
        for (Session session : rooms.get(roomid)) {
            session.getAsyncRemote().sendText(TextUtils.filter(msg));
        }
    }

}