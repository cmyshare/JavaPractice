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
 * @date 2024/5/12 0:30
 * @description 你的ImageMerger类代码实现了一个简单的图片合并功能，但是它只考虑了水平方向的图片排列，而没有处理垂直方向上的图片换行逻辑。下面我将对代码进行修改，以支持垂直方向上的图片换行：
 */
public class ImageTest4 {

        private static final int BASE_WIDTH = 6000; // 底图宽度
        private static final int BASE_HEIGHT = 10000; // 底图高度
        private static final int SPACING = 10; // 图片间距
        private static final int CURRENT_Y = 0; // 初始垂直位置

        public static void main(String[] args) {
            // 假设我们有多张图片，你需要根据实际情况修改路径和文件名
            String[] imagePaths = {"path/to/image1.jpg", "path/to/image2.jpg", "path/to/image3.jpg"}; // 更多图片...

            // 创建一个新的BufferedImage作为底图
            BufferedImage baseImage = new BufferedImage(BASE_WIDTH, BASE_HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = baseImage.createGraphics();

            // 设置背景色（如果需要）
            g2d.setColor(Color.WHITE); // 例如，设置为白色背景
            g2d.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);

            int currentX = 0; // 当前图片的水平位置
            int currentY = CURRENT_Y; // 当前图片的垂直位置

            List<Integer> usedHeights = new ArrayList<>(); // 记录已使用的高度

            for (String imagePath : imagePaths) {
                try {
                    BufferedImage image = ImageIO.read(new File(imagePath));

                    // 检查图片是否会在底图外
                    if (currentX + image.getWidth() + SPACING > BASE_WIDTH) {
                        // 垂直换行
                        currentX = 0; // 重置水平位置
                        currentY += getNextLineY(usedHeights, SPACING); // 计算新的垂直位置
                    }

                    // 绘制图片到底图上
                    g2d.drawImage(image, currentX, currentY, null);

                    // 更新当前位置以放置下一张图片
                    currentX += image.getWidth() + SPACING;

                    // 记录当前图片使用的高度
                    usedHeights.add(currentY + image.getHeight());

                    // 如果有必要，可以添加额外的逻辑来处理图片在垂直方向上超出底图高度的情况

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }

            // 释放Graphics2D资源
            g2d.dispose();

            // 保存合并后的图片
            try {
                ImageIO.write(baseImage, "jpg", new File("path/to/combined_image.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 找到下一个可用的垂直位置
        private static int getNextLineY(List<Integer> usedHeights, int spacing) {
            int maxY = 0;
            for (int height : usedHeights) {
                maxY = Math.max(maxY, height);
            }
            return maxY + spacing; // 返回当前最大已使用高度加上间距
        }

}
