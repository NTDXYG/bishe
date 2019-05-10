package com.yg.bishe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.jarod.qqwry.IPZone;
import com.github.jarod.qqwry.QQWry;
import com.yg.bishe.bean.Log;
import com.yg.bishe.util.DateUtil;
import io.swagger.models.auth.In;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.Stack;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BisheApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BisheApplicationTests {

}

