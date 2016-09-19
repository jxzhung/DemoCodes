package com.jzhung.test.util;

import java.util.Random;

/**
 * 代理浏览器
 */
public class UserAgent {
    static String [] agents = {
        "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0",//Firefox
        "Mozilla/5.0 (Windows NT 5.2) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30", //chrome
        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET4.0E; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET4.0C)",
        "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN) AppleWebKit/533.21.1 (KHTML, like Gecko) Version/5.0.5 Safari/533.21.1"//Safari
    };

   public static String getUserAgent(){
       return agents[0];
   }

    public static String getRandomUserAgent(){
        return agents[new Random().nextInt(agents.length)];
    }

    public static String getChromeAgent(){
        return agents[1];
    }

    public static String getFirefoxAgent(){
        return agents[0];
    }

    public static String getIE8Agent(){
        return agents[2];
    }

    public static String getSafariAgent(){
        return agents[2];
    }
}
