package com.cmy.practice;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 财务月报导出控制器（带三级复杂表头）
 *
 * 功能说明：
 * - 生成一个包含三级表头的 Excel 报表（主标题 + 二级分组 + 三级明细）
 * - 支持中文文件名下载（兼容主流浏览器）
 * - 自动合并单元格（如“部门”跨两行、“费用明细”跨两列等）
 * - 插入 logo 图片（可选）
 * - 应用统一的样式（标题、表头、数据行）
 *
 * @author cmy
 * @version 1.0
 * @since 2025/11/5
 */
@RestController
public class ComplexFinancialReportController {

    /**
     * 导出带三级表头的财务月报 Excel 文件
     *
     * <p>接口路径：GET /export-complex-report</p>
     * <p>返回：application/vnd.openxmlformats-officedocument.spreadsheetml.sheet 格式的二进制流</p>
     *
     * @param response HttpServletResponse 对象，用于写入 Excel 内容并设置下载头
     * @throws Exception 所有可能的异常（如 IO、编码、POI 操作失败等）
     */
    @GetMapping("/export-complex-report")
    public void exportComplexReport(HttpServletResponse response) throws Exception {
        // 模拟财务数据（实际项目中应从数据库查询）
        List<FinancialRecord> data = Arrays.asList(
                new FinancialRecord("销售部", "差旅费", 5000.0, "已通过", "高铁+住宿"),
                new FinancialRecord("研发部", "设备采购", 20000.0, "待审核", "服务器")
        );

        // 设置下载文件名（含中文）
        String fileName = "财务月报_三级表头.xlsx";

        // 设置响应内容类型为 Excel（.xlsx）
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        // ✅ 关键：正确设置 Content-Disposition 以支持中文文件名
        // 使用 RFC 6266 标准：filename*=UTF-8''{encodedName}
        try {
            // 将中文文件名按 UTF-8 编码为 URL 安全格式（如 %E8%B4%A2%E5%8A%A1...）
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            // 构造符合标准的 Content-Disposition 头
            String contentDisposition = "attachment; filename*=UTF-8''" + encodedFileName;
            response.setHeader("Content-Disposition", contentDisposition);
        } catch (UnsupportedEncodingException e) {
            // 理论上不会触发（UTF-8 是标准字符集），但保留降级方案
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        }

        // todo 创建 Excel 工作簿（使用 try-with-resources 自动关闭资源）
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("财务月报");

            // ====== todo 1. 创建三种单元格样式 ======
            CellStyle titleStyle = createTitleStyle(workbook);   // 主标题样式（大号加粗居中）
            CellStyle headerStyle = createHeaderStyle(workbook); // 表头样式（白字蓝底边框）
            CellStyle dataStyle = createDataStyle(workbook);     // 数据行样式（带边框居中）

            // ====== todo 2. 第1行：主标题（A1:E1 合并）======
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("2025年4月财务月报");
            titleCell.setCellStyle(titleStyle);
            // 合并 A1 到 E1（共5列），形成横跨整表的主标题
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4)); // (起始行, 结束行, 起始列, 结束列)

            // ====== todo 3. 第2行：二级分组标题 ======
            Row secondHeaderRow = sheet.createRow(1);

            // A2: "部门" —— 将与下方 A3 合并，作为左侧独立列标题
            Cell deptHeader = secondHeaderRow.createCell(0);
            deptHeader.setCellValue("部门");
            deptHeader.setCellStyle(headerStyle);

            // B2: "费用明细" —— 跨 B2 和 C2 两列
            Cell costHeader = secondHeaderRow.createCell(1);
            costHeader.setCellValue("费用明细");
            costHeader.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 2)); // B2:C2

            // D2: "审核信息" —— 跨 D2 和 E2 两列
            Cell auditHeader = secondHeaderRow.createCell(3);
            auditHeader.setCellValue("审核信息");
            auditHeader.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 4)); // D2:E2

            // ====== todo 4. 第3行：三级明细表头（仅 B3 到 E3）======
            Row thirdHeaderRow = sheet.createRow(2);
            // 注意：A3 不创建内容！因为 A2:A3 已合并，重复写入会导致 Excel 损坏或显示异常
            thirdHeaderRow.createCell(1).setCellValue("项目");
            thirdHeaderRow.createCell(2).setCellValue("金额(元)");
            thirdHeaderRow.createCell(3).setCellValue("状态");
            thirdHeaderRow.createCell(4).setCellValue("备注");

            // 为 B3~E3 设置表头样式
            for (int i = 1; i <= 4; i++) {
                thirdHeaderRow.getCell(i).setCellStyle(headerStyle);
            }

            // ====== todo 5. 合并“部门”单元格（A2:A3）======
            // 使“部门”标题纵向跨两行，视觉上更清晰
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0)); // A2:A3

            // ====== todo 6. 填充业务数据（从第4行开始，即 rowIndex=3）======
            int dataStartRow = 3;
            for (int i = 0; i < data.size(); i++) {
                //注意程序中行是从0开始的，所以第4行的索引是3
                FinancialRecord record = data.get(i);
                Row dataRow = sheet.createRow(dataStartRow + i);

                // 按列填充数据
                dataRow.createCell(0).setCellValue(record.getDepartment()); // A列：部门
                dataRow.createCell(1).setCellValue(record.getItem());       // B列：项目
                dataRow.createCell(2).setCellValue(record.getAmount());     // C列：金额
                dataRow.createCell(3).setCellValue(record.getStatus());    // D列：状态
                dataRow.createCell(4).setCellValue(record.getRemark());    // E列：备注

                // 为整行应用数据样式（居中 + 边框）
                for (int col = 0; col <= 4; col++) {
                    dataRow.getCell(col).setCellStyle(dataStyle);
                }
            }

            // ====== 7. 插入 logo 图片（可选功能）======
            // 图片路径：src/main/resources/static/logo.png
            try {
                ClassPathResource imgResource = new ClassPathResource("static/logo.png");
                if (imgResource.exists()) {
                    byte[] pictureBytes = StreamUtils.copyToByteArray(imgResource.getInputStream());
                    // 将图片注册到工作簿，返回图片索引
                    int pictureIdx = workbook.addPicture(pictureBytes, Workbook.PICTURE_TYPE_PNG);
                    // 创建绘图对象
                    CreationHelper helper = workbook.getCreationHelper();
                    Drawing<?> drawing = sheet.createDrawingPatriarch();
                    // 设置图片锚点（左上角在 A1，右下角在 B2）
                    ClientAnchor anchor = helper.createClientAnchor();
                    anchor.setCol1(0); // 起始列（A）
                    anchor.setRow1(0); // 起始行（第1行）
                    anchor.setCol2(1); // 结束列（B）
                    anchor.setRow2(1); // 结束行（第2行）
                    drawing.createPicture(anchor, pictureIdx);
                }
            } catch (Exception e) {
                // 图片缺失不应中断主流程，仅记录警告
                System.err.println("警告：logo.png 未找到，跳过图片插入。");
            }

            // ====== 8. 自动调整列宽（提升可读性）======
            // 防止列宽溢出（Excel 最大列宽为 255 字符单位，每单位 = 1/256 字符宽度）
            for (int i = 0; i < 5; i++) {
                sheet.setColumnWidth(i, Math.min(sheet.getColumnWidth(i) * 2, 255 * 256));
            }

            // ====== 9. 输出 Excel 到 HTTP 响应流 ======
            workbook.write(response.getOutputStream());
        }
        // try-with-resources 自动调用 workbook.close()
    }

    /**
     * 创建主标题样式（用于“2025年4月财务月报”）
     *
     * 特点：
     * - 字体大小 16pt
     * - 加粗
     * - 水平 & 垂直居中
     *
     * @param wb 当前工作簿（用于创建字体和样式）
     * @return 配置好的 CellStyle
     */
    private CellStyle createTitleStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 创建表头样式（用于所有表头单元格）
     *
     * 特点：
     * - 白色加粗字体
     * - 深蓝色背景
     * - 实心填充
     * - 四周细边框
     * - 居中对齐
     *
     * @param wb 当前工作簿
     * @return 配置好的 CellStyle
     */
    private CellStyle createHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex()); // 白色文字
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 添加边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /**
     * 创建数据行样式（用于所有数据单元格）
     *
     * 特点：
     * - 居中对齐
     * - 四周细边框
     *
     * @param wb 当前工作簿
     * @return 配置好的 CellStyle
     */
    private CellStyle createDataStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}