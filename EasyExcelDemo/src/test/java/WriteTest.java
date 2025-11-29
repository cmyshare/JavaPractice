import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.*;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.open.easyexcel.model.*;
import com.open.easyexcel.utils.TestFileUtil;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/4/29 0029 15:33
 * @description EasyExcel读的常见写法
 */

@Ignore
@Slf4j
public class WriteTest {
    /**
     * 通用数据生成 后面不会重复写
     * @return
     */
    private List<DemoData> data() {
        List<DemoData> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    /**
     * 最简单的写
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 直接写即可
     */
    @Test
    public void simpleWrite() {
        //// 写法1 JDK8+
        //// since: 3.0.0-beta1
        //System.out.println(TestFileUtil.getPath()); //xlsx文件在 F:/IDEA2022/workmenu/easyExcelDemo/target/test-classes/
        //String fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        //// 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        //// 如果这里想使用03 则 传入excelType参数即可
        //EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(() -> {
        //            // 分页查询数据
        //            return data();
        //        });

        //// 写法2
        //// 存储地址TestFileUtil.getPath()、excel名称"simpleWrite" + System.currentTimeMillis() + ".xlsx"
        //String fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        //// 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        //// 如果这里想使用03 则 传入excelType参数即可
        //EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(data());

        // 写法3
        String fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = null;
        try {
            //构建excel文件写入流
            excelWriter = EasyExcel.write(fileName, DemoData.class).build();
            //构建excel表
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            //使用写入流
            excelWriter.write(data(), writeSheet);
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 根据参数只导出指定列
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 根据自己或者排除自己需要的列
     * <p>3. 直接写即可
     */
    @Test
    public void excludeOrIncludeWrite() {
        String fileName = TestFileUtil.getPath() + "excludeOrIncludeWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里需要注意 在使用ExcelProperty注解的使用，如果想不空列则需要加入order字段，而不是index,order会忽略空列，然后继续往后，而index，不会忽略空列，在第几列就是第几列。
        // 根据用户传入字段 假设我们要忽略exclude date
        Set<String> excludeColumnFiledNames = new HashSet<String>();
        excludeColumnFiledNames.add("date");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoData.class).excludeColumnFiledNames(excludeColumnFiledNames).sheet("模板")
                .doWrite(data());

        fileName = TestFileUtil.getPath() + "excludeOrIncludeWrite" + System.currentTimeMillis() + ".xlsx";
        // 根据用户传入字段 假设我们只要导出include date
        Set<String> includeColumnFiledNames = new HashSet<String>();
        includeColumnFiledNames.add("date");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoData.class).includeColumnFiledNames(includeColumnFiledNames).sheet("模板")
                .doWrite(data());
    }

    /**
     * 指定写入的列
     * <p>1. 创建excel对应的实体对象 参照{@link IndexData}
     * <p>2. 使用{@link ExcelProperty}注解指定写入的列
     * <p>3. 直接写即可
     */
    @Test
    public void indexWrite() {
        String fileName = TestFileUtil.getPath() + "indexWrite" + System.currentTimeMillis() + ".xlsx";
        //这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭 data()为数据源
        //write方法两个参数：第一个参数文件路径名称，第二个参数实体类class、sheet为表名、doWrite为写入数据
        EasyExcel.write(fileName, IndexData.class).sheet("模板").doWrite(data());
    }

    /**
     * 复杂头写入
     * <p>1. 创建excel对应的实体对象 参照{@link ComplexHeadData}
     * <p>2. 使用{@link ExcelProperty}注解指定复杂的头
     * <p>3. 直接写即可
     */
    @Test
    public void complexHeadWrite() {
        String fileName = TestFileUtil.getPath() + "complexHeadWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, ComplexHeadData.class).sheet("模板").doWrite(data());
    }

    /**
     * 重复多次写入
     * <p>1. 创建excel对应的实体对象 参照{@link ComplexHeadData}
     * <p>2. 使用{@link ExcelProperty}注解指定复杂的头
     * <p>3. 直接调用二次写入即可
     */
    @Test
    public void repeatedWrite() {
        // 方法1 如果写到同一个sheet
        String fileName = TestFileUtil.getPath() + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = null;
        //try {
        //    // 这里 需要指定写用哪个class去写
        //    excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        //    // 这里注意 如果同一个sheet只要创建一次
        //    WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        //    // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
        //    for (int i = 0; i < 5; i++) {
        //        // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
        //        List<DemoData> data = data();
        //        excelWriter.write(data, writeSheet);
        //    }
        //} finally {
        //    // 千万别忘记finish 会帮忙关闭流
        //    if (excelWriter != null) {
        //        excelWriter.finish();
        //    }
        //}

        //// 方法2 如果写到不同的sheet 同一个对象
        //fileName = TestFileUtil.getPath() + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        //try {
        //    // 这里指定文件
        //    excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        //    // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
        //    for (int i = 0; i < 5; i++) {
        //        // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
        //        WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + i).build();
        //        // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
        //        List<DemoData> data = data();
        //        excelWriter.write(data, writeSheet);
        //    }
        //} finally {
        //    // 千万别忘记finish 会帮忙关闭流
        //    if (excelWriter != null) {
        //        excelWriter.finish();
        //    }
        //}

        // 方法3 如果写到不同的sheet 不同的对象
        fileName = TestFileUtil.getPath() + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        try {
            // 这里 指定文件
            excelWriter = EasyExcel.write(fileName).build();
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
            for (int i = 0; i < 5; i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class 实际上可以一直变
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(DemoData.class).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<DemoData> data = data();
                excelWriter.write(data, writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 日期、数字或者自定义格式转换
     * <p>1. 创建excel对应的实体对象 参照{@link ConverterData}
     * <p>2. 使用{@link ExcelProperty}配合使用注解{@link DateTimeFormat}、{@link NumberFormat}或者自定义注解
     * <p>3. 直接写即可
     */
    @Test
    public void converterWrite() {
        String fileName = TestFileUtil.getPath() + "converterWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, ConverterData.class).sheet("模板").doWrite(data());
    }

    /**
     * 图片导出
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link ImageDemoData}
     * <p>
     * 2. 直接写即可
     */
    @Test
    public void imageWrite() throws Exception {
        //存储地址
        String fileName = TestFileUtil.getPath() + "imageWrite" + System.currentTimeMillis() + ".xlsx";
        //图片地址
        String imagePath = TestFileUtil.getPath() + "converter" + File.separator + "img.png";
        //定义输入流读取数据
        try (InputStream inputStream = FileUtils.openInputStream(new File(imagePath))) {
            // 图片列表
            List<ImageDemoData> list =  ListUtils.newArrayList();
            // 定义图片数据
            ImageDemoData imageDemoData = new ImageDemoData();
            // 加入列表
            list.add(imageDemoData);

            // 放入五种类型的图片 实际使用只要选一种即可
            imageDemoData.setByteArray(FileUtils.readFileToByteArray(new File(imagePath)));
            imageDemoData.setFile(new File(imagePath));
            imageDemoData.setString(imagePath);
            imageDemoData.setInputStream(inputStream);
            imageDemoData.setUrl(new URL(
                    "http://192.168.0.123:8848/nacos/img/logo-2000-390.svg"));

            //// 这里演示
            //// 需要额外放入文字
            //// 而且需要放入2个图片
            //// 第一个图片靠左
            //// 第二个靠右 而且要额外的占用他后面的单元格
            //WriteCellData<Void> writeCellData = new WriteCellData<>();
            //imageDemoData.setWriteCellDataFile(writeCellData);
            //// 这里可以设置为 EMPTY 则代表不需要其他数据了
            //writeCellData.setType(CellDataTypeEnum.STRING);
            //writeCellData.setStringValue("额外的放一些文字");
            //
            //// 可以放入多个图片
            //List<ImageData> imageDataList = new ArrayList<>();
            //ImageData imageData = new ImageData();
            //imageDataList.add(imageData);
            //writeCellData.setImageDataList(imageDataList);
            //// 放入2进制图片
            //imageData.setImage(FileUtils.readFileToByteArray(new File(imagePath)));
            //// 图片类型
            //imageData.setImageType(ImageData.ImageType.PICTURE_TYPE_PNG);
            //// 上 右 下 左 需要留空
            //// 这个类似于 css 的 margin
            //// 这里实测 不能设置太大 超过单元格原始大小后 打开会提示修复。暂时未找到很好的解法。
            //imageData.setTop(5);
            //imageData.setRight(40);
            //imageData.setBottom(5);
            //imageData.setLeft(5);
            //
            //// 放入第二个图片
            //imageData = new ImageData();
            //imageDataList.add(imageData);
            //writeCellData.setImageDataList(imageDataList);
            //imageData.setImage(FileUtils.readFileToByteArray(new File(imagePath)));
            //imageData.setImageType(ImageData.ImageType.PICTURE_TYPE_PNG);
            //imageData.setTop(5);
            //imageData.setRight(5);
            //imageData.setBottom(5);
            //imageData.setLeft(50);
            //// 设置图片的位置 假设 现在目标 是 覆盖 当前单元格 和当前单元格右边的单元格
            //// 起点相对于当前单元格为0 当然可以不写
            //imageData.setRelativeFirstRowIndex(0);
            //imageData.setRelativeFirstColumnIndex(0);
            //imageData.setRelativeLastRowIndex(0);
            //// 前面3个可以不写  下面这个需要写 也就是 结尾 需要相对当前单元格 往右移动一格
            //// 也就是说 这个图片会覆盖当前单元格和 后面的那一格
            //imageData.setRelativeLastColumnIndex(1);

            // 写入数据
            EasyExcel.write(fileName, ImageDemoData.class).sheet().doWrite(list);
        }
    }

    /**
     * 超链接、备注、公式、指定单个单元格的样式、单个单元格多种样式
     * <p>1. 创建excel对应的实体对象 参照{@link WriteCellDemoData}
     * <p>2. 直接写即可
     */
    @Test
    public void writeCellDataWrite() {
        String fileName = TestFileUtil.getPath() + "writeCellDataWrite" + System.currentTimeMillis() + ".xlsx";
        // 样式数据实体对象
        WriteCellDemoData writeCellDemoData = new WriteCellDemoData();

        // 设置超链接 WriteCellData单元格对象
        WriteCellData<String> hyperlink = new WriteCellData<>("官方网站");
        //超链接加入单元格
        writeCellDemoData.setHyperlink(hyperlink);
        //定义超链接数据对象
        HyperlinkData hyperlinkData = new HyperlinkData();
        //加入超链接hyperlink
        hyperlink.setHyperlinkData(hyperlinkData);
        //设置地址
        hyperlinkData.setAddress("https://github.com/alibaba/easyexcel");
        //设置类型
        hyperlinkData.setHyperlinkType(HyperlinkData.HyperlinkType.URL);

        // 设置备注 WriteCellData单元格对象
        WriteCellData<String> comment = new WriteCellData<>("备注的单元格信息");
        //备注加入单元格
        writeCellDemoData.setCommentData(comment);
        //定义备注数据对象
        CommentData commentData = new CommentData();
        //加入备注数据
        comment.setCommentData(commentData);
        //设置作者
        commentData.setAuthor("Jiaju Zhuang");
        //文本备注提示内容
        commentData.setRichTextStringData(new RichTextStringData("这是一个备注"));
        // 备注的默认大小是按照单元格的大小 这里想调整到4个单元格那么大 所以向后 向下 各额外占用了一个单元格
        commentData.setRelativeLastColumnIndex(1);
        commentData.setRelativeLastRowIndex(1);

        // 设置公式 WriteCellData单元格对象
        WriteCellData<String> formula = new WriteCellData<>();
        //公式加入单元格
        writeCellDemoData.setFormulaData(formula);
        //定义公式数据对象
        FormulaData formulaData = new FormulaData();
        //加入公式数据
        formula.setFormulaData(formulaData);
        // 将 123456789 中的第一个数字替换成 2
        // 这里只是例子 如果真的涉及到公式 能内存算好尽量内存算好 公式能不用尽量不用
        formulaData.setFormulaValue("REPLACE(123456789,1,1,2)");

        // 设置单个单元格的样式 当然样式 很多的话 也可以用注解等方式。
        WriteCellData<String> writeCellStyle = new WriteCellData<>("单元格样式");
        //设置样式类型
        writeCellStyle.setType(CellDataTypeEnum.STRING);
        //加入样式类型
        writeCellDemoData.setWriteCellStyle(writeCellStyle);
        //定义样式类型数据
        WriteCellStyle writeCellStyleData = new WriteCellStyle();
        //加入样式类型数据
        writeCellStyle.setWriteCellStyle(writeCellStyleData);
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.
        writeCellStyleData.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
        writeCellStyleData.setFillForegroundColor(IndexedColors.GREEN.getIndex());

        // 设置单个单元格多种样式
        WriteCellData<String> richTest = new WriteCellData<>();
        //设置样式类型
        richTest.setType(CellDataTypeEnum.RICH_TEXT_STRING);
        //加入样式类型
        writeCellDemoData.setRichText(richTest);
        //定义样式类型数据
        RichTextStringData richTextStringData = new RichTextStringData();
        //加入样式类型数据
        richTest.setRichTextStringDataValue(richTextStringData);
        //设置样式内容说明
        richTextStringData.setTextString("红色绿色默认");
        // 前2个字红色 设置内容字体
        WriteFont writeFont = new WriteFont();
        writeFont.setColor(IndexedColors.RED.getIndex());
        richTextStringData.applyFont(0, 2, writeFont);
        // 接下来2个字绿色
        writeFont = new WriteFont();
        writeFont.setColor(IndexedColors.GREEN.getIndex());
        richTextStringData.applyFont(2, 4, writeFont);

        //定义样式类型数据对象列表
        List<WriteCellDemoData> data = new ArrayList<>();
        data.add(writeCellDemoData);
        //写入数据
        EasyExcel.write(fileName, WriteCellDemoData.class).inMemory(true).sheet("模板").doWrite(data);
    }

    /**
     * 根据模板写入
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 使用{@link ExcelProperty}注解指定写入的列
     * <p>3. 使用withTemplate 写取模板
     * <p>4. 直接写即可
     */
    @Test
    public void templateWrite() {
        // 模板
        String templateFileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
        // 写入目标文件名
        String fileName = TestFileUtil.getPath() + "templateWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoData.class).withTemplate(templateFileName).sheet().doWrite(data());
    }

    /**
     * 列宽、行高
     * <p>1. 创建excel对应的实体对象 参照{@link WidthAndHeightData}
     * <p>2. 使用注解{@link ColumnWidth}、{@link HeadRowHeight}、{@link ContentRowHeight}指定宽度或高度
     * <p>3. 直接写即可
     */
    @Test
    public void widthAndHeightWrite() {
        String fileName = TestFileUtil.getPath() + "widthAndHeightWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, WidthAndHeightData.class).sheet("模板").doWrite(data());
    }

    /**
     * 动态头，实时生成头写入
     * <p>
     * 思路是这样子的，先创建List<String>头格式的sheet仅仅写入头,然后通过table 不写入头的方式 去写入数据
     *
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 然后写入table即可
     */
    @Test
    public void dynamicHeadWrite() {
        String fileName = TestFileUtil.getPath() + "dynamicHeadWrite" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName)
                // 这里放入动态头
                .head(head()).sheet("动态头模板")
                // 当然这里数据也可以用 List<List<String>> 去传入
                .doWrite(data());
    }

    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = new ArrayList<String>();
        head0.add("主体");
        head0.add("字符串" + System.currentTimeMillis());
        List<String> head1 = new ArrayList<String>();
        head1.add("主体");
        head1.add("数字" + System.currentTimeMillis());
        List<String> head2 = new ArrayList<String>();
        head2.add("主体");
        head2.add("日期" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }


}