package com.jzhung.test.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试阿里云服务器性能
 */
public class AliyunServerTest {
    private static Hashtable<Long, Integer> timeMap = new Hashtable<Long, Integer>();

    public static int stuId = 1121301004;
    public static String logFIle = "~/log.txt";
    public static int reqCount = 0;
    public static int time = 0;
    public static int poolSize = 0;
    public static String baseUrl = "http://domain.com.cn:8891";

    public static void main(String[] e) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        //logFIle = "/home/admin/serverTest-" + format.format(new Date()) +".log";
        logFIle = "d:\\serverTest-" + format.format(new Date()) +".log";
        System.out.println("循环次数：");
        Scanner in = new Scanner(System.in);
        time = in.nextInt();

        System.out.println("线程池大小：");
        poolSize = in.nextInt();

        //logFIle = "d:\\log.txt";
        try {
            final List<String> urlList = getHttpUrl();
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(300);
            int totalReq = time * urlList.size();
            System.out.println("准备发起" + totalReq + "次请求");
            Thread.sleep(3000);

            for (int z = 0; z < time; z++) {
                for (int i = 0; i < urlList.size(); i++) {
                    final int j = i;
                    fixedThreadPool.execute(new Runnable() {
                        public void run() {
                            sendGet(urlList.get(j));
                            //System.out.println(j);
                        }
                    });
                }
            }
            fixedThreadPool.shutdown();
            long waitTime = 5000;
            while (!fixedThreadPool.isTerminated()) {
                System.out.println("等" + waitTime + "ms再检测");
                Thread.sleep(waitTime);
            }
            System.out.println("所有的子线程都结束了！");
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } finally {
            System.out.println("主线程结束");
            writeTxt();
        }
    }

    public static List<String> getHttpUrl() {
        List<String> urlList = new ArrayList<>();
        urlList.add(baseUrl + "/json/lesson/getLessons.action");
        urlList.add(baseUrl + "/json/classCourseware/getList.action?studentId="
                + stuId + "&lessonName=语文");
        urlList.add(baseUrl + "/json/exercise/getClassExercise4PAD.action");
        urlList.add(baseUrl + "/json/resource/getAllVideoByTime.action");
        urlList.add(baseUrl + "/json/userRecoder/getStudentDetailScore.action?student.studentId="
                + stuId + "");
        urlList.add(baseUrl + "/json/userRecoder/getStudentHistory.action?student.studentId="
                + stuId + "&currentPage=1&pageSize=10");
        urlList.add(baseUrl + "/json/userRecoder/getStudentScore.action?student.studentId="
                + stuId + "");
        urlList.add(baseUrl + "http://aliyun.t600.com.cn:8890/images/speakEnglish.png");
        urlList.add(baseUrl + "http://aliyun.t600.com.cn:8890/images/list.png");
        urlList.add(baseUrl + "http://aliyun.t600.com.cn:8890/images/group.png");
        urlList.add(baseUrl + "/padStudent/padStudentLoginShow.action?account="
                + stuId + "");
        urlList.add(baseUrl + "/courseDesign/studentCourseListShow.action?lesson.name=%E8%AF%AD%E6%96%87");
        urlList.add(baseUrl + "/courseDesign/studentCourseListShow.action?lesson.name=%E8%AF%AD%E6%96%87&parentId=368");
        urlList.add(baseUrl + "/json/pad/getAppList.action");
        urlList.add(baseUrl + "/json/pad/getDisplayApps.action?studentId="
                + stuId + "&mac=98fae33f4ea6");
        urlList.add(baseUrl + "/json/multipart/selectMultipart.action?sentenceId=31&start=0&end=10&studentId="
                + stuId + "");
        return urlList;
    }

    public static void testPost() throws IOException {
        String url = "http://aliyun.t600.com.cn:8890/images/upload.action";

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        MultipartEntity entity = new MultipartEntity();

        //添加字符参数
        StringBody stringBody = new StringBody("文件的描述");
        entity.addPart("desc", stringBody);

        //添加附件
        String file = "/home/sendpix0.jpg";
        FileBody fileBody = new FileBody(new File(file));
        entity.addPart("file", fileBody);

        //执行提交
        post.setEntity(entity);
        HttpResponse response = httpclient.execute(post);
        if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){

            HttpEntity entitys = response.getEntity();
            if (entity != null) {
                System.out.println(entity.getContentLength());
                System.out.println(EntityUtils.toString(entitys));
            }
        }
        httpclient.getConnectionManager().shutdown();
    }


    public static String sendGet(String urlNameString) {
        reqCount++;
        String result = "";
        BufferedReader in = null;
        URLConnection connection = null;
        long start = System.currentTimeMillis();
        try {
            URL realUrl = new URL(urlNameString);

            System.out.println("线程:" + Thread.currentThread().getName() + " 请求数：" + reqCount + " 获取：" + urlNameString);
            connection = realUrl.openConnection();
            HttpURLConnection urlCon = (HttpURLConnection) realUrl
                    .openConnection();
            urlCon.setConnectTimeout(100000);
            urlCon.setReadTimeout(100000);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            System.err.println("异常线程:" + Thread.currentThread().getName() + " 请求数：" + reqCount + " 获取：" + urlNameString);
            //System.out.println(e.toString());
            int count = 1;
            if (timeMap.containsKey(9999999)) {
                count = timeMap.get(9999999) + 1;
            }
            timeMap.put((long) 9999999, count);
        } finally {
            long time = System.currentTimeMillis() - start;
            int count = 1;
            if (timeMap.containsKey(time)) {
                count = timeMap.get(time) + 1;
            }
            timeMap.put(time, count);
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static void writeTxt() {
        System.out.println("保存文件：" + logFIle);
        try {

            //排序
            List<Map.Entry<Long, Integer>> infoIds =
                    new ArrayList<>(timeMap.entrySet());
            Collections.sort(infoIds, new Comparator<Map.Entry<Long, Integer>>() {
                public int compare(Map.Entry<Long, Integer> o1, Map.Entry<Long, Integer> o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            });

            StringBuilder builder = new StringBuilder();
            builder.append("循环次数:" + time);
            builder.append("线程池大小:" + poolSize);
            builder.append("\n");
            for (int i = 0; i < infoIds.size(); i++) {
                Map.Entry<Long, Integer> entry = infoIds.get(i);
                builder.append("时间：");
                builder.append(entry.getKey());
                builder.append(" 线程数:");
                builder.append(entry.getValue());
                builder.append("\n");
            }

            FileOutputStream fout = new FileOutputStream(logFIle);
            fout.write(builder.toString().getBytes("gbk"));
            fout.flush();
            fout.close();
            System.out.println("数据：" + builder.toString());
            System.out.println("结果文件保存在：" + logFIle);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}