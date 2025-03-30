package com.open.image;

import net.coobird.thumbnailator.Thumbnails;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author CmyShare
 * @date 2025/3/30
 * @description Java图片处理库Thumbnails
 */
public class UpdateImageWidthHeight {

    /**
     * 参考链接
     * https://www.cnblogs.com/fswhq/p/17729487.html
     * https://juejin.cn/post/7055461293456621582
     * https://mvnrepository.com/artifact/net.coobird/thumbnailator/0.4.20
     * https://github.com/coobird/thumbnailator
     *
     */


    /**
     * 使用Thumbnails库-读取网路图片进行100%质量图片缩放
     * @param args
     */
    public static void main(String[] args) {
        String imageUrl = "https://static.cmyshare.cn/one.jpg"; // 请替换为实际的图片网络地址
        byte[] bytes = ImageZoomingByUrl(imageUrl, 1000, 1000);
        System.out.println(bytes);

        String imageUrl2 = "https://static.cmyshare.cn/SpaceX/spaceXcompare.jpg"; // 替换为实际的图片网络地址
        byte[] bytes1 = ImageFillerByUrl(imageUrl2);
        System.out.println(bytes1);
    }

    public static byte[] ImageZoomingByUrl(String imageUrl, int width, int height) {
        //String imageUrl = "https://static.cmyshare.cn/one.jpg"; // 请替换为实际的图片网络地址
        String outputFilePath = "D:\\Desk\\output.jpg"; // 输出文件路径
        try {
            // 从网络读取图片
            URL url = new URL(imageUrl);
            // 使用 Thumbnails 库进行图片缩放
            File file = new File(outputFilePath);
            Thumbnails.of(url)
                    .width(width) // 设置目标宽度
                    .outputQuality(1.0) // 设置输出质量为 100%
                    .toFile(file);
            System.out.println("图片已成功缩放并保存。");


            // 使用 Thumbnailator 保存处理后的图片，并设置质量为 100%
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Thumbnails.of(url)
                    .width(width)
                    .outputQuality(1.0) // 设置输出质量为 100%
                    .outputFormat("jpg") // 指定输出格式为 JPG
                    .toOutputStream(byteArrayOutputStream);
            byteArrayOutputStream.close();
            System.out.println("输出字节数组，图片已成功缩放并保存。"+byteArrayOutputStream.toByteArray().length);

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用Thumbnails库-读取网路图片对非1:1图片进行100%质量图片补白正方形
     * @param imageUrl
     * @return
     */
    public static byte[] ImageFillerByUrl(String imageUrl) {
        //String imageUrl = "https://static.cmyshare.cn/SpaceX/spaceXcompare.jpg"; // 替换为实际的图片网络地址
        String outputFilePath = "D:\\Desk\\outputWhite.jpg"; // 输出文件路径

        try {
            // 从网络读取图片并获取原始尺寸
            URL url = new URL(imageUrl);
            BufferedImage originalImage = Thumbnails.of(url)
                    .scale(1.0) // 按原始比例加载图片
                    .asBufferedImage();

            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            // 计算目标正方形尺寸（取宽高中的较大值）
            int canvasSize = Math.max(originalWidth, originalHeight);

            // 创建一个正方形的空白画布，默认背景为白色
            BufferedImage squareImage = new BufferedImage(canvasSize, canvasSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = squareImage.createGraphics();

            // 设置抗锯齿和高质量渲染
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            // 填充背景为白色
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, canvasSize, canvasSize);

            // 将原图绘制到正方形画布中央
            int x = (canvasSize - originalWidth) / 2;
            int y = (canvasSize - originalHeight) / 2;
            g2d.drawImage(originalImage, x, y, null);

            // 释放绘图资源
            g2d.dispose();

            File file = new File(outputFilePath);
            // 使用 Thumbnailator 保存处理后的图片，并设置质量为 100%
            Thumbnails.of(squareImage)
                    .size(canvasSize, canvasSize)
                    .outputQuality(1.0) // 设置输出质量为 100%
                    .toFile(file);
            System.out.println("输出FIle，图片已成功补白为正方形并保存。"+file.getAbsolutePath());

            // 使用 Thumbnailator 保存处理后的图片，并设置质量为 100%
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Thumbnails.of(squareImage)
                    .size(canvasSize, canvasSize)
                    .outputQuality(1.0) // 设置输出质量为 100%
                    .outputFormat("jpg") // 指定输出格式为 JPG
                    .toOutputStream(byteArrayOutputStream);
            byteArrayOutputStream.close();
            System.out.println("输出字节数组，图片已成功补白为正方形并保存。"+byteArrayOutputStream.toByteArray().length);

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
