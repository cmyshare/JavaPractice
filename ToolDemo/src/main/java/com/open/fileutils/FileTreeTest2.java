package com.open.fileutils;

import lombok.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author cmy
 * @version 1.0
 * @date 2024/10/12 21:56
 * @description 根据树形文件夹数据创建文件夹并保存文件的方法第2版
 */
@Data
public class FileTreeTest2 {
    public static File createFoldersAndSaveFiles(FileTreeNode node, String basePath) throws Exception {
        String currentFolderName = node.getFileName();
        if (node.getParentFileName() == null) {
            currentFolderName = basePath + File.separator + currentFolderName;
        } else {
            File parentFolder = new File(basePath + File.separator + node.getParentFileName());
            currentFolderName = parentFolder.getAbsolutePath() + File.separator + currentFolderName;
        }

        File folder = new File(currentFolderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (node.getNameUrlMap() != null) {
            node.getNameUrlMap().forEach((k, v) -> {
                String suffixName = getFileFullSuffix(v);
                String fileName = k+suffixName;
                if (fileName != null) {
                    File file = new File(folder, fileName);
                    if (!file.exists()) {
                        try {
                            InputStream in = getInputStreamFromUrl(v);
                            FileOutputStream out = new FileOutputStream(file);
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = in.read(buffer)) > 0) {
                                out.write(buffer, 0, len);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        if (node.getSonFileList() != null && !node.getSonFileList().isEmpty()) {
            for (FileTreeNode childNode : node.getSonFileList()) {
                createFoldersAndSaveFiles(childNode, basePath);
            }
        }
        return folder;
    }

    ///**
    // * 直接取file出来统一压缩，破坏了层级结构
    // * @param folderToZip
    // * @param zipFileName
    // * @return
    // * @throws IOException
    // */
    //public static File zipFolder(File folderToZip, String zipFileName) throws IOException {
    //    File zipFile = new File(zipFileName);
    //    ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFileName));
    //    Files.walk(Paths.get(folderToZip.getAbsolutePath()))
    //            .filter(path -> !Files.isDirectory(path))
    //            .forEach(path -> {
    //                try {
    //                    ZipEntry zipEntry = new ZipEntry(folderToZip.getName() + File.separator + path.getFileName().toString());
    //                    zipOut.putNextEntry(zipEntry);
    //                    try (InputStream fileIn = new FileInputStream(path.toFile())) {
    //                        byte[] buffer = new byte[1024];
    //                        int len;
    //                        while ((len = fileIn.read(buffer)) > 0) {
    //                            zipOut.write(buffer, 0, len);
    //                        }
    //                    }
    //                    zipOut.closeEntry();
    //                } catch (IOException e) {
    //                    e.printStackTrace();
    //                }
    //            });
    //    return zipFile;
    //}

    /**
     * 保留了层级，但解压后，直接就时二级文件
     * @param folderToZip
     * @param zipFileName
     * @return
     * @throws IOException
     */
    public static File zipFolder(File folderToZip, String zipFileName) throws IOException {
        File zipFile = new File(zipFileName);
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            Files.walk(folderToZip.toPath())
                    .forEach(path -> {
                        try {
                            if (!Files.isDirectory(path)) {
                                Path relativePath = folderToZip.toPath().relativize(path);
                                ZipEntry zipEntry = new ZipEntry(relativePath.toString());
                                zipOut.putNextEntry(zipEntry);
                                try (InputStream fileIn = Files.newInputStream(path)) {
                                    byte[] buffer = new byte[1024];
                                    int len;
                                    while ((len = fileIn.read(buffer)) > 0) {
                                        zipOut.write(buffer, 0, len);
                                    }
                                }
                                zipOut.closeEntry();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        return zipFile;
    }


    private static InputStream getInputStreamFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return connection.getInputStream();
    }

    /**
     * 获取完整文件后缀，例如：.jpg
     * @param url
     * @return
     */
    private static String getFileFullSuffix(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < url.length() - 1) {
            String fileName = url.substring(lastSlashIndex + 1);
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex >= 0) {
                String extension = fileName.substring(dotIndex);
                return extension.startsWith(".")? extension : "." + extension;
            }
        }
        return null;
    }

    /**
     * 获取完整文件名，例如：cmyshare.jpg
     * @param url
     * @return
     */
    private static String getFileNameFromUrl(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < url.length() - 1) {
            System.out.println("获取完整文件名，例如：cmyshare.jpg============"+url.substring(lastSlashIndex + 1));
            return url.substring(lastSlashIndex + 1);
        }
        return null;
    }

    public static void main(String[] args) {
        // 创建一个示例的树形结构
        FileTreeNode rootNode = new FileTreeNode();
        rootNode.setParentFileName(null);
        rootNode.setFileName("one工单");
        rootNode.setNameUrlMap(null);
        // 创建二级文件夹
        List<FileTreeNode> twoNode = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            FileTreeNode fileTreeNode = new FileTreeNode();
            fileTreeNode.setParentFileName("one工单");
            fileTreeNode.setFileName("two商品SPU" + i);
            Map<String, String> fileUrlsMap = new HashMap<>();
            fileUrlsMap.put("sku" + Math.random(), "https://static.cmyshare.cn/SpaceX/cmyshare.jpg");
            fileUrlsMap.put("sku" + Math.random(), "https://cdn.wwads.cn/creatives/oSReKgEOBzJ3wzX2Yt8DUALdcYwlx2ppSXkIpbTu.png");
            fileTreeNode.setNameUrlMap(fileUrlsMap);
            fileTreeNode.setSonFileList(null);
            twoNode.add(fileTreeNode);
        }
        rootNode.setSonFileList(twoNode);

        String basePath = "output";
        try {
            File createdFolder = createFoldersAndSaveFiles(rootNode, basePath);
            System.out.println("操作完成，组装文件：" + createdFolder.getName());
            ////根据文件路径获取File
            //File rootFolder = new File(basePath + File.separator + rootNode.getFileName());
            //String zipFileName = "rootFolder.zip";
            File zipFile = zipFolder(createdFolder, rootNode.getFileName()+".zip");

            System.out.println("操作完成，生成了压缩文件：" + zipFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
