package com.open.javabasetool.fileutils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author cmy
 * @version 1.0
 * @date 2024/10/12 1:23
 * @description
 */
public class CreateManyFile {
    //共同点: 提供一个创建文件(父级文件file,新建文件名列表及其文件清单),返回一个文件夹
    //提供公共方法  创建父级文件夹,在一个文件夹中创建一个文件夹,移动文件夹,把文件加入指定文件夹中,删除整个文件夹,将整个文件夹打包zip
    public static File createAndPopulateFoldersWithURLs(String parentFolderName, List<String> spuNames, List<String> fileUrls) throws IOException {
        File parentFolder = new File(parentFolderName);
        if (!parentFolder.exists()) {
            parentFolder.mkdir();
        }

        //在parentFolder下循环创建SPU文件
        for (String spu : spuNames) {
            File spuFolder = new File(parentFolder, spu);
            if (!spuFolder.exists()) {
                spuFolder.mkdir();
            }

            //在SPU文件下加入文件
            for (String url : fileUrls) {
                String fileName = getFileNameFromUrl(url);
                if (fileName!= null) {
                    File file = new File(spuFolder, new Date().getTime() +fileName);
                    if (!file.exists()) {
                        try (InputStream in = getInputStreamFromUrl(url);
                             FileOutputStream out = new FileOutputStream(file)) {
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = in.read(buffer)) > 0) {
                                out.write(buffer, 0, len);
                            }
                        }
                    }
                }
            }
        }

        return parentFolder;
    }

    public static void zipFolder(File folderToZip, String zipFileName) throws IOException {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFileName))) {
            Files.walk(Paths.get(folderToZip.getAbsolutePath()))
                    .filter(path ->!Files.isDirectory(path))
                    .forEach(path -> {
                        try {
                            ZipEntry zipEntry = new ZipEntry(folderToZip.getName() + File.separator + path.getFileName().toString());
                            zipOut.putNextEntry(zipEntry);
                            try (InputStream fileIn = new FileInputStream(path.toFile())) {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = fileIn.read(buffer)) > 0) {
                                    zipOut.write(buffer, 0, len);
                                }
                            }
                            zipOut.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
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
        //组装数据
        String parentFolderName = "工单";
        List<String> spuNames = new ArrayList<>();
        spuNames.add("SPU1");
        spuNames.add("SPU2");
        List<String> fileUrls = new ArrayList<>();
        fileUrls.add("https://static.cmyshare.cn/SpaceX/cmyshare.jpg");
        fileUrls.add("https://cdn.wwads.cn/creatives/oSReKgEOBzJ3wzX2Yt8DUALdcYwlx2ppSXkIpbTu.png");

        try {
            //组装文件完成
            File parentFolder = createAndPopulateFoldersWithURLs(parentFolderName, spuNames, fileUrls);
            //压缩成zip
            String zipFileName = "工单.zip";
            zipFolder(parentFolder, zipFileName);
            System.out.println("操作完成，生成了压缩文件：" + zipFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
