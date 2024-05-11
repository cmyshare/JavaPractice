package com.open.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2024/5/11 23:36
 * @description 在这基础上，在底图上从左到右横向放图片，当底图无法容纳所有图片时就停止放图片，保存合并后的图片。
 */
public class ImageTest2 {

    ////方案1 只放入一行
    //private static final int BASE_WIDTH = 6000; // 底图宽度
    //private static final int BASE_HEIGHT = 50000; // 底图高度
    //private static final int SPACING = 10; // 图片间距
    //
    //public static void main(String[] args) {
    //    // 假设我们有多张图片，你需要根据实际情况修改路径和文件名
    //    String[] imagePaths = {"D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex1.jpg"
    //            ,"D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex2.jpg"
    //            ,"D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex3.jpg",
    //            "D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex11.jpg",
    //            "D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex22.jpg",
    //            "D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex.jpg"}; // 更多图片...
    //
    //    // 创建一个新的BufferedImage作为底图
    //    BufferedImage baseImage = new BufferedImage(BASE_WIDTH, BASE_HEIGHT, BufferedImage.TYPE_INT_RGB);
    //    Graphics2D g2d = baseImage.createGraphics();
    //
    //    // 设置背景色（如果需要）
    //    g2d.setColor(Color.WHITE); // 例如，设置为白色背景
    //    g2d.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);
    //
    //    int currentX = 0; // 当前图片的水平位置
    //
    //    for (String imagePath : imagePaths) {
    //        try {
    //            BufferedImage image = ImageIO.read(new File(imagePath));
    //            System.out.println("width"+image.getWidth()+" height"+image.getHeight());
    //
    //            // 检查图片是否会在底图外
    //            if (currentX + image.getWidth() + SPACING > BASE_WIDTH) {
    //                // 停止放置图片
    //                break;
    //            }
    //
    //            // 绘制图片到底图上
    //            g2d.drawImage(image, currentX, 0, null); // 假设图片从顶部开始放置
    //
    //            // 更新当前位置以放置下一张图片
    //            currentX += image.getWidth() + SPACING;
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //            break;
    //        }
    //    }
    //
    //    // 释放Graphics2D资源
    //    g2d.dispose();
    //
    //    // 保存合并后的图片
    //    try {
    //        ImageIO.write(baseImage, "jpg", new File("D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex4.jpg"));
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    //}

    //方案2，在底图范围内，从左到右横向放入多张图片
    private static final int BASE_WIDTH = 6000; // 底图宽度
    private static final int BASE_HEIGHT = 10000; // 底图高度，根据需要设置
    private static final int SPACING = 50; // 图片间距

    public static void main(String[] args) {
            // 假设我们有多张图片，你需要根据实际情况修改路径和文件名
            String[] imagePaths = {"D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex1.jpg"
                    ,"D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex2.jpg"
                    ,"D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex3.jpg",
                    "D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex11.jpg",
                    "D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex22.jpg",
                    "D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex.jpg"}; // 更多图片...

        List<BufferedImage> images = new ArrayList<>();
        // 加载图片列表
        for (String imagePath : imagePaths) {
            try {
                images.add(ImageIO.read(new File(imagePath)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 创建一个新的BufferedImage作为底图
        BufferedImage baseImage = new BufferedImage(BASE_WIDTH, BASE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = baseImage.createGraphics();

        // 设置背景色（如果需要）
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);

        int currentX = 0; // 当前图片的水平位置
        int currentY = 0; // 当前图片的垂直位置
        int maxHeight = 0; // 记录已经放置的图片的最大高度

        for (BufferedImage image : images) {
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            // 检查是否还有足够的空间放置当前图片（包括间距）
            if (currentX + imageWidth + SPACING > BASE_WIDTH) {
                // 如果没有足够的水平空间，则移动到下一行
                currentX = 0; // 重置水平位置
                currentY += maxHeight + SPACING; // 垂直位置下移（包括间距）
                maxHeight = 0; // 重置最大高度
            }

            // 绘制图片
            g2d.drawImage(image, currentX, currentY, null);

            // 更新当前位置和最大高度
            currentX += imageWidth + SPACING;
            maxHeight = Math.max(maxHeight, imageHeight);
        }

        // 释放Graphics2D资源
        g2d.dispose();

        // 保存合并后的图片
        try {
            ImageIO.write(baseImage, "jpg", new File("D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex6.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
