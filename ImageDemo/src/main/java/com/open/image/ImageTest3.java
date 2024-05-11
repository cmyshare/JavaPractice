package com.open.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2024/5/11 23:36
 * @description 在这基础上，是否能上多张图片放置充分利用空间，比如像俄罗斯方块一样，从左到右横向且充分利用空间，图与图之间还有间距。
 */
public class ImageTest3 {
    private static final int BASE_WIDTH = 6000; // 底图宽度
    private static final int BASE_HEIGHT = 50000; // 底图高度
    private static final int SPACING = 10; // 图片间距

    public static void main(String[] args) {
        // 假设我们有多张图片，你需要根据实际情况修改路径和文件名
        String[] imagePaths = {"D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex1.jpg"
                ,"D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex2.jpg"
                ,"D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex3.jpg",
                "D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex11.jpg",
                "D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex22.jpg",
                "D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex.jpg"}; // 更多图片...

        // 加载图片并获取其宽度和高度
        List<BufferedImageWithSize> images = new ArrayList<>();
        int lastPlacedImageHeight = 0; // 初始化上一张图片的高度为0
        for (String imagePath : imagePaths) {
            try {
                BufferedImage image = ImageIO.read(new File(imagePath));
                int width = image.getWidth();
                int height = image.getHeight();
                images.add(new BufferedImageWithSize(image, width, height));
            } catch (IOException e) {
                e.printStackTrace();
                continue;
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

        for (BufferedImageWithSize imageWithSize : images) {
            // 检查当前行是否有足够的空间
            if (currentX + imageWithSize.width + SPACING > BASE_WIDTH) {
                // 如果没有，移动到下一行的最左侧
                currentX = 0;
                currentY += Math.max(lastPlacedImageHeight, SPACING); // 上一张图片的高度加上间距
                if (currentY + imageWithSize.height > BASE_HEIGHT) {
                    // 如果下一行也放不下，停止放置图片
                    break;
                }
            }

            // 绘制图片到底图上
            g2d.drawImage(imageWithSize.image, currentX, currentY, null);

            // 更新位置和高度信息
            currentX += imageWithSize.width + SPACING;
            lastPlacedImageHeight = imageWithSize.height; // 更新上一张图片的高度

            // 如果这是当前行的最后一张图片（即后面没有图片或者没有足够的空间放置下一张图片），则重置X位置
            if (currentX + SPACING >= BASE_WIDTH || images.indexOf(imageWithSize) == images.size() - 1) {
                currentX = 0; // 重置X位置到起始点
            }
        }

        // 释放Graphics2D资源
        g2d.dispose();

        // 保存合并后的图片
        try {
            ImageIO.write(baseImage, "jpg", new File("D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex5.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 辅助类来存储BufferedImage及其宽度和高度
    static class BufferedImageWithSize {
        public BufferedImage image;
        public int width;
        public int height;

        public BufferedImageWithSize(BufferedImage image, int width, int height) {
            this.image = image;
            this.width = width;
            this.height = height;
        }
    }
}