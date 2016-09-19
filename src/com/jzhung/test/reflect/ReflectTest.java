package com.jzhung.test.reflect;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jzhung on 2016/9/6.
 */
public class ReflectTest {
    @Test
    public void getMethods() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class clz = Class.forName("java.util.ArrayList");
        Method[] methods = clz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            //System.out.print(methods[i].getName());
            System.out.println(methods[i].toString());
        }
        ArrayList arrayList = (ArrayList) clz.newInstance();
        arrayList.add("1");
        System.out.println(arrayList.size());

        AtomicBoolean isOk = new AtomicBoolean();
        isOk.set(true);
        System.out.println(isOk);
    }
}
