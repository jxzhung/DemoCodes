package com.jzhung.test.http.jsoup;

import com.jzhung.test.util.LogUitl;
import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

/**
 * HTTP工具类
 */
public class HttpUtil {
    /**
     * GET请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        String result = Request.Get(url).execute().returnContent().asString();
        LogUitl.debug("GET:" + url + " 结果:" + result);
        return result;
    }

    public static String post(String url, HttpEntity entity) throws IOException {
        String result = Request.Post(url)
                .connectTimeout(20000)
                .socketTimeout(20000)
                .body(entity)
                .execute().returnContent().asString();
        LogUitl.debug("POST:" + url + " 结果:" + result);
        return result;
    }
}
