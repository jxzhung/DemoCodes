package com.jzhung.test.com.notebook;

import com.sun.istack.internal.NotNull;
import org.junit.Test;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Jzhung on 2016/9/23.
 */
public class VersionStringTest {

    @Test
    public void test () {
        System.out.println(UUID.randomUUID());
        System.out.println(UUID.randomUUID().toString().length());
        String version = "";
        for (int i = 0; i < 128; i++) {
            version = genNewVersionString(version);
            System.out.println("v" + (i+1) + ": " + version);
        }
    }


    /**
     * 获取新版本号
     * @param oriVersion
     * @return
     */
    public static String genNewVersionString(@NotNull String oriVersion){
        StringBuffer buffer = new StringBuffer();
        if (oriVersion.length() == 0){
            buffer.append("0.");
        }
        buffer.append(oriVersion);
        buffer.append(getRandomString(2));

        String afterDotStr = buffer.substring(buffer.indexOf(".") + 1, buffer.length());
        if(afterDotStr.length() >= 128){
            int w = Integer.parseInt(buffer.substring(0, buffer.indexOf("."))) + 64;
            buffer = new StringBuffer();
            buffer.append(w);
            buffer.append(".");
            buffer.append(afterDotStr.substring(64, afterDotStr.length()));
            //System.out.println(newS.substring(64, newS.length()).length());
        }
        return buffer.toString();
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
