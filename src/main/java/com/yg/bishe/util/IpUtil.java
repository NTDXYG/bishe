package com.yg.bishe.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.jarod.qqwry.IPZone;
import com.github.jarod.qqwry.QQWry;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.http.HttpServletRequest;

public class IpUtil {
    public static String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getAddress(String ip) throws Exception{
        if (ip.contains("192.168.")||ip.contains("127.0.0.1")){
            return "本地访问";
        }else {
            Connection con = Jsoup.connect("https://api.webse.cn/getip?ip=112.21.108.182");
            //请求头设置，特别是cookie设置
            con.header("Access-Control-Allow-Origin", "*");
            con.header("Content-Type", "application/json; charset=utf-8");
            con.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            con.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
            con.ignoreContentType(true);
            Document doc = con.get();
            JSONObject json = JSON.parseObject(doc.body().text());
            String province = (String) json.get("province");
            String city = (String) json.get("city");
            return province+city;
        }
    }

    public static String getOperator(String ip) throws Exception{
        if (ip.contains("192.168.")||ip.contains("127.0.0.1")){
            return "局域网";
        }else{
            QQWry qqwry = new QQWry();
            IPZone zone = qqwry.findIP(ip);
            return zone.getSubInfo().substring(0, 2);
        }
    }
}