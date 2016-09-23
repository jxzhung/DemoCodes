package com.jzhung.test.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Jzhung on 2016/8/31.
 */
public class TestMain {
    @Test
    public void getGetWeather(){
        String url = "http://www.kuaishou.com/hot";
//        String url = "http://aliyun.t600.com.cn:8890/";
        System.out.println(getContent(url));
    }

    public static String getContent(String url){
        try {
            return Request.Get(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0) Gecko/20100101 Firefox/6.0").execute().returnContent().asString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
