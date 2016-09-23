package com.jzhung.test.http.site115;

import com.google.gson.Gson;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.Test;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 模拟115网盘添加上传
 */
public class Web115Test {
    private List<String> taskUrlList = new ArrayList<>();
    private String taskList;

    @Test
    public void testAddTask() throws IOException {
        String head = "d:\\115.txt";
        taskUrlList.add("http://blog.csdn.net/");
        taskUrlList.add("http://www.2cto.com/");

        System.out.println("离线下载:" + taskUrlList.toString());

        String postUrl = "http://115.com//web/lixian/?ct=lixian&ac=add_task_urls";
        Request request = Request.Post(postUrl);
        addHeaderFromFile(request, head);

        String uid = "1130701";
        String sign = "64c2ab0f10af1d66130f9f406250b538a00a5334";
        String time = "1474423200";
        String task = buildPostBody(uid, sign, time);
        //System.out.println("发送内容:" + task);
        String result = request.bodyString(task, ContentType.APPLICATION_FORM_URLENCODED)
                .execute()
                .returnContent()
                .asString();
        Gson gson = new Gson();
        LiXianResp liXianResp = gson.fromJson(result, LiXianResp.class);
        if(liXianResp.isState()){
            System.out.println("成功");
        }else {
            System.out.println("失败:" + result);
        }
    }

    /**
     * 从HTTP Analyzer转存的请求文件中分析出需要的请求头
     * origin：http://115.com/web/lixian/?ct=lixian&ac=add_task_urls
     * @param request
     * @param requestFile
     * @return
     * @throws FileNotFoundException
     */
    public Request addHeaderFromFile(Request request, String requestFile) throws FileNotFoundException {
        File headFile = new File(requestFile);
        if (!headFile.isFile() || !headFile.exists()) {
            throw new FileNotFoundException("头文件不存在 " + headFile.getAbsolutePath());
        }
        BufferedReader buffReader = new BufferedReader(new FileReader(headFile));
        String line = null;
        String key = null;
        String value = null;
        int mIndex = 0;
        try {
            while ((line = buffReader.readLine()) != null) {
                if (line.equals("\r\n")) {
                    break;
                }
                //System.out.println(line);
                mIndex = line.indexOf(":");
                if (mIndex == -1 || line.startsWith("Content-Length")) {
                    continue;
                }

                key = line.substring(0, mIndex).trim();
                value = line.substring(mIndex + 1, line.length()).trim();
                //System.out.println("添加头: " + key + ":" + value);
                request.addHeader(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return request;
    }

    /**
     * 构建POST请求体
     *
     * @param uid
     * @param sign
     * @param time
     * @return
     */
    public String buildPostBody(String uid, String sign, String time) {
        if (taskUrlList == null || taskUrlList.size() == 0) {
            throw new IllegalArgumentException("下载的网址不能为空");
        }

        StringBuilder builder = new StringBuilder();
        int flag = 0;
        try {
            for (int i = 0; i < taskUrlList.size(); i++) {
                if (flag > 0)
                    builder.append("&");
                builder.append("url%5B");
                builder.append(i);
                builder.append("%5D=");
                builder.append(URLEncoder.encode(taskUrlList.get(i), "utf-8"));
                flag++;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        builder.append("&uid=");
        builder.append(uid);
        builder.append("&sign=");
        builder.append(sign);
        builder.append("&time=");
        builder.append(time);
        return builder.toString();
    }

/*    @Test
    public void testBody() {
        taskUrlList.add("http://blog.csdn.net/");
        taskUrlList.add("http://www.2cto.com/");

        String uid = "1130701";
        String sign = "64c2ab0f10af1d66130f9f406250b538a00a5334";
        String time = "1474423200";
        String task = buildPostBody(uid, sign, time);
        System.out.println("发送内容:" + task);
    }*/
}
