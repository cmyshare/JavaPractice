package com.open.office;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.FilePictureRenderData;
import com.deepoove.poi.data.Pictures;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cmy
 * @version 1.0
 * @date 2024/5/26 17:45
 * @description Word解析工具类
 *
 * poi-tl（poi template language）是Word模板引擎，使用模板和数据创建很棒的Word文档。
 * Documents4j是一个开源的Java库，用于在Java应用程序中进行Microsoft Office文档（如Word、Excel、PowerPoint等）的转换。
 * 参考地址：
 * https://deepoove.com/poi-tl/#_why_poi_tl
 * https://blog.csdn.net/chuihou8980/article/details/100757674
 * https://blog.csdn.net/wlyang666/article/details/130246576
 * https://blog.lovelu.top/p/11f7.html#
 * https://blog.csdn.net/q2qwert/article/details/131592947
 * https://blog.csdn.net/AnnieRabbit/article/details/133806099
 */
public class WordTest {

    public static void main(String[] args) {
        try {
            Map<String, Object> map = new HashMap<>();
            //文本填充
            map.put("JIA_FANG", "甲方名称");
            map.put("YI_FANG", "乙方名称");
            map.put("HE_TONG_CONTENT", "身份证");
            // 图片填充，读取本地磁盘图片
            map.put("JIA_SIGN", new FilePictureRenderData(100, 100, "F:\\桌面\\Word工具类\\微信图片_20240526015250.jpg"));
            // 图片填充，通过url读取网络图片。推荐使用工厂Pictures构建图片模型
            map.put("YI_SIGN", Pictures.ofUrl("https://static.cmyshare.cn/SpaceX/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20240526015240.jpg").size(100, 100).create());
            //读取本地word模版
            //File file = new File("F:\\桌面\\Word工具类\\WordTest.docx");
            //读取网络word模版
            URL url = new URL("https://static.cmyshare.cn/OfficeDemo/WordTest.docx");
            //使用HttpURLConnection、URLConnection、URL获取getInputStream
            //URLConnection connection = url.openConnection();
            //InputStream inputStream = connection.getInputStream();
            //开启模版、填充字段
            XWPFTemplate template = XWPFTemplate.compile(url.openStream()).render(map);
            //读取目标word文件
            FileOutputStream out = new FileOutputStream(new File("F:\\桌面\\Word工具类\\WordTestOut2.docx"));
            //输出合并后的word
            template.write(out);
            //释放资源
            out.flush();
            //关闭输出流
            out.close();
            //关闭模板
            template.close();

            WordToPdfUrl("F:\\桌面\\Word工具类\\WordTestOut2.docx","F:\\桌面\\Word工具类\\PdfTestOut2.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * WORD转成PDF
     * @param wordUrl word地址
     * @param pdfUrl pdf地址
     * @throws IOException
     */
    private static void WordToPdfPath(String wordUrl, String pdfUrl) throws IOException {
        // 声明两个流变量，docxInputStream用于读取Word文档，outputStream用于写入PDF文件
        InputStream docxInputStream = null;
        OutputStream outputStream = null;
        // 声明一个IConverter接口类型的变量converter，用于执行文档转换操作
        IConverter converter=null;
        try {
            // 使用FileInputStream打开Word文档文件，并赋值给docxInputStream
            docxInputStream = new FileInputStream(new File(wordUrl));
            // 使用FileOutputStream打开（或创建）PDF文件，并赋值给outputStream
            outputStream = new FileOutputStream(new File(pdfUrl));
            // 创建一个LocalConverter的实例，用于执行文档的转换
            converter = LocalConverter.builder().build();
            // 调用converter的convert方法开始转换操作
            // 将docxInputStream作为输入，outputStream作为输出，转换为PDF格式
            converter.convert(docxInputStream)
                    .as(DocumentType.DOCX)
                    .to(outputStream)
                    .as(DocumentType.PDF)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 尝试关闭outputStream
            outputStream.close();
            // 尝试关闭docxInputStream
            docxInputStream.close();
            // 尝试停止converter（如果它不为null）
            converter.kill();
        }
    }

    /**
     * WORD转成PDF
     * @param wordPath word地址
     * @param pdfPath pdf地址
     * @throws IOException
     */
    private static void WordToPdfUrl(String wordPath, String pdfPath) throws IOException {
        // 声明两个流变量，docxInputStream用于读取Word文档，outputStream用于写入PDF文件
        InputStream docxInputStream = null;
        OutputStream outputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 声明一个IConverter接口类型的变量converter，用于执行文档转换操作
        IConverter converter=null;
        try {
            // 使用FileInputStream打开Word文档文件，并赋值给docxInputStream
            docxInputStream = new FileInputStream(new File(wordPath));
            // 使用FileOutputStream打开（或创建）PDF文件，并赋值给outputStream
            outputStream = new FileOutputStream(new File(pdfPath));
            // 创建一个LocalConverter的实例，用于执行文档的转换
            converter = LocalConverter.builder().build();
            // 调用converter的convert方法开始转换操作
            // 将docxInputStream作为输入，outputStream作为输出，转换为PDF格式
            converter.convert(docxInputStream)
                    .as(DocumentType.DOCX)
                    .to(byteArrayOutputStream)
                    .as(DocumentType.PDF)
                    .execute();
            outputStream.write(byteArrayOutputStream.toByteArray());
            //todo 上传文件服务器，返回文件地址，进行数据库存储即可。
            System.out.println("pdf bytes:"+byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 尝试关闭outputStream
            outputStream.close();
            byteArrayOutputStream.close();
            // 尝试关闭docxInputStream
            docxInputStream.close();
            // 尝试停止converter（如果它不为null）
            converter.kill();
        }
    }
}
