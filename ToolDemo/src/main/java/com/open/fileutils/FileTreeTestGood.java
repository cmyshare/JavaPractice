package com.open.fileutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author cmy
 * @version 1.0
 * @date 2024/10/12 21:56
 * @description 根据树形文件夹数据创建文件夹并保存文件的方法优化版
 */

public class FileTreeTestGood {

    /**
     * 根据树形文件夹数据创建文件夹并保存文件的方法
     * @param node
     * @param basePath
     * @return
     * @throws Exception
     */
    public static File createFoldersAndSaveFiles(FileTreeNode node, String basePath) throws Exception {
        // 确定当前文件夹的名称
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

        // 如果有文件 URL 和文件名的映射
        if (node.getNameUrlMap()!= null) {
            node.getNameUrlMap().forEach((k, v) -> {
                String suffixName = getFileFullSuffix(v);
                String fileName = k + suffixName;
                if (fileName!= null) {
                    File file = new File(folder, fileName);
                    if (!file.exists()) {
                        try {
                            // 从 URL 获取输入流
                            InputStream in = getInputStreamFromUrl(v);
                            // 使用文件输出流保存文件，这里增加缓冲区优化写入性能
                            try (FileOutputStream out = new FileOutputStream(file)) {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = in.read(buffer)) > 0) {
                                    out.write(buffer, 0, len);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        // 如果有子文件夹节点，递归处理
        if (node.getSonFileList()!= null &&!node.getSonFileList().isEmpty()) {
            for (FileTreeNode childNode : node.getSonFileList()) {
                createFoldersAndSaveFiles(childNode, basePath);
            }
        }
        return folder;
    }

    /**
     * 将文件夹压缩成zip文件
     *
     * @param folderToZip 要压缩的文件夹
     * @param zipFileName 压缩后的文件名
     * @return 压缩后的文件对象
     * @throws IOException 如果在压缩过程中发生 I/O 错误
     */
    public static File zipFolder(File folderToZip, String zipFileName) throws IOException {
        File zipFile = new File(zipFileName);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
            // 遍历文件夹及其子文件夹和文件
            Files.walk(folderToZip.toPath())
                    .forEach(path -> {
                        try {
                            if (!Files.isDirectory(path)) {
                                Path relativePath = folderToZip.toPath().relativize(path);
                                ZipEntry zipEntry = new ZipEntry(relativePath.toString());
                                zipOut.putNextEntry(zipEntry);
                                // 使用输入流读取文件内容，增加缓冲区优化读取性能
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
        }
        return zipFile;
    }

    /**
     * 从 URL 获取输入流
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
     * 获取完整文件后缀，例如：.jpg
     *
     * @param url 文件的 URL
     * @return 文件后缀名
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
            Map<String, String> fileUrlsMap = new java.util.HashMap<>();
            fileUrlsMap.put("sku" + Math.random(), "https://static.cmyshare.cn/SpaceX/cmyshare.jpg");
            fileUrlsMap.put("sku" + Math.random(), "https://cdn.wwads.cn/creatives/oSReKgEOBzJ3wzX2Yt8DUALdcYwlx2ppSXkIpbTu.png");
            fileTreeNode.setNameUrlMap(fileUrlsMap);
            fileTreeNode.setSonFileList(null);
            twoNode.add(fileTreeNode);
        }
        rootNode.setSonFileList(twoNode);

        String basePath = "output";
        try {
            //File createdFolder = createFoldersAndSaveFiles(rootNode, basePath);
            //System.out.println("操作完成，组装文件：" + createdFolder.getName());
            //File zipFile = zipFolder(createdFolder, rootNode.getFileName() + ".zip");
            //System.out.println("操作完成，生成了压缩文件：" + zipFile.getName());

            ////返回file
            //File fileToZipByTreeFileData = FileUtils.createFileToZipByTreeFileData(rootNode, basePath);

            //返回byte
            Map<String, byte[]> fileToZipByTreeFileData2 = CustomFileUtils.createFileToZipByTreeFileData2(rootNode, basePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}