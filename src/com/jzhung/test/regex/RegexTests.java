package com.jzhung.test.regex;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式测试
 */
public class RegexTests {

    @Test
    public void matchStr(){
        String str = "This order was placed for QT3000! OK?";
        String regex = "(.*)(\\d+)(.*)";
        Pattern pattern = Pattern.compile(regex);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            Matcher matcher = pattern.matcher(str);
            //是否匹配
            System.out.println(matcher.matches());
        }

        long time = System.currentTimeMillis() -start;
        System.out.println("耗时:" + time);
        //while (matcher.matches())
    }

    @Test
    public void matchSites(){
        String url = "http://www.demo.com/path?query=s";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            //是否匹配
            System.out.println(SiteMatcher.match(url));
        }

        long time = System.currentTimeMillis() -start;
        System.out.println("耗时:" + time);
    }
}
