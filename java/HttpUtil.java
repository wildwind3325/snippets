package org.clkg;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    public static String request(String url, String method, String body, Map<String, String> headers) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod(method);
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
            if (body != null && body.length() > 0) {
                conn.setDoOutput(true);
                String contentType = "application/json; charset=utf-8";
                if (headers.containsKey("Content-Type")) {
                    contentType = headers.get("Content-Type");
                }
                String encoding = "utf-8";
                if (contentType.contains("charset=")) {
                    encoding = contentType.substring(contentType.indexOf("charset=") + 8);
                }
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), encoding);
                osw.write(body);
                osw.flush();
                osw.close();
            }
            int rc = conn.getResponseCode();
            if (rc == HttpURLConnection.HTTP_OK) {
                String encoding = "utf-8";
                Map<String, List<String>> hfs = conn.getHeaderFields();
                for (String key : hfs.keySet()) {
                    if ("Content-Type".equalsIgnoreCase(key)) {
                        String contentType = conn.getHeaderField(key);
                        if (contentType.contains("charset=")) {
                            encoding = contentType.substring(contentType.indexOf("charset=") + 8);
                        }
                        break;
                    }
                }
                byte[] buffer = new byte[1024];
                int len = 0;
                InputStream inputStream = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while ((len = inputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                bos.close();
                inputStream.close();
                return new String(bos.toByteArray(), encoding);
            } else {
                throw new Exception(rc + ", " + conn.getResponseMessage());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "";
        }
    }

    public static byte[] download(String url, String method, String body, Map<String, String> headers) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod(method);
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
            if (body != null && body.length() > 0) {
                conn.setDoOutput(true);
                String contentType = "application/json; charset=utf-8";
                if (headers.containsKey("Content-Type")) {
                    contentType = headers.get("Content-Type");
                }
                String encoding = "utf-8";
                if (contentType.contains("charset=")) {
                    encoding = contentType.substring(contentType.indexOf("charset=") + 8);
                }
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), encoding);
                osw.write(body);
                osw.flush();
                osw.close();
            }
            int rc = conn.getResponseCode();
            if (rc == HttpURLConnection.HTTP_OK) {
                byte[] buffer = new byte[1024];
                int len = 0;
                InputStream inputStream = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while ((len = inputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                bos.close();
                inputStream.close();
                return bos.toByteArray();
            } else {
                throw new Exception(rc + ", " + conn.getResponseMessage());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new byte[0];
        }
    }
}
