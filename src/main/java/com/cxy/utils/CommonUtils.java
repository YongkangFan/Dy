package com.cxy.utils;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    public static String DOU_YIN_BASE_URL = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";

    public static String HUO_SHAN_BASE_URL = " https://share.huoshan.com/api/item/info?item_id=";

    public static String DOU_YIN_DOMAIN = "douyin";

    public static String HUO_SHAN_DOMAIN = "huoshan";

    public static String getLocation(String url) {
        try {
            URL serverUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("User-agent", "ua");//模拟手机连接
            conn.connect();
            String location = conn.getHeaderField("Location");
            return location;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String matchNo(String redirectUrl) {

        List<String> results = new ArrayList<>();
        //正则表达式，
        Pattern p = Pattern.compile("video/([\\w/\\.]*)/");
        Matcher m = p.matcher(redirectUrl);
        //Matcher类的hitEnd()方法用于检查此匹配器上的模式匹配是否已停止
        //find方法属于搜索匹配。比如传入str="222m333"，find方法会将这个字符串拆成若干个子字符串，
        // 只要有一个或多个子字符串符合正则表达式，则返回true。
        while (!m.hitEnd() && m.find()) {
            //java中使用正则表达式返回符合正则表达式的字符串就要用到group()
            results.add(m.group(1));
        }
        return results.get(0);
    }

    public static String hSMatchNo(String redirectUrl) {
        List<String> results = new ArrayList<>();
        Pattern p = Pattern.compile("item_id=([\\w/\\.]*)&");
        Matcher m = p.matcher(redirectUrl);
        while (!m.hitEnd() && m.find()) {
            results.add(m.group(1));
        }
        return results.get(0);
    }

    public static String httpGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "text/json;charset=utf-8");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuffer buf = new StringBuffer();
        String inputLine = in.readLine();
        while (inputLine != null) {
            buf.append(inputLine).append("\r\n");
            inputLine = in.readLine();
        }
        in.close();
        return buf.toString();
    }
}
