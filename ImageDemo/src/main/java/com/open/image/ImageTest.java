package com.open.image;

/**
 * @author cmy
 * @version 1.0
 * @date 2024/5/8 21:33
 * @description
 */
public class ImageTest {
    public static void main(String[] args) {
        OperateImage operateImage = new OperateImage();
        String[] pics={"D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex1.jpg","D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex2.jpg"};
        boolean jpeg = operateImage.joinImageListVertical(pics, "JPEG", "D:\\intellij2020-1\\idea\\JavaPractice\\ImageDemo\\src\\main\\java\\com\\open\\image\\spacex3.jpg");
    }
}
