package com.jzhung.test.ols;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 测试消息服务器
 */
public class MsgServerTest {

    @Test
    private static void testMsgServer() {
        String studentId = "1111501019";
        int count = 10;
        long duration = 400;
        MsgServerTest.sendMsg(studentId, count, duration);
        System.out.println("========================  执行完毕  ========================");
    }


    public static void sendMsg(String studentId, int times, long duration) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Random random = new Random();
        for (int i = 0; i < times; i++) {
            String url = "http://192.168.1.9:9090/plugins/pushnotification/pushtoperson?info=消息" + i + "的消息&subtitle=" + format.format(new Date()) + "标题" + i + "&account=" + studentId + "&uri=appstart:com.tt.reshub";
            try {
                doGet(url);
                System.out.println("总消息" + times + " 发送消息" + i + " url：" + url);
                //Thread.sleep(random.nextInt(15000));
                Thread.sleep(duration);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Get Request
     *
     * @return
     * @throws Exception
     */
    public static String doGet(String url) throws Exception {
        URL localURL = new URL(url);
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
        }

        try {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return resultBuffer.toString();
    }
}
