package com.yg.bishe.util;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @Title:GetMessageCode
 * @Description:发送验证码
 * @author:yg
 */
public class SendMessageUtil {
    private static final String QUERY_PATH="https://api.miaodiyun.com/20150822/industrySMS/sendSMS";
    private static final String SEND_PATH="https://api.miaodiyun.com/20150822/affMarkSMS/sendSMS";
    private static final String ACCOUNT_SID="f746884c830f464188fdc0d61ab71237";
    private static final String AUTH_TOKEN="42b4bd3d69254cdf965b7032ee25a800";

    public static String getCode(String phone){
        String rod=smsCode();
        String timestamp=getTimestamp();
        String sig=getMD5(ACCOUNT_SID,AUTH_TOKEN,timestamp);
        String tamp="【云智教育】您的验证码为"+rod+"，请于"+3+"分钟内正确输入，如非本人操作，请忽略此短信。";
        OutputStreamWriter out=null;
        BufferedReader br=null;
        StringBuilder result=new StringBuilder();
        try {
            URL url=new URL(QUERY_PATH);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);//设置是否允许数据写入
            connection.setDoOutput(true);//设置是否允许参数数据输出
            connection.setConnectTimeout(5000);//设置链接响应时间
            connection.setReadTimeout(10000);//设置参数读取时间
            connection.setRequestProperty("Content-type","application/x-www-form-urlencoded");
            out=new OutputStreamWriter(connection.getOutputStream(),"UTF-8");
            String args=getQueryArgs(ACCOUNT_SID, tamp, phone, timestamp, sig, "JSON");
            out.write(args);
            out.flush();
            br=new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            String temp="";
            while((temp=br.readLine())!=null){
                result.append(temp);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject json=JSONObject.fromObject(result.toString());
        String respCode=json.getString("respCode");
        String defaultRespCode="00000";
        if(defaultRespCode.equals(respCode)){
            return rod;
        }else{
            return defaultRespCode;
        }
    }
    public static String getQueryArgs(String accountSid,String smsContent,String to,String timestamp,String sig,String respDataType){
        return "accountSid="+accountSid+"&smsContent="+smsContent+"&to="+to+"&timestamp="+timestamp+"&sig="+sig+"&respDataType="+respDataType;
    }
    public static String getTimestamp(){
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
    public static String getMD5(String sid,String token,String timestamp){
        StringBuilder result=new StringBuilder();
        String source=sid+token+timestamp;
        try {
            MessageDigest digest=MessageDigest.getInstance("MD5");
            byte[] bytes=digest.digest(source.getBytes());
            for(byte b:bytes){
                String hex=Integer.toHexString(b&0xff);
                if(hex.length()==1){
                    result.append("0"+hex);
                }else{
                    result.append(hex);
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    public static String smsCode(){
        String random=(int)((Math.random()*9+1)*100000)+"";
        return random;
    }
}
