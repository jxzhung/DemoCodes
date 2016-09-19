package com.jzhung.test.mail;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * jzhung
 * 发送错误log到邮箱
 */
public class MailSender {
    public static final String useraccount = "tianqijianlog@163.com";
    private static final String password = "bvlgltcbcmcnnefy";
    private String exContent; //异常文件内容
    private String getExContentFirstLine = null;//异常首行内容
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-ms");

    @Test
    public void test(){
        new MailSender().sendEmail();
    }


    public void sendPlainTextEmail(String toEmail, String form, String to, String subject, String content) {
        PassAuthenticator pass = new PassAuthenticator();   //获取帐号密码
        Session session = Session.getInstance(getDefalutSenderProp(), pass); //获取验证会话

        InternetAddress fromAddress, toAddress;
        try {
            fromAddress = new InternetAddress(useraccount, form);
            toAddress = new InternetAddress(toEmail, to);

            //邮件发送信息
            MimeMessage message = new MimeMessage(session);
            message.setSubject(subject);
            message.setFrom(fromAddress);

            //邮件正文
            MimeBodyPart text = new MimeBodyPart();
            text.setContent(content, "text/html;charset=UTF-8");//防止中文乱码

            //创建容器描述数据关系
            MimeMultipart mp = new MimeMultipart();
            mp.addBodyPart(text);
            mp.setSubType("mixed");


            //设置数据到信息中
            message.setContent(mp);
            message.addRecipient(javax.mail.Message.RecipientType.TO, toAddress);
            message.saveChanges();

            //连接邮箱并发送
            Transport transport = session.getTransport("smtp");
            /**
             * 这个地方需要改称自己的账号和密码
             */
            transport.connect("smtp.163.com", useraccount, password);
            transport.send(message);
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Properties getDefalutSenderProp() {
        Properties props = new Properties();
        props.put("mail.smtp.protocol", "smtp");
        props.put("mail.smtp.auth", "true");//设置要验证
        props.put("mail.smtp.host", "smtp.163.com");//设置host
        props.put("mail.smtp.port", "25");  //设置端口
        return props;
    }

    public void sendEmail() {
        PassAuthenticator pass = new PassAuthenticator();   //获取帐号密码
        Session session = Session.getInstance(getDefalutSenderProp(), pass); //获取验证会话
        try {
            //配置发送及接收邮箱
            InternetAddress fromAddress, toAddress;
            /**
             * 这个地方需要改成自己的邮箱
             */
            fromAddress = new InternetAddress(useraccount, "客户端"); //发件人邮箱和姓名
            toAddress = new InternetAddress(useraccount, "日志收件箱"); //收件人邮箱和姓名


            String file = getLastExceptionFile();
            if (file == null) {
                System.out.println("最后的日志文件为NULL无法发送邮件");
                return;
            }

            exContent = getExceptionContent(file);

            //创建邮件发送信息
            MimeMessage message = new MimeMessage(session);
            message.setSubject(getSubject());
            message.setFrom(fromAddress);
            //message.setContent("test", "text/plain");

            //创建邮件正文，为了避免邮件正文中文乱码问题，需要使用charset=UTF-8指明字符编码
            MimeBodyPart text = new MimeBodyPart();
            text.setContent(getMailContent(), "text/html;charset=UTF-8");

            //创建邮件附件
            MimeBodyPart attachPart = new MimeBodyPart();
            DataHandler dh = new DataHandler(new FileDataSource(file));
            attachPart.setDataHandler(dh);
            attachPart.setFileName(dh.getName());

            MimeMultipart allMultipart = new MimeMultipart("mixed"); //附件
            allMultipart.addBodyPart(attachPart);//添加

            //创建容器描述数据关系
            MimeMultipart mp = new MimeMultipart();
            mp.addBodyPart(text);
            mp.addBodyPart(attachPart);
            mp.setSubType("mixed");


            //设置数据到信息中
            message.setContent(mp);
            message.addRecipient(javax.mail.Message.RecipientType.TO, toAddress);
            message.saveChanges();

            //连接邮箱并发送
            Transport transport = session.getTransport("smtp");
            /**
             * 这个地方需要改称自己的账号和密码
             */
            transport.connect("smtp.163.com", useraccount, password);
            transport.send(message);
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();

            //throw new RuntimeException();//将此异常向上抛出，此时CrashHandler就能够接收这里抛出的异常并最终将其存放到txt文件中
            //Log.e("sendmail", e.getMessage());
        }
    }

    public String getMailContent() {
        StringBuilder sb = new StringBuilder();
        /** 软件信息 **/
        sb.append("hello<br/>");
        sb.append("this is a test email");
        return sb.toString();
    }


    /**
     * 获取标题
     *
     * @return
     */
    public String getSubject() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ 10086 ]");
        sb.append("的设备异常[ ");
        sb.append(getExContentFirstLine);
        sb.append(" ]");
        return sb.toString();
    }


    /**
     * 获取文件内容
     *
     * @param f
     * @return
     */
    public String getExceptionContent(String f) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                if (getExContentFirstLine == null) {
                    int last = 130;
                    if (line.length() < last) {
                        last = line.length();
                    }
                    getExContentFirstLine = line.substring(0, last);
                }
                content.append(line + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    /**
     * 获取最新的日志文件
     *
     * @return 返回日志文件路径或null
     */
    public String getLastExceptionFile() {
        File lastLogFile = null;

        FileHolder lastFileHolder = null;

        File logFolder = new File("d:\\");
        File[] files = logFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".log"))
                    return true;
                return false;
            }
        });
        if (files != null) {
            for (File file : files) {
                long fileCreateTime = file.lastModified();
                if (lastFileHolder == null) {
                    lastFileHolder = new FileHolder();
                    lastFileHolder.file = file;
                    lastFileHolder.time = fileCreateTime;
                } else {
                    if (lastFileHolder.time < fileCreateTime) {
                        lastFileHolder.file = file;
                        lastFileHolder.time = fileCreateTime;
                    }
                }

            }

            return lastFileHolder.file.getAbsolutePath();
        }
        return null;
    }

    class FileHolder {
        File file;
        long time;
    }


    class PassAuthenticator extends Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            /**
             * 这个地方需要添加上自己的邮箱的账号和密码
             */
            String username = useraccount;
            String pwd = password;
            return new PasswordAuthentication(useraccount, password);
        }
    }
}