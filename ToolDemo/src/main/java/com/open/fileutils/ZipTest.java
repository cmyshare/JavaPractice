package com.open.fileutils;//package com.open.javabasetool.fileutils;
//
//import cn.chendd.compress.sevenz.CompressOutItemStructure;
//import cn.chendd.compress.sevenz.Item;
//import cn.chendd.compress.sevenz.SevenZCompressCallBack;
//import net.sf.sevenzipjbinding.IInArchive;
//import net.sf.sevenzipjbinding.IOutCreateArchive7z;
//import net.sf.sevenzipjbinding.SevenZip;
//import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
//import net.sf.sevenzipjbinding.impl.RandomAccessFileOutStream;
//import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
//import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
//import org.apache.commons.compress.utils.Lists;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.compress.utils.IOUtils;
//import org.apache.commons.lang3.StringUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @version 1.0
// * @Author cmy
// * @Date 2024/10/17 14:13
// * @desc 7-zip文件压缩测试
// */
//public class ZipTest {
//
//    /**
//     * 整体介绍：
//     * 7-Zip-jbinding是一个免费的跨平台java绑定7-Zip免费压缩/解压缩库，7z archiver引擎的Java包装器，内置了多个动态链接库文件来适配不同的操作系统，
//     * 支持Linux、Windows、Mac，以及32/64位系统的支持，可实现应用的跨平台，支持多种压缩格式的文件，
//     * 如：7zip、zip、gzip、rar（rar4和rar5）、tar、iso 等多种常见格式；支持压缩包文件的提取、分割卷的提、支持密码的压缩和解压缩、多线程等，对于程序的自动适配和兼容支持的比较友好。
//     *
//     * Java压缩和解压缩（五）总结一：
//     * https://www.chendd.cn/blog/article/1632056205882793985.html
//     */
//
//    /**
//     * 压缩文件（夹）
//     * @param compressFile 压缩文件
//     * @param sevenZipFile 压缩后的文件
//     * @return 压缩的文件列表
//     */
//    public static List<File> sevenZ(File compressFile, File sevenZipFile) {
//        return sevenZ(compressFile , sevenZipFile , null);
//    }
//
//    /**
//     * 带密码的文件（夹）压缩
//     * @param compressFile 压缩文件
//     * @param sevenZipFile 压缩后的文件
//     * @param password 密码
//     * @return 压缩的文件列表
//     */
//    public static List<File> sevenZ(File compressFile, File sevenZipFile, String password) {
//        //构建输出压缩对象
//        IOutCreateArchive7z outArchive = null;
//        RandomAccessFileOutStream outStream = null;
//        List<File> resultList = new ArrayList();
//        try {
//            outArchive = SevenZip.openOutArchive7z();
//            if (! sevenZipFile.getParentFile().exists()) {
//                sevenZipFile.getParentFile().mkdirs();
//            }
//            outStream = new RandomAccessFileOutStream(new RandomAccessFile(sevenZipFile, "rw"));
//            List<Item> items = CompressOutItemStructure.create(compressFile);
//            //压缩测试
//            outArchive.setLevel(5);
//            outArchive.setSolid(true);
//            //设置加密文件名
//            outArchive.setHeaderEncryption(true);
//            //创建压缩对象
//            outArchive.createArchive(outStream, items.size(), new SevenZCompressCallBack(compressFile, items , password));
//            items.forEach(item -> {
//                resultList.add(item.getFile());
//            });
//            return resultList;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (outStream != null) {
//                try {
//                    outStream.close();
//                } catch (IOException ignore) {}
//            }
//            IOUtils.closeQuietly(outArchive);
//        }
//    }
//
//    /**
//     * 判断是否解压缩成功，判断标准：解压缩后的文件（夹）大于 解压前的文件
//     * @param srcFile 源压缩包文件
//     * @param destFile 解压缩后的文件
//     * @return true 解压缩后的文件 大于 解压缩前的文件大小
//     */
//    public static boolean validatorUnSevenZ(File srcFile , File destFile) {
//        return FileUtils.sizeOf(destFile) > srcFile.length();
//    }
//
//    /**
//     * 解压缩
//     * @param zipFile 压缩包文件
//     * @param outFile 输出文件
//     * @return 文件夹列表
//     */
//    public static List<File> unSevenZ(File zipFile , File outFile) {
//        return unSevenZ(zipFile , outFile , null);
//    }
//
//    /**
//     * 解压缩带密码文件
//     * @param zipFile 压缩包文件
//     * @param outFile 输出文件
//     * @param password 密码
//     * @return 文件列表
//     */
//    public static List<File> unSevenZ(File zipFile , File outFile , String password) {
//        RandomAccessFileInStream inStream = null;
//        IInArchive inArchive = null;
//        boolean usePassword = StringUtils.isNotEmpty(password);
//        List<File> resultList = new ArrayList();
//        try {
//            RandomAccessFile randomAccessFile = new RandomAccessFile(zipFile, "r");
//            inStream = new RandomAccessFileInStream(randomAccessFile);
//            // 自动模式
//            if (usePassword) {
//                inArchive = SevenZip.openInArchive(null , inStream, password);
//            } else {
//                inArchive = SevenZip.openInArchive(null, inStream);
//            }
//            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();
//            ISimpleInArchiveItem[] archiveItems = simpleInArchive.getArchiveItems();
//            for (ISimpleInArchiveItem item : archiveItems) {
//                RandomAccessFileOutStream rafo = null;
//                try {
//                    File file;
//                    if (item.isFolder()) {
//                        new File(outFile , item.getPath()).mkdirs();
//                        continue;
//                    } else {
//                        file = new File(outFile , item.getPath());
//                        if (! file.getParentFile().exists()) {
//                            file.getParentFile().mkdirs();
//                        }
//                    }
//
//                    rafo = new RandomAccessFileOutStream(new RandomAccessFile(file , "rw"));
//                    if (usePassword) {
//                        item.extractSlow(rafo , password);
//                    } else {
//                        item.extractSlow(rafo);
//                    }
//                    resultList.add(file);
//                } finally {
//                    if (rafo != null) {
//                        rafo.close();
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            IOUtils.closeQuietly(inStream);
//            IOUtils.closeQuietly(inArchive);
//        }
//        return resultList;
//    }
//}
