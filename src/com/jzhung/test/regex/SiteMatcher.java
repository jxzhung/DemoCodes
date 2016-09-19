package com.jzhung.test.regex;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网址匹配器
 */
public class SiteMatcher {
    private static Set<String> sites;
    private static String regex;
    private static Pattern pattern;

    public static boolean match(String url) {
        if (sites == null) {
            init();
        }
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    private static void init() {
        sites = new HashSet<>();
        sites.add("www.demo.com");
        sites.add("www.baidu.com");
        sites.add("qq.com");
        sites.add("www.runoob.com");
        sites.add("blog.csdn.net");

        StringBuffer buffer = new StringBuffer();
        for (String site : sites) {
            buffer.append(site);
            buffer.append("|");
        }
        String siteStr = buffer.toString();
        siteStr = buffer.substring(0, siteStr.length() - 1);
        regex = ".*(" + siteStr + ").*";
        pattern = Pattern.compile(regex);

    }
}
