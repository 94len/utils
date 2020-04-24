package com.hyqfx.util.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

/**
 * POI excel表工具类
 * @auth len
 * @createTime 2019/8/30
 */
public class POIUtils {
    HSSFWorkbook wb = new HSSFWorkbook();
    HSSFSheet sheet = null;
    Row headRow = null;
    String[] sheetHeader = null;
    List<String[]> data = null;
    // 默认表名
    String excelName = "ExcelData" + LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    // 行下标
    int rowIndex = 0;
    // sheet下标
    int sheetIndex = 1;


    /**
     * 设置表头
     * @param headers 表头数据
     * @return
     */
    public POIUtils setHeader(String[] headers){
        sheetHeader = headers;
        this.createHeader();
        return this;
    }

    /**
     * 设置数据
     * @param datas 数据体
     * @return
     */
    public POIUtils setSheetData(List<POIUtils.SheetData> datas){
        if(datas != null && datas.size() > 0){
            this.fillData(datas);
        }

        return this;

    }

    /**
     * 设置数据
     * @param data 数据体
     * @return
     */
    public POIUtils setData(List<String[]> data){
        if(data != null && data.size() > 0){
            this.data = data;
            this.fillData();
        }

        return this;

    }

    /**
     * 设置文件名
     * @param excelName excel文件名
     */
    public POIUtils setExcelName(String excelName){
        this.excelName = excelName;
        return this;
    }

    /**
     * 生成exce表
     * 页面自动弹出下载窗口
     * @param response HttpServletResponse接口响应
     */
    public void getExcelAndDownload(HttpServletResponse response){
        OutputStream output = null;
        try {
            output = response.getOutputStream();
            response.reset();
            response.setContentType("application/msexcel");
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(excelName, "UTF-8") + ".xls");
            wb.write(output);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(output != null){
                try {
//                    output.flush();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createHeader(){
        if(sheet == null){
            sheet = wb.createSheet("表"+sheetIndex);
            sheetIndex++;
        }

        // 表头
        // 设置默认宽度
        sheet.setDefaultColumnWidth(20);

        Row headRow = sheet.createRow(rowIndex++);

        HSSFCellStyle style = wb.createCellStyle();
        // 设置表头行样式
        HSSFFont font = wb.createFont();
        font.setFontHeight((short)(2.5*100));
        font.setFontName("黑体");
        style.setFont(font);

        // 设置单元格普通样式
        createCellStyle(style);

        createRow(headRow,style,sheetHeader);

        sheet.setAutobreaks(true);

    }

    /**
     * 填充数据
     */
    private void fillData(){
        if(sheet == null){
            sheet = wb.createSheet("表"+sheetIndex);
            sheetIndex++;
        }
        HSSFCellStyle cellStyle =  wb.createCellStyle();
        createCellStyle(cellStyle);

        if(data != null){
            HSSFRow row = null;
            for (int i = 0; i < data.size(); i++) {
                if(rowIndex >= 65000){
                    // 重置行下标
                    rowIndex = 0;
                    sheet = wb.createSheet("表"+sheetIndex);
                    sheetIndex++;
                    if(sheetHeader != null){
                        this.createHeader();
                    }
                }
                try {
                    row = sheet.createRow(rowIndex++);
                } catch (Exception e) {
                    System.out.println("###sheetIndex: "+sheetIndex);
                    System.out.println("###rowIndex: "+rowIndex);
                    System.out.println("###i: "+i);
                    e.printStackTrace();
                    throw e;
                }
                createRow(row,cellStyle,data.get(i));
            }

        }
    }

    /**
     * 创建行
     * @param row
     * @param cellStyle
     * @param strings
     * @return
     */
    private void createRow(Row row, CellStyle cellStyle, String... strings) {
        if (strings == null){
            return;
        }
        // 设置行高
        row.setHeightInPoints(20.0f);
        Cell cell;
        for (int i = 0; i < strings.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(strings[i]);

        }
    }

    /**
     * 创建普通表格样式: 加边框、居中
     * @param cellStyle
     * @return
     */
    private void createCellStyle(HSSFCellStyle cellStyle) {
        // 水平居中
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// poi v3.1版本这样写 cellStyle.setAlignment(HorizontalAlignment.CENTER);
		
		
        // 垂直居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// poi v3.1版本这样写 cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		
        // 边框
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		// poi v3.1版本这样写 cellStyle.setBorderTop(BorderStyle.THIN);
		 
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setWrapText(true);
    }

    /**
     * 填充多个sheet
     * @param list 填充数据
     */
    private void fillData(List<SheetData> list){
        if (list == null || list.size() < 1){
            return;
        }
        HSSFRow row;
        CellRangeData cellRangeData;
        for (SheetData sheetData : list){
            // 重置行下标
            rowIndex = 0;
            sheet = wb.createSheet(sheetData.getSheetName()+sheetIndex);
            sheetHeader = sheetData.sheetHeader;
            if (sheetHeader != null){
                this.createHeader();
            }
            sheetIndex++;
            sheet.setDefaultColumnWidth(20);
            HSSFCellStyle cellStyle =  wb.createCellStyle();
            createCellStyle(cellStyle);
            data = sheetData.getData();
            if(data != null){
                for (int i = 0; i < data.size(); i++) {
                    if(rowIndex >= 65000){
                        // 重置行下标
                        rowIndex = 0;
                        sheet = wb.createSheet(sheetData.getSheetName()+sheetIndex);
                        sheetIndex++;
                        sheetHeader = sheetData.sheetHeader;
                        this.createHeader();
                    }
                    try {
                        row = sheet.createRow(rowIndex++);
                        if (sheetData.cellRangeDataMap != null
                                && (cellRangeData = sheetData.cellRangeDataMap.get(i)) != null){
                            sheet.addMergedRegion(new CellRangeAddress(cellRangeData.startRow,cellRangeData.endtRow
                                    ,cellRangeData.startCloum,cellRangeData.endCloum));
                            rowIndex += cellRangeData.endtRow - cellRangeData.startRow;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                    createRow(row,cellStyle,data.get(i));
                }

            }
        }
    }

    public static class SheetData{
        /**
         * 表格名称
         */
        private String sheetName;
        /**
         * sheet中的数据
         */
        private List<String[]> data;
        /**
         * sheet中的表头
         */
        private String[] sheetHeader;
        /**
         * 合并单元格的数据
         * key：第几行数据开始进行单元格合并
         * value：合并单元的需要的4个参数
         */
        private Map<Integer,CellRangeData> cellRangeDataMap;

        public SheetData() {}

        public SheetData data(List<String[]> data){
            this.data = data;
            return this;
        }

        public SheetData sheetName(String sheetName){
            this.sheetName = sheetName;
            return this;
        }

        public SheetData sheetHeader(String[] sheetHeader){
            this.sheetHeader = sheetHeader;
            return this;
        }

        public String getSheetName() {
            return sheetName;
        }

        public List<String[]> getData() {
            return data;
        }
        public SheetData cellRangeDataMap(Map<Integer,CellRangeData> cellRangeDataMap) {
            this.cellRangeDataMap = cellRangeDataMap;
            return this;
        }
    }

    public static class CellRangeData{
        /**
         * 起始行
         */
        private Integer startRow;
        /**
         * 结束行
         */
        private Integer endtRow;
        /**
         * 起始列
         */
        private Integer startCloum;
        /**
         * 结束列
         */
        private Integer endCloum;

        public CellRangeData(Integer startRow,Integer endtRow,Integer startCloum,Integer endCloum){
            this.startRow = startRow;
            this.endtRow = endtRow;
            this.startCloum = startCloum;
            this.endCloum = endCloum;
        }
        public CellRangeData(){}
    }
}
