package com.jzhung.test.mail;

import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.Socket;

public class SMTPMain {
    public static void main(String[] args) {
        String sender = "tianqijianlog@163.com";
        String receiver = "937149933@qq.com";
        String password = "bvlgltcbcmcnnefy";
        String user = new BASE64Encoder().encode(sender.substring(0, sender.indexOf("@")).getBytes());
        String pass = new BASE64Encoder().encode(password.getBytes());
        try {
            Socket socket = new Socket("smtp.163.com", 25);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter writter = new PrintWriter(outputStream, true);  //我TM去 这个true太关键了!
            System.out.println(reader.readLine());
            //HELO
            writter.println("HELO huan");
            System.out.println(reader.readLine());
            //AUTH LOGIN
            writter.println("auth login");
            System.out.println(reader.readLine());
            writter.println(user);
            System.out.println(reader.readLine());
            writter.println(pass);
            System.out.println(reader.readLine());
            //Above   Authentication successful <pre name="code" class="java">
            //Set mail from   and   rcpt to
            writter.println("mail from:<" + sender +">");
            System.out.println(reader.readLine());
            writter.println("rcpt to:<" + receiver +">");
            System.out.println(reader.readLine());

            //Set data
            writter.println("data");
            System.out.println(reader.readLine());
            writter.println("subject:干嘛来！");
            writter.println("from:j@qq.com");
            writter.println("to:" + receiver);
            writter.println("Content-Type:text/html;charset=UTF-8");
            writter.println();
            writter.println("我是你哥 ");
            writter.println(".");
            writter.println("");
            System.out.println(reader.readLine());

            //Say GoodBye
            writter.println("rset");
            System.out.println(reader.readLine());
            writter.println("quit");
            System.out.println(reader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void send(){
        Socket socket = null;
        try {
            socket = new Socket("smtp.163.com", 25);

          /*  InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter writter = new PrintWriter(outputStream, true);  //我TM去 这个true太关键了!
            //HELO
            writter.println("HELO server");
            //AUTH LOGIN
            writter.println("auth login");
            writter.println(user);
            writter.println(pass);
            //Above   Authentication successful <pre name="code" class="java">
            //Set mail from   and   rcpt to
            writter.println("mail from:<" + sender +">");
            writter.println("rcpt to:<" + receiver +">");

            //Set data
            writter.println("data");
            writter.println("subject:测试是我");
            writter.println("from:百度admin@baidu.com");
            writter.println("to:" + receiver);
            writter.println("Content-Type:text/html;charset=UTF-8");
            writter.println();
            writter.println("晚上可以共进晚餐吗？");
            writter.println(".");
            writter.println("");

            //Say GoodBye
            writter.println("rset");
            writter.println("quit");*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}