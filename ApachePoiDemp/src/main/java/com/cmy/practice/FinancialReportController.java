package com.cmy.practice;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2025/11/5 15:44
 * @desc
 */

@RestController
public class FinancialReportController {
    @GetMapping("/export-poi")
    public void exportPoi(HttpServletResponse response) throws Exception {
        List<FinancialRecord> data = Arrays.asList(
                new FinancialRecord("销售部", "差旅费", 5000.0, "高铁+住宿"),
                new FinancialRecord("研发部", "设备采购", 20000.0, "服务器")
        );

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=financial_report_poi.xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("财务月报");

            // 创建样式
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.CENTER);

            // ====== 第一行表头（合并）======
            Row row0 = sheet.createRow(0);
            Cell cell0 = row0.createCell(0);
            cell0.setCellValue("2025年4月财务月报");
            cell0.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

            // ====== 第二行：多级表头 ======
            Row row1 = sheet.createRow(1);
            String[] headers = {"部门", "费用项目", "金额(元)", "备注"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = row1.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ====== 填充数据 ======
            int rowNum = 2;
            for (FinancialRecord record : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getDepartment());
                row.createCell(1).setCellValue(record.getItem());
                row.createCell(2).setCellValue(record.getAmount());
                row.createCell(3).setCellValue(record.getRemark());
                for (int i = 0; i < 4; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            // ====== 插入图片（示例：logo.png 放在 resources/static 下）======
            // 读取图片资源
            ClassPathResource resource = new ClassPathResource("static/logo.png");
            byte[] pictureBytes = StreamUtils.copyToByteArray(resource.getInputStream());

            int pictureIdx = workbook.addPicture(pictureBytes, Workbook.PICTURE_TYPE_PNG);
            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();

            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(0); anchor.setRow1(0);
            anchor.setCol2(1); anchor.setRow2(1);
            drawing.createPicture(anchor, pictureIdx);

            // 自动列宽
            for (int i = 0; i < 4; i++) sheet.autoSizeColumn(i);

            workbook.write(response.getOutputStream());
        }
    }

    private CellStyle createHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}
