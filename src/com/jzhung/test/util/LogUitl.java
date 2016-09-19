package com.jzhung.test.util;

/**
 * 日志工具类
 */
public class LogUitl {
    public static void info(String msg){
        //System.out.println(Thread.currentThread().getName() + " - " + msg);
        System.out.println(msg);
    }

    public static void debug(String msg){
        //System.out.println(Thread.currentThread().getName() + " - " + msg);
        System.out.println(msg);
    }

    public static void error(String msg){
        System.err.println(Thread.currentThread().getName() + " - " + msg);
    }
}
