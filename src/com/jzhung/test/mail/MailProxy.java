package com.jzhung.test.mail;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

/**
 * 邮件发送代理
 * @author jzhung.com
 */
public class MailProxy {
    @Test
    public void sendContent() {
        send("测试内容1");
        send("测试标题2", "测试内容2");
        send("测试标题3", "测试内容3", new File("d:\\log.txt"));
    }


    public static void send(String content) {
        buildSender()
                .withContent(content)
                .send();
    }

    public static void send(String title, String content) {
        buildSender()
                .subject(title)
                .withContent(content)
                .send();
    }

    public static void send(String content, File... files) {
        try {
            MailSender sender = buildSender().withContent(content);
            for (File file : files) {
                sender.addAttachment(file.getName(), new FileInputStream(file));
            }
            sender.send();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void send(String title, String content, File... files) {
        try {
            MailSender sender = buildSender().subject(title).withContent(content);
            for (File file : files) {
                sender.addAttachment("ServerTest.java", new FileInputStream(file));
            }
            sender.send();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static MailSender buildSender() {
        MailSender mail = new MailSender();
        mail.smtpServer("smtp.163.com")
                .smtpPort(25)
                .fromMail("tianqijianlog@163.com")
                .toMail("tianqijianlog@163.com")
                .userName("tianqijianlog@163.com")
                .password("bvlgltcbcmcnnefy")
                .subject("无标题邮件" + UUID.randomUUID())
                .setHtml(true)
                .showLog(true);
        return mail;
    }

}
