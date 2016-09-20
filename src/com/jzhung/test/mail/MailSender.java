package com.jzhung.test.mail;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;

public class MailSender {
    /**
     * 邮件主题
     **/
    private String subject;
    /**
     * 从此地址发出
     **/
    private String fromMail;
    /**
     * 用户名
     **/
    private String userName;
    /**
     * 登录密码
     **/
    private String password;
    /**
     * SMTP 服务器地址
     **/
    private String smtpServer;
    /**
     * SMTP 服务器端口（默认：25）
     **/
    private int smtpPort = 25;
    /**
     * 发送到 toMail 中的所有地址
     **/
    private List<String> toMail;
    /**
     * 邮件内容
     **/
    private String content;
    /**
     * 附件 Map&lt;附件名称,输入流&gt;
     **/
    private Map<String, InputStream> attachments;
    /**
     * 是否显示日志
     **/
    private boolean showLog;
    /**
     * 邮件内容是否为HTML格式（默认：false）
     **/
    private boolean isHtml;

    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private BufferedReaderProxy reader;
    private PrintWriterProxy writer;
    private String contentSeparate;

    public MailSender toMail(String mail) {
        if (toMail == null)
            toMail = new ArrayList<>();
        toMail.add(mail);
        return this;
    }

    private void sendPrepare() {
        if (smtpServer == null) {
            throw new RuntimeException("smtpServer 不能为空");
        }
        if (userName == null) {
            throw new RuntimeException("userName 不能为空");
        }
        if (password == null) {
            throw new RuntimeException("password 不能为空");
        }
        if (fromMail == null) {
            throw new RuntimeException("fromMail 不能为空");
        }
        if (toMail == null || toMail.isEmpty()) {
            throw new RuntimeException("toMail 不能为空");
        }
        if (content == null || toMail.isEmpty()) {
            content = "Content is empty!";
        }

        try {
            socket = new Socket(smtpServer, smtpPort);
            socket.setSoTimeout(3000);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException("连接到 " + smtpServer + ":" + smtpPort + " 失败", e);
        }

        reader = new BufferedReaderProxy(new InputStreamReader(in), showLog);
        writer = new PrintWriterProxy(out, showLog);
        reader.showResponse();
        writer.println("HELO " + smtpServer);
        reader.showResponse();
        writer.println("AUTH LOGIN");
        reader.showResponse();
        writer.println(new String(Base64.encodeBase64(userName.getBytes())));
        reader.showResponse();
        writer.println(new String(Base64.encodeBase64(password.getBytes())));
        reader.showResponse();
        writer.println("MAIL FROM:<" + fromMail + ">");
        reader.showResponse();
        for (String mail : toMail) {
            writer.println("RCPT TO:<" + mail + ">");
            reader.showResponse();
        }
        writer.println("DATA");
    }

    private void sendHeader() throws IOException {
        if (subject != null) {
            writer.println("Subject:" + "=?UTF-8?B?" + new String(Base64.encodeBase64(subject.getBytes("UTF-8")), "UTF-8") + "?=");
        }
        writer.println("From:" + fromMail);
        writer.print("To:");
        for (String mail : toMail) {
            writer.print(mail + "; ");
        }
        writer.println();
        contentSeparate = "=-content_separate_" + RandomStringUtils.random(30, true, true);
        writer.println("Content-Type:multipart/mixed;boundary=\"" + contentSeparate + "\"");
        writer.println();
    }

    private void sendBody() throws IOException {
        writer.println("--" + contentSeparate);
        if (isHtml) {
            writer.println("Content-Type: text/html; charset=UTF-8");
        } else {
            writer.println("Content-Type: text/plain; charset=UTF-8");
        }
        writer.println("Content-Transfer-Encoding: base64");
        writer.println();
        writer.println(new String(Base64.encodeBase64(content.getBytes("UTF-8"))));
        writer.println();
    }

    private void sendAttachments() throws IOException {
        if (attachments != null && !attachments.isEmpty()) {
            for (String name : attachments.keySet()) {
                writer.println("--" + contentSeparate);
                writer.println("Content-Type: application/octet-stream; name=\"=?UTF-8?B?" + new String(Base64.encodeBase64(name.getBytes("UTF-8")), "UTF-8") + "?=\"");
                writer.println("Content-Transfer-Encoding: base64");
                writer.println();

                InputStream attaIn = attachments.get(name);

                /*int len = 0;
                byte[] tmpBuf;
                byte[] buffer = new byte[1024 * 8];
                while ((len = attaIn.read()) != -1) {
                    System.out.println("长度" + len);
                    tmpBuf = new byte[len];
                    System.arraycopy(buffer, 0, tmpBuf, 0, len);
                    writer.print(new String(tmpBuf, "UTF-8"));
                }
                writer.println();*/

                // fixme 附件太大内存溢出
                byte[] datas = IOUtils.toByteArray(attaIn);
                attaIn.close();

                writer.println(new String(Base64.encodeBase64(datas), "UTF-8"));

                writer.println();
            }
        }
    }

    private void endSend() {
        writer.println("--" + contentSeparate + "--");
        writer.println(".");
        reader.showResponse();
        writer.println("QUIT");
        reader.showResponse();
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("发送邮件完成，关闭 Socket 出错：" + e.getMessage());
        }
    }

    public boolean send() {
        try {
            sendPrepare();
            sendHeader();
            sendBody();
            sendAttachments();
            endSend();
            return true;
        } catch (Exception e) {
            System.err.println("发送邮件出错：" + e.getMessage());
            return false;
        }
    }

    public String getSubject() {
        return subject;
    }

    public MailSender subject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getFromMail() {
        return fromMail;
    }

    public MailSender fromMail(String fromMail) {
        this.fromMail = fromMail;
        return this;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public MailSender smtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
        return this;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public MailSender smtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MailSender withContent(String content) {
        this.content = content;
        return this;
    }

    public List<String> getToMail() {
        return toMail;
    }

    public MailSender setToMail(List<String> toMail) {
        this.toMail = toMail;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public MailSender userName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MailSender password(String password) {
        this.password = password;
        return this;
    }

    public boolean getShowLog() {
        return showLog;
    }

    public MailSender showLog(boolean showLog) {
        this.showLog = showLog;
        return this;
    }

    public MailSender setAttachments(Map<String, InputStream> attachments) {
        this.attachments = attachments;
        return this;
    }

    public Map<String, InputStream> getAttachments() {
        return attachments;
    }

    public MailSender addAttachment(String name, InputStream in) {
        if (attachments == null)
            attachments = new HashMap<String, InputStream>();
        attachments.put(name, in);
        return this;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public MailSender setHtml(boolean isHtml) {
        this.isHtml = isHtml;
        return this;
    }

    static class PrintWriterProxy extends PrintWriter {
        private boolean showRequest;

        public PrintWriterProxy(OutputStream out, boolean showRequest) {
            super(out, true);
            this.showRequest = showRequest;
        }

        @Override
        public void println() {
            if (showRequest)
                System.out.println("Send:\\n");
            super.println();
        }

        public void print(String s) {
            if (showRequest)
                System.out.println("Send:" + s);
            super.print(s);
        }
    }

    static class BufferedReaderProxy extends BufferedReader {
        private boolean showResponse = true;

        public BufferedReaderProxy(Reader in, boolean showResponse) {
            super(in);
            this.showResponse = showResponse;
        }

        public void showResponse() {
            try {
                String line = readLine();
                String number = line.substring(0, 3);
                int num = -1;
                try {
                    num = Integer.parseInt(number);
                } catch (Exception e) {
                }
                if (num == -1) {
                    throw new RuntimeException("响应信息错误 : " + line);
                } else if (num >= 400) {
                    throw new RuntimeException("发送邮件失败 : " + line);
                }
                if (showResponse) {
                    System.out.println("Response:" + line);
                }
            } catch (IOException e) {
                System.out.println("获取响应失败");
            }
        }

    }
}