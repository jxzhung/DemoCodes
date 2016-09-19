package com.jzhung.test.http;

import org.apache.http.client.fluent.Request;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Apache httpclient flent api test
 */
public class HttpClientFluentTests {

    @Test
    public void fetchContent() throws IOException {
        //获取网页内容
        String url = "https://zhuanlan.zhihu.com/p/21410338";
        String content = Request.Get(url).execute().returnContent().asString(Charset.forName("utf-8"));
        System.out.println(content.length());
    }

    @Test
    public void DownloadFile() throws IOException {
        //下载图片
        String img = "https://pic2.zhimg.com/7f0628c1675fa3be26e59ac29b1f9b1d_b.png";
        Request.Get(img).execute().saveContent(new File("d:\\img.jpg"));
    }
}
