package com.jzhung.test.http.jsoup;

import com.jzhung.test.util.LogUitl;
import com.jzhung.test.util.UserAgent;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 测试内容页分析
 */
public class BaseParser {

    @Test
    public void testCSDN() {
        String url = "http://blog.csdn.net/a86261566/article/details/50906456";
        parse(url);
    }
    @Test
    public void testCNBlogs() {
        String url = "http://www.cnblogs.com/bastard/archive/2012/04/07/2436262.html";
        parse(url);
    }
    @Test
    public void testJianShu() {
        //fixme 图片无法获取
        String url = "http://www.jianshu.com/p/9fd98822ee44";
        parse(url);
    }
    @Test
    public void testWordpress1() {
        String url = "http://www.woaitqs.cc/android/2016/07/14/android-reflection";
        parse(url);
    }
    @Test
    public void testWordpress2() {
        String url = "http://gityuan.com/2016/05/15/event-log/";
        parse(url);
    }
    public void parse(String url) {
        Document doc = null;
        FileOutputStream fout = null;
        try {
            doc = Jsoup.connect(url).userAgent(UserAgent.getFirefoxAgent()).timeout(5000).get();
           /* String src = "d:\\temp\\csdn\\src.htm";
            Document doc = Jsoup.parse(new File(src), "utf-8");*/
            doc.charset(Charset.forName("utf-8"));

            Element contentDiv = getContentDiv(doc);

            List<Node> nodeList = contentDiv.childNodes();
            StringBuilder builder = new StringBuilder();
            builder.append("<html>\n<head>\n<title>" + doc.title() + "</title>\n");
            builder.append("<link id=\"linkstyle\" rel='stylesheet' href='markdown.css'/>\n");
            builder.append("<link id=\"linkstyle1\" rel='stylesheet' href='style.css'/>\n");
            builder.append("</head>\n<body>\n<div id=\"con\">");
            builder = doNode(nodeList, builder);
            builder.append("</div></body>\n</html>");
            String saveFile = "d:\\temp\\csdn\\test.html";
            fout = new FileOutputStream(saveFile);
            fout.write(builder.toString()
                    .replace("<br><br><br><br>", "")
                    .replace("<br><br><br>", "")
                    .replace("<br><br>", "")
                    .getBytes("gbk"));
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 自动分析文章内容区域
     *
     * @param doc
     * @return
     */
    private Element getContentDiv(Element doc) {
        Element contentDiv = null;
        Elements pList = doc.select("p");
        if (pList != null && pList.size() > 0) {
            //LogUitl.info("P数量" + pList.size());
            for (Element p : pList) {
                String divclz = p.attr("class");
                if (!"".equals(divclz)) {
                    continue;
                }
                contentDiv = p.parent();
                LogUitl.info("自动分析出内容区域");
                break;
            }
        } else {
            contentDiv = doc.getElementById("article_content");
        }
        return contentDiv;
    }

    public StringBuilder doNode(List<Node> list, StringBuilder builder) {

        for (Node node : list) {
            String outHtml = node.outerHtml().trim();
            if (outHtml.equals("")) {//如果不是空的进行设置类型
                //log.info("跳过空"+outHtml);
                continue;
            }

            //log.info("outHtml:" + outHtml);
            String cls = node.attr("class");
            String id = node.attr("id");
            LogUitl.debug("节点:" + node.nodeName() + (cls.equals("") ? "" : " class:" + cls) + (id.equals("") ? "" : " id:" + id) + " 内容:" + node.outerHtml());

            String nodeName = node.nodeName();
            if (nodeName.equals("style") || nodeName.equals("script")) {
                LogUitl.debug("跳过样式和JS");
                continue;
            }
            if (nodeName.startsWith("h")) {
                builder.append(node.outerHtml());
                continue;
            }
            if (nodeName.equals("p")) {
                String text = ((Element) node).text();
                if (!text.equals("")) {
                    builder.append(node.outerHtml());
                }
                continue;
            }

            //代码节点
            if (nodeName.equals("pre")) {
                String text = ((Element) node).text()
                       /* .replace("\"", "&quot;")*/
                        .replace("&", "&amp;")
                        .replace("<", "&lt;")
                        .replace(">", "&gt;");
                LogUitl.debug("[代码]\n" + node.toString());
                String name = node.attr("name");
                String codeclass = node.attr("class");
                builder.append("<pre name=\"");
                builder.append(name + "\" ");
                builder.append("class=\"");
                builder.append(codeclass);
                builder.append("\">");
                builder.append(text);
                builder.append("</pre>");
                //LogUitl.debug("[代码text]\n" + text);
                continue;
            }
            if (nodeName.equals("table")) {
                String text = ((Element) node).text()
                       /* .replace("\"", "&quot;")*/
                        .replace("&", "&amp;")
                        .replace("<", "&lt;")
                        .replace(">", "&gt;");
                LogUitl.debug("[table]\n" + node.toString());
                builder.append(node.outerHtml());
                continue;
            }


            if (node.childNodeSize() != 0) {
                doNode(node.childNodes(), builder);
                //builder.append("</"+nodeName+">");
            } else {
                if (outHtml.indexOf("<img") >= 0) {//如果是 img标签进行标识type
                    String netUrl = node.attr("abs:file");
                    if (netUrl.equals("")) {
                        continue;
                    }
                    String fileName = System.currentTimeMillis() + ".jpg";
                    //todo 本地路径和网络路径的处理
                    File downResultFile = new File("d:\\temp\\csdn\\" + fileName);
                    //不存在才下载
                    if (!downResultFile.exists()) {
                        //downResultFile = client.download(netUrl, savePath);
                        try {
                            Request.Get(netUrl).execute().saveContent(downResultFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //log.info("文件已存在磁盘 " + savePath + " url:" + netUrl);
                    }

                    //下载成功后不再下载
                    if (downResultFile.exists()) {
                        long imgLen = downResultFile.length();
                        if (imgLen < 100) {
                            downResultFile.delete();
                            //log.info("下载图片失败 文件大小太小:" + imgLen + " " + savePath + " url:" + netUrl);
                            continue;
                        }
                    } else {
                        //log.info("下载图片失败， 文件不存在 " + savePath + " url:" + netUrl);
                    }
                    //Res res = new Res();
                    //localImgList.add(res);


                    builder.append("<img src=\"" + fileName + "\">");
                    builder.append("<br/>");

                    LogUitl.info("[img]" + node.attr("file"));
                } else {
                   /* if (nodeName.equals("p")) {
                        String text = ((Element)node).text();
                        builder.append("<p>");
                        String clearText = text.replace(Jsoup.parse("&nbsp;").text(), "").trim();
                        builder.append(clearText);
                        builder.append("</p>");
                        //LogUitl.debug("[段落]\n" + node.toString());
                        //LogUitl.debug("[段落清理后]\n<p>" + clearText + "</p>");
                        continue;
                    } if (nodeName.equals("span")){
                        String text = ((Element)node).text();
                        builder.append(text);
                        continue;
                    }*/

                    /*if (nodeName.equals("a")) {
                        //node.html(builder);
                        builder.append(node.outerHtml());
                        LogUitl.debug("[超链接]\n" + node.outerHtml());
                        continue;
                    }*/

                    String text = node.outerHtml();
                    LogUitl.info("[文本]\n" + text);

                    Node parent = node.parentNode();
                    if (parent.nodeName().equals("strong")) {
                        LogUitl.info("[strong]" + parent.toString());
                        builder.append(parent.toString());
                    } else if (parent.nodeName().equals("span")) {
                        LogUitl.info("[span]" + parent.toString());
                        builder.append(parent.toString());
                    } else if (parent.nodeName().equals("a")) {
                        LogUitl.info("[a]" + parent.toString());
                        builder.append(parent.outerHtml());
                    } else {
                        String clearText = text.replace("\\u00A0", "");
                        builder.append(clearText);
                        builder.append("<br>");
                    }
                }
            }

        }
        return builder;
    }

}
