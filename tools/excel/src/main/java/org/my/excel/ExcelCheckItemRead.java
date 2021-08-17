package org.my.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author 李杰  210242
 * @description:
 * @date 2021/8/16 11:15
 */
public class ExcelCheckItemRead {
    public static void main(String[] args) throws IOException {
        Map<String, List<String>> itemContentMap = getCheckItemContent();
        List<Asset> assetList = getAssetItem(itemContentMap);
        setAssetCheckItem(assetList, itemContentMap);
    }

    /**
     * 设备资产检查项
     * @param assetList
     * @param itemContentMap
     * @throws IOException
     */
    private static void setAssetCheckItem(List<Asset> assetList, Map<String, List<String>> itemContentMap) throws IOException {
        Set<String> assetCodeSet = new HashSet<>();
        //创建Excel文件薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表sheeet
        Sheet sheet = workbook.createSheet();
        //创建第一行
        Row row = sheet.createRow(0);
        String[] title = {"路线名称", "区域名称（标牌名称）", "资产编码（9位）", "资产名称", "采集数据信息"};
        Cell cell = null;
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }
        int rowIndex = 1;
        //遍历资产LIST
        for (Asset asset : assetList) {
            if (assetCodeSet.contains(asset.getAssetCode())){
                continue;
            }else {
                assetCodeSet.add(asset.getAssetCode());
            }

            itemContentMap.forEach((key, value) -> {
                if (asset.getAssetName().contains(key)) {
                    asset.setCheckItemList(value);
                }
            });
            if (asset.getCheckItemList() != null) {
                for (int i = 0; i < asset.getCheckItemList().size(); i++) {
                    String checkItemStr = asset.getCheckItemList().get(i);
                    Row newRow = sheet.createRow(rowIndex++);
                    if (i == 0 ){
                        newRow.createCell(0).setCellValue(asset.getRouting());
                        newRow.createCell(1).setCellValue(asset.getArea());
                        newRow.createCell(2).setCellValue(asset.getAssetCode());
                        newRow.createCell(3).setCellValue(asset.getAssetName());
                    }
                    newRow.createCell(4).setCellValue(checkItemStr);
                }
            }
        }

        //创建一个文件
        File file = new File("E:\\Work\\Data\\公司\\设备点检内容\\update\\1250_update_checkitem.xls");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream stream = new FileOutputStream(file);
        workbook.write(stream);
        stream.close();

    }

    /**
     * 获取资产明细
     *
     * @param itemContentMap
     * @return
     * @throws IOException
     */
    private static List<Asset> getAssetItem(Map<String, List<String>> itemContentMap) throws IOException {
        List<Asset> itemContentList = new ArrayList();
        //excel文件路径
        String excelPath = "E:\\Work\\Data\\公司\\设备点检内容\\update\\1250机械点检标准_original.xls";
        Workbook workbook = getReadWorkbook(excelPath);
        //开始解析
        Sheet sheet = workbook.getSheet("运行班炉区粗轧点检路线");

        for (int rIndex = 3; rIndex <= sheet.getLastRowNum(); rIndex++) {
            Row row = sheet.getRow(rIndex);
            if (row != null) {
                Asset asset = new Asset();
                asset.setRouting(row.getCell(1).toString());
                asset.setArea(row.getCell(3).toString());
                asset.setAssetCode(row.getCell(5).toString());
                asset.setAssetName(row.getCell(6).toString());
                itemContentList.add(asset);
            }
        }
        return itemContentList;
    }

    /**
     * 读取设备的各项点检内容
     *
     * @return
     */
    private static Map<String, List<String>> getCheckItemContent() {
        //excel文件路径
        String excelPath = "E:\\Work\\Data\\公司\\设备点检内容\\update\\点检项.xlsx";
        Map<String, List<String>> checkItemMap = new HashMap<>();

        try {
            Workbook wb = getReadWorkbook(excelPath);

            //开始解析
            Sheet sheet = wb.getSheet("点检项整理");

            int firstRowIndex = sheet.getFirstRowNum();   //第一行是列名，所以不读
            int lastRowIndex = sheet.getLastRowNum();

            String itemKey = "";
            for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                Row row = sheet.getRow(rIndex);
                if (row != null) {
                    int firstCellIndex = row.getFirstCellNum();
                    int lastCellIndex = row.getLastCellNum();
                    //点检设备项
                    if (row.getCell(0) != null && !row.getCell(0).toString().isEmpty()) {
                        itemKey = row.getCell(0).toString();
                    }

                    for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                        Cell cell = row.getCell(cIndex);
                        if (cell != null) {
                            String cellVal = cell.toString();
                            if (!cellVal.isEmpty() && cIndex != firstCellIndex) {
//                                    System.out.print(itemKey + " : " + cellVal );
                                checkItemMap.computeIfAbsent(itemKey, k -> new ArrayList<String>()).add(cellVal);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkItemMap;
    }

    /**
     * 获取读入文件workbook
     *
     * @param excelPath
     * @return
     * @throws IOException
     */
    private static Workbook getReadWorkbook(String excelPath) throws IOException {
        File excel = new File(excelPath);
        FileInputStream fis = new FileInputStream(excel);   //文件流对象
        if (!excel.isFile() || !excel.exists()) {   //判断文件是否存在
            return null;
        }
        String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
        Workbook wb;
        //根据文件后缀（xls/xlsx）进行判断
        if ("xls".equals(split[1])) {
            wb = new HSSFWorkbook(fis);
        } else if ("xlsx".equals(split[1])) {
            wb = new XSSFWorkbook(fis);
        } else {
            System.out.println("文件类型错误!");
            return null;
        }
        return wb;
    }
}
