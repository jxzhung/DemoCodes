package com.jzhung.test.lambda;

import org.junit.Test;
import java.io.File;

/**
 * Created by Jzhung on 2016/9/2.
 */
public class LambdaTest {

    @Test
    public void startOneThread() throws InterruptedException {
        Thread thread = new Thread(()-> {
            System.out.println("nice1");
            System.out.println("nice2");
            System.out.println("nice3");
        });
        thread.start();
        thread.join();
        System.out.println("complete!");
    }

    @Test
    public void listFiles() throws InterruptedException {
        String file = "D:\\";
        File dir = new File(file);

        File[] files = dir.listFiles((File f)->{
            if(f.isFile()){
                return true;
            }
            return false;
        });

        for (File f:files){
            System.out.println(f.getAbsolutePath());
        }

    }
}
