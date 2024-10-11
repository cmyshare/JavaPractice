package com.open.javabasetool.fileutils;

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
public class ManyFileZip {

    /**
     * 整体思路：
     * 1创建一个父级文件夹：工单。
     * 2在父级文件夹下循环创建二级文件夹：SPU。
     * 3知道文件url，在二级文件夹SPU中循环加入多个指定名称文件url。
     * 4返回父级文件夹：工单。
     * 5最后将父级文件夹：工单打包成zip。
     * 写一个满足这个需求，创建文件夹+加入文件的工具类。
     *
     * 创建文件夹
     *
     */

    public static void main(String[] args) {
        List<String> urls = new ArrayList<>();
        urls.add("https://static.cmyshare.cn/SpaceX/cmyshare.jpg");
        urls.add("https://cdn.wwads.cn/creatives/oSReKgEOBzJ3wzX2Yt8DUALdcYwlx2ppSXkIpbTu.png");
        try {
            downloadZipFromUrls(urls, "output.zip");
            System.out.println("ZIP 文件创建成功并下载完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadZipFromUrls(List<String> urls, String outputZipFileName) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(outputZipFileName));
            for (String url : urls) {
                String fileName = getFileNameFromUrl(url);
                if (fileName!= null) {
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    zipOut.putNextEntry(zipEntry);
                    InputStream in = getInputStreamFromUrl(url);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zipOut.write(buffer, 0, len);
                    }
                    in.close();
                    zipOut.closeEntry();
                }
            }

    }

    /**
     * 根据url获取文件输入流
     * @param urlString
     * @return
     * @throws IOException
     */
    private static InputStream getInputStreamFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return connection.getInputStream();
    }

    /**
     * 根据url获取文件名
     * @param url
     * @return
     */
    private static String getFileNameFromUrl(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < url.length() - 1) {
            return url.substring(lastSlashIndex + 1);
        }
        return null;
    }
}
