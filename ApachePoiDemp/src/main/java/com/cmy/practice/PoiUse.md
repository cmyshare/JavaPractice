在日常使用 Apache POI（尤其是处理 Excel 文件）时，以下 **核心类与接口** 是 **最高频、最常用** 的。它们覆盖了 90% 以上的 Excel 读写、样式、格式、合并、图片等需求。

参考地址：
https://www.w3ccoo.com/apache_poi/apache_poi_core_classes.html

---

## ✅ 一、高频核心类与接口总览（按功能分类）

### 1. **工作簿（Workbook）**
| 类/接口 | 说明 | 使用频率 |
|--------|------|--------|
| `org.apache.poi.ss.usermodel.Workbook` | **通用工作簿接口**（HSSF/XSSF/SXSSF 公共父类） | ⭐⭐⭐⭐⭐ |
| `org.apache.poi.xssf.usermodel.XSSFWorkbook` | `.xlsx` 格式工作簿（基于 XML） | ⭐⭐⭐⭐⭐ |
| `org.apache.poi.hssf.usermodel.HSSFWorkbook` | `.xls` 格式工作簿（旧版二进制） | ⭐⭐（逐渐淘汰） |
| `org.apache.poi.xssf.streaming.SXSSFWorkbook` | **流式写入 `.xlsx`**，适合大数据量（>10万行） | ⭐⭐⭐⭐ |

> ✅ **建议**：新项目一律用 `XSSFWorkbook` 或 `SXSSFWorkbook`。

---

### 2. **工作表（Sheet）**
| 类/接口 | 说明 | 使用频率 |
|--------|------|--------|
| `org.apache.poi.ss.usermodel.Sheet` | 工作表接口 | ⭐⭐⭐⭐⭐ |
| 方法：`createRow(int)`, `getRow(int)`, `addMergedRegion(...)`, `setColumnWidth(...)` | 常用操作 | ⭐⭐⭐⭐⭐ |

---

### 3. **行与单元格（Row & Cell）**
| 类/接口 | 说明 | 使用频率 |
|--------|------|--------|
| `org.apache.poi.ss.usermodel.Row` | 行对象 | ⭐⭐⭐⭐⭐ |
| `org.apache.poi.ss.usermodel.Cell` | 单元格对象 | ⭐⭐⭐⭐⭐ |
| 方法：`createCell(int)`, `setCellValue(...)`, `getCell(int)` | 核心数据写入/读取 | ⭐⭐⭐⭐⭐ |

> ✅ `setCellValue()` 支持：
> - `String`
> - `double` / `Double`
> - `boolean`
> - `LocalDateTime`（需配合 `CellStyle` 设置日期格式）

---

### 4. **单元格样式（CellStyle）**
| 类/接口 | 说明 | 使用频率 |
|--------|------|--------|
| `org.apache.poi.ss.usermodel.CellStyle` | 单元格样式（对齐、边框、填充、字体等） | ⭐⭐⭐⭐⭐ |
| `org.apache.poi.ss.usermodel.Font` | 字体设置 | ⭐⭐⭐⭐ |
| `org.apache.poi.ss.usermodel.HorizontalAlignment` | 水平对齐（`LEFT`, `CENTER`, `RIGHT`） | ⭐⭐⭐⭐ |
| `org.apache.poi.ss.usermodel.VerticalAlignment` | 垂直对齐（`TOP`, `CENTER`, `BOTTOM`） | ⭐⭐⭐ |
| `org.apache.poi.ss.usermodel.BorderStyle` | 边框样式（`THIN`, `MEDIUM`, `THICK`） | ⭐⭐⭐⭐ |
| `org.apache.poi.ss.usermodel.FillPatternType` | 填充模式（`SOLID_FOREGROUND` 最常用） | ⭐⭐⭐ |
| `org.apache.poi.ss.usermodel.IndexedColors` | 预定义颜色（如 `WHITE`, `RED`, `DARK_BLUE`） | ⭐⭐⭐⭐ |

> 💡 **注意**：样式对象应**复用**，不要在循环中频繁创建！

---

### 5. **单元格合并**
| 类 | 说明 | 使用频率 |
|-----|------|--------|
| `org.apache.poi.ss.util.CellRangeAddress` | 表示合并区域（`firstRow, lastRow, firstCol, lastCol`） | ⭐⭐⭐⭐ |
| 方法：`sheet.addMergedRegion(CellRangeAddress)` | 执行合并 | ⭐⭐⭐⭐ |

> ⚠️ 合并前确保区域内**无重复写入**，否则 Excel 可能损坏。

---

### 6. **数据格式（可选但重要）**
| 类/接口 | 说明 | 使用频率 |
|--------|------|--------|
| `org.apache.poi.ss.usermodel.DataFormat` | 自定义数字/日期格式 | ⭐⭐⭐ |
| 示例：`"¥#,##0.00"`, `"yyyy-mm-dd"` | 货币、日期格式化 | ⭐⭐⭐ |

```java
DataFormat format = workbook.createDataFormat();
CellStyle style = workbook.createCellStyle();
style.setDataFormat(format.getFormat("¥#,##0.00"));
```

---

### 7. **图片与绘图**
| 类/接口 | 说明 | 使用频率 |
|--------|------|--------|
| `org.apache.poi.ss.usermodel.Drawing<?>` | 绘图容器（插图、图表） | ⭐⭐ |
| `org.apache.poi.ss.usermodel.ClientAnchor` | 图片定位锚点 | ⭐⭐ |
| `Workbook.addPicture(byte[], int)` | 注册图片（返回索引） | ⭐⭐ |
| `Workbook.PICTURE_TYPE_PNG / JPEG` | 图片类型常量 | ⭐⭐ |

---

### 8. **辅助工具类**
| 类 | 说明 | 使用频率 |
|-----|------|--------|
| `org.apache.poi.ss.usermodel.CreationHelper` | 创建锚点、超链接等 | ⭐⭐ |
| `org.apache.poi.util.IOUtils`（旧）或 `org.springframework.util.StreamUtils`（Spring） | 读取资源流 | ⭐⭐ |

---

## ✅ 二、高频方法速查表

| 操作 | 方法 |
|------|------|
| 创建工作簿 | `new XSSFWorkbook()` |
| 创建工作表 | `workbook.createSheet("Sheet1")` |
| 创建行 | `sheet.createRow(0)` |
| 创建单元格 | `row.createCell(0)` |
| 写入值 | `cell.setCellValue("文本")` 或 `cell.setCellValue(123.45)` |
| 设置样式 | `cell.setCellStyle(style)` |
| 合并单元格 | `sheet.addMergedRegion(new CellRangeAddress(0,0,0,3))` |
| 设置列宽 | `sheet.setColumnWidth(0, 20 * 256)` |
| 写入文件 | `workbook.write(outputStream)` |
| 插入图片 | `workbook.addPicture(...) + drawing.createPicture(...)` |

---

## ✅ 三、使用建议（最佳实践）

1. **样式复用**：在循环外创建 `CellStyle`，避免内存膨胀。
2. **资源关闭**：使用 `try-with-resources` 确保 `Workbook` 关闭。
3. **大数据量**：超过 5 万行建议用 `SXSSFWorkbook`（流式写入）。
4. **中文文件名**：前端下载时用 `filename*=UTF-8''...` 编码。
5. **避免合并冲突**：不要在已合并区域重复写值。

---

## ✅ 四、依赖推荐（Maven）

```xml
<!-- 基础 POI -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>5.2.4</version>
</dependency>

<!-- 支持 .xlsx -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.4</version>
</dependency>
```

> 📌 版本建议：**5.2.x**（支持 Java 8+，功能稳定）

---

## ✅ 总结：日常开发只需掌握这 8 类

| 类别 | 核心类 |
|------|--------|
| 工作簿 | `Workbook`, `XSSFWorkbook` |
| 工作表 | `Sheet` |
| 行/单元格 | `Row`, `Cell` |
| 样式 | `CellStyle`, `Font` |
| 对齐/颜色 | `HorizontalAlignment`, `IndexedColors` |
| 边框/填充 | `BorderStyle`, `FillPatternType` |
| 合并 | `CellRangeAddress` |
| 图片（可选） | `Drawing`, `ClientAnchor` |

掌握这些，你就能应对 **99% 的 Excel 导出/导入需求**！

如需 **读取 Excel 模板、处理公式、添加批注、导出 PDF** 等高级功能，也可以继续问我 😊