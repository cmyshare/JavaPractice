package com.open.javabasetool.fileutils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author cmy
 * @version 1.0
 * @date 2024/10/13 0:46
 * @description 自定义文件处理工具类
 */
public class CustomFileUtils {

    /**
     * 根据树形文件夹数据创建文件夹并保存文件的方法
     *
     * @param node     树形文件夹数据
     * @param basePath 临时路径
     * @return
     * @throws Exception
     */
    public static File createFoldersAndSaveFiles(FileTreeNode node, String basePath) throws Exception {

        Assert.isTrue(ObjectUtil.isNotEmpty(node), "文件夹树节点不能为空!");
        Assert.isTrue(StrUtil.isNotBlank(basePath), "临时路径不能为空!");
        //父级文件夹名称可以为空! 根节点的父级文件夹名称为空! 其他节点的父级文件夹名称不为空!
        Assert.isTrue(StrUtil.isNotBlank(node.getFileName()), "当前文件夹名称不能为空!");
        //当前文件夹中文件数据可以为空!
        //子级文件夹可以为空! 叶子节点的子级文件夹为空!

        //确定当前文件夹的名称。如果当前节点没有父文件夹名称，那么直接将基础路径和当前文件夹名称组合起来。
        // 如果有父文件夹名称，先创建父文件夹的File对象，然后将父文件夹的绝对路径和当前文件夹名称组合起来。
        String currentFolderName = node.getFileName();
        if (node.getParentFileName() == null) {
            currentFolderName = basePath + File.separator + currentFolderName;
        } else {
            File parentFolder = new File(basePath + File.separator + node.getParentFileName());
            currentFolderName = parentFolder.getAbsolutePath() + File.separator + currentFolderName;
        }

        //创建当前文件夹的File对象。如果该文件夹不存在，则创建它。
        File folder = new File(currentFolderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 如果有文件 URL 和文件名的映射
        if (node.getNameUrlMap() != null) {
            node.getNameUrlMap().forEach((k, v) -> {
                //获取完整文件后缀，例如：.jpg
                String suffixName = getFileFullSuffix(v);
                //组装完整文件名称=文件名+后缀
                String fileName = k + suffixName;
                //如果文件名不为空，创建对应的文件File对象。如果该文件不存在，从 URL 获取输入流，然后使用文件输出流将文件保存到本地。
                if (fileName != null) {
                    File file = new File(folder, fileName);
                    if (!file.exists()) {
                        try {
                            // 从 URL 获取输入流
                            InputStream in = getInputStreamFromUrl(v);
                            // 使用文件输出流保存文件，这里增加缓冲区优化写入性能
                            // Java 7 引入的 try-with-resources 语句来自动管理资源
                            try (FileOutputStream out = new FileOutputStream(file)) {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = in.read(buffer)) > 0) {
                                    out.write(buffer, 0, len);
                                }
                            }
                        } catch (Exception e) {
                            // 在这里可以记录日志或进行其他更详细的错误处理
                            throw new RuntimeException("使用文件输出流将文件保存到本地异常！", e);
                        }
                    }
                }
            });
        }

        // 如果有子文件夹节点，递归处理
        if (CollUtil.isNotEmpty(node.getSonFileList())) {
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
    public static File zipFolder(File folderToZip, String zipFileName) throws Exception {
        File zipFile = new File(zipFileName);
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
        // 遍历文件夹及其子文件夹和文件
        Files.walk(folderToZip.toPath()).forEach(path -> {
            try {
                if (!Files.isDirectory(path)) {
                    Path relativePath = folderToZip.toPath().relativize(path);
                    ZipEntry zipEntry = new ZipEntry(relativePath.toString());
                    zipOut.putNextEntry(zipEntry);
                    // 使用输入流读取文件内容，增加缓冲区优化读取性能
                    // Java 7 引入的 try-with-resources 语句来自动管理资源
                    try (InputStream fileIn = Files.newInputStream(path)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fileIn.read(buffer)) > 0) {
                            zipOut.write(buffer, 0, len);
                        }
                    }
                    zipOut.closeEntry();
                }
            } catch (Exception e) {
                // 在这里可以记录日志或进行其他更详细的错误处理
                throw new RuntimeException("将文件夹压缩成zip文件异常！", e);
            }
        });
        zipOut.close();

        return zipFile;
    }

    /**
     * 从 URL 获取输入流
     *
     * @param urlString
     * @return
     */
    public static InputStream getInputStreamFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == 200) {
                return connection.getInputStream();
            } else {
                throw new IOException("从 URL 获取输入流失败。响应码：" + connection.getResponseCode() + "。URL：" + urlString);
            }
        } catch (Exception e) {
            // 在这里可以记录日志或进行其他更详细的错误处理
            throw new RuntimeException("从 URL 获取输入流时发生错误。请检查 URL 是否能访问！" + urlString, e);
        }
    }

    /**
     * 获取完整文件后缀，例如：.jpg
     *
     * @param url 文件的 URL
     * @return 文件后缀名
     */
    public static String getFileFullSuffix(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < url.length() - 1) {
            String fileName = url.substring(lastSlashIndex + 1);
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex >= 0) {
                String extension = fileName.substring(dotIndex);
                return extension.startsWith(".") ? extension : "." + extension;
            }
        }
        return null;
    }

    /**
     * 根据File读取字节流
     *
     * @param file 要读取的文件对象
     * @return 文件内容的字节数组
     * @throws IOException 如果在读取文件过程中发生 I/O 错误
     */
    public static byte[] getBytesFromFile(File file) throws Exception {
        try (FileInputStream fis = new FileInputStream(file);
             FileChannel channel = fis.getChannel()) {
            // 获取文件的长度
            long fileSize = file.length();
            // 分配一个与文件大小相同的缓冲区
            ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
            // 循环读取文件内容到缓冲区，直到读取完毕或缓冲区位置与文件大小一致
            while (channel.read(buffer) > 0 || buffer.position() != fileSize) ;
            // 将缓冲区切换为读模式
            buffer.flip();
            // 创建一个与缓冲区大小相同的字节数组
            byte[] bytes = new byte[buffer.limit()];
            // 将缓冲区中的内容读取到字节数组中
            buffer.get(bytes);
            return bytes;
        }
    }

    /**
     * 删除文件或者目录。
     *
     * @param file 要删除的文件或目录对象。
     * @return 如果成功删除，返回被删除的文件或目录的名称；如果文件不存在或无法删除，返回 null。
     */
    public static String deleteFile(File file) {
        // 检查文件或目录是否存在
        if (file.exists()) {
            // 如果是文件
            if (file.isFile()) {
                // 删除文件
                file.delete();
                // 返回被删除文件的名称
                return file.getName();
            } else if (file.isDirectory()) {
                // 如果是目录，获取目录下的所有文件和子目录
                File[] files = file.listFiles();
                if (files != null) {
                    // 递归删除目录下的每个文件和子目录
                    for (File subFile : files) {
                        deleteFile(subFile);
                    }
                }
                // 删除该目录本身
                file.delete();
                // 返回被删除目录的名称
                return file.getName();
            }
        }
        // 如果文件不存在或无法删除，返回 null
        return null;
    }

    /**
     * 根据树形文件夹数据创建文件夹压缩打包成zip文件，返回文件File
     */
    public static File createFileToZipByTreeFileData(FileTreeNode rootNode, String basePath) throws Exception {
        File createdFolder = createFoldersAndSaveFiles(rootNode, basePath);
        System.out.println("操作完成，组装文件：" + createdFolder.getName());
        File zipFile = zipFolder(createdFolder, rootNode.getFileName() + ".zip");
        System.out.println("操作完成，生成了压缩文件：" + zipFile.getName());
        //得到文件名，文件字节流后
        return zipFile;
    }

    /**
     * 根据树形文件夹数据创建文件夹压缩打包成zip文件,返回文件名+字节流
     */
    public static Map<String, byte[]> createFileToZipByTreeFileData2(FileTreeNode rootNode, String basePath) throws Exception {
        File createdFolder = createFoldersAndSaveFiles(rootNode, basePath);
        System.out.println("操作完成，组装文件：" + createdFolder.getName());
        File zipFile = zipFolder(createdFolder, rootNode.getFileName() + ".zip");
        System.out.println("操作完成，生成了压缩文件：" + zipFile.getName());
        //得到文件名+字节流
        HashMap<String, byte[]> fileMap = new HashMap<>();
        byte[] bytesFromFile = getBytesFromFile(zipFile);
        fileMap.put(zipFile.getName(), bytesFromFile);
        System.out.println("得到文件名+字节流" + zipFile.getName());
        System.out.println("得到文件名+字节流" + bytesFromFile.length);
        ////删除相关文件夹及其文件
        //String s = deleteFile(createdFolder);
        //System.out.println("删除相关文件夹及其文件"+s);
        //String s1 = deleteFile(zipFile);
        //System.out.println("删除相关文件夹及其文件"+s1);
        return fileMap;
    }


}
