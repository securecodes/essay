package cc.ends.unitrans;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by ends on 2/27/15.
 */
public class CheckUpdate extends Thread{
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("user-agent","from ends.cc tool");
            conn.setConnectTimeout(5000);
            // 建立实际的连接
            conn.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            return null;
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                return null;
            }
        }
        return result;
    }

    /**
     * 多线程Run方法
     * */
    public void run(){
        String result = sendGet("http://ends.cc/","sdfasdf=1");
        if(result != null){
            Object[] options = {"Update now","Close"};
            int m = JOptionPane.showOptionDialog(null, "A new version of Unitrans is now available.", "Unicode Transformation Issues",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if(m == JOptionPane.NO_OPTION){
                return;
            }else{
                openURL("http://ends.cc/");
            }
        }
    }


    static final String[] browsers = { "google-chrome", "firefox", "opera","epiphany", "konqueror", "conkeror", "midori", "kazehakase", "mozilla" };
    static final String errMsg = "Error attempting to launch web browser";

    /**
     * 调用本地浏览器打开链接地址
     *
     * @param url 发送请求的URL
     * */
    public void openURL(String url) {
        try {
            //反射加载JDK 1.6+ java.awt.Desktop
            Class<?> d = Class.forName("java.awt.Desktop");
            d.getDeclaredMethod("browse", new Class[] { java.net.URI.class }).invoke(d.getDeclaredMethod("getDesktop").invoke(null), new Object[]{java.net.URI.create(url)});
            // above code mimicks: java.awt.Desktop.getDesktop().browse()
        } catch (Exception ignore) { // library not available or failed
            String osName = System.getProperty("os.name");
            try {
                if (osName.startsWith("Mac OS")) {
                    Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL", new Class[]{String.class}).invoke(null, new Object[]{url});
                } else if (osName.startsWith("Windows")){
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                }else {
                //判断Unix or Linux
                    String browser = null;
                    for (String b : browsers)
                        if (browser == null&& Runtime.getRuntime().exec(new String[] { "which", b }).getInputStream().read() != -1)
                            Runtime.getRuntime().exec(new String[] { browser = b, url });
                    if (browser == null)
                        throw new Exception(Arrays.toString(browsers));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, errMsg + "\n" + e.toString());
            }
        }
    }
}
