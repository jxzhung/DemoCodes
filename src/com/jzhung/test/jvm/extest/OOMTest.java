package com.jzhung.test.jvm.extest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzhung on 2016/9/12.
 */
public class OOMTest {
    static class OOMObject{

    }
    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();

        int i = 0;
        try{
            while (true){
                i++;
                list.add(new OOMObject());
                System.out.println(i);
            }
        }catch (Exception e){

        }

        System.out.println("111111111");

    }
}
