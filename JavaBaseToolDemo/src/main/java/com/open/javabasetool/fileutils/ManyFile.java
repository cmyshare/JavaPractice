package com.open.javabasetool.fileutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author cmy
 * @version 1.0
 * @date 2024/10/11 23:27
 * @description 多个文件打包zip并下载
 */
public class ManyFile {

    public static File saveFilesToFolderAndReturn(List<String> urls, String folderPath) throws IOException {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        for (String url : urls) {
            String fileName = getFileNameFromUrl(url);
            if (fileName!= null) {
                try (InputStream in = getInputStreamFromUrl(url);
                     FileOutputStream out = new FileOutputStream(new File(folderPath + File.separator + fileName))) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                }
            }
        }

        return folder;
    }

    private static InputStream getInputStreamFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return connection.getInputStream();
    }

    private static String getFileNameFromUrl(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < url.length() - 1) {
            return url.substring(lastSlashIndex + 1);
        }
        return null;
    }

    public static void main(String[] args) {
        List<String> urls = new ArrayList<>();
        urls.add("https://static.cmyshare.cn/SpaceX/cmyshare.jpg");
        urls.add("https://cdn.wwads.cn/creatives/oSReKgEOBzJ3wzX2Yt8DUALdcYwlx2ppSXkIpbTu.png");
        try {
            File folder = saveFilesToFolderAndReturn(urls, "targetFolder");
            System.out.println("文件已保存到文件夹：" + folder.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
