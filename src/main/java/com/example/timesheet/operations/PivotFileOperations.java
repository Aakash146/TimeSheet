package com.example.timesheet.operations;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class PivotFileOperations {

    // Method to combine data from the two input files and save to the result file
    public void combineAndSaveFiles(File inputFile1, File inputFile2, File resultFile) {
        try (Workbook workbook1 = new XSSFWorkbook(new FileInputStream(inputFile1));
             Workbook workbook2 = new XSSFWorkbook(new FileInputStream(inputFile2));
             Workbook resultWorkbook = new XSSFWorkbook()) {

            // Combine data from input files and create the result Excel file
            Sheet sheet1 = workbook1.getSheetAt(0);
            Sheet sheet2 = workbook2.getSheetAt(0);

            // Create a new sheet in the result workbook
            Sheet resultSheet1 = resultWorkbook.createSheet("Combined_Data");
            Sheet resultSheet2 = resultWorkbook.createSheet("Pivot_Table");

            // Combine data and write to resultSheet1
            combineSheets(sheet1, sheet2, resultSheet1);

            // Create a pivot table in resultSheet2 based on the combined data
            createPivotTable(resultSheet1, resultSheet2);

            // Write the result workbook to the specified file
            try (FileOutputStream out = new FileOutputStream(resultFile)) {
                resultWorkbook.write(out);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while processing the Excel files: " + e.getMessage());
        }
    }
    // Method to read data from an Excel file and return it as a Map
    public void combineSheets(Sheet sheet1, Sheet sheet2, Sheet resultSheet1) {
        // Implement your data combination logic here
        // Assuming both input files have the same structure (column headers)

        // Create a map to store the combined data
        Map<String, RowData> combinedData = new HashMap<>();

        // Read data from sheet1 and combine it
        readAndCombineData(sheet1, combinedData);

        // Read data from sheet2 and combine it
        readAndCombineData(sheet2, combinedData);

        // Write combined data to resultSheet1
        writeCombinedDataToSheet(resultSheet1, combinedData);
    }

    // Read data from the sheet and combine it into the combinedData map
    private void readAndCombineData(Sheet sheet, Map<String, RowData> combinedData) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0)
                continue;

            if(Objects.isNull(row.getCell(0)))
                continue;

            // Extract data from the row
            Cell workPackageIdCell = row.getCell(0); // Work Package ID
            Cell activityTypeCell = row.getCell(1);
            Cell projectBUIDCell = row.getCell(2);
            Cell projectBUCell = row.getCell(3);
            Cell projectCostCell = row.getCell(4);
            Cell projectCostTagCell = row.getCell(5);
            Cell shortTextCell = row.getCell(6);
            Cell personnelNumberCell = row.getCell(7);
            Cell empoloyeeIdCell = row.getCell(8);
            Cell fullNameCell = row.getCell(9);
            Cell dateCell = row.getCell(10);
            Cell startCell = row.getCell(11);
            Cell endCell = row.getCell(12);
            Cell statusTextCell = row.getCell(13);
            Cell hoursCell = row.getCell(14);


            String workPackageId = workPackageIdCell.getStringCellValue();
            String activityType = activityTypeCell.getStringCellValue();
            String projectBUID = projectBUIDCell.getStringCellValue();
            String projectBU = projectBUCell.getStringCellValue();
            String projectCost = projectCostCell.getStringCellValue();
            String projectCostTag = projectCostTagCell.getStringCellValue();
            String shortText = shortTextCell.getStringCellValue();
            String personnelNumber = personnelNumberCell.getStringCellValue();
            String empoloyeeId = empoloyeeIdCell.getStringCellValue();
            String fullName = fullNameCell.getStringCellValue();
            String date = dateCell.getStringCellValue();
            String start = startCell.getStringCellValue();
            String end = endCell.getStringCellValue();
            String statusText = statusTextCell.getStringCellValue();
            double hours = hoursCell.getNumericCellValue();

            // Create a unique key based on Work Package ID and date
            String key = workPackageId + "_"+ activityType + "_" + empoloyeeId + "_" + date;

            // Check if the key already exists in the combinedData map
            if (!combinedData.containsKey(key)) {
                // Create a new RowData entry
                combinedData.put(key, new RowData(workPackageId, activityType, projectBUID, projectBU, projectCost, projectCostTag, shortText, personnelNumber, empoloyeeId, fullName, date, start, end,statusText, hours));
            }
            else{
                RowData rowData = combinedData.get(key);
                rowData.hours = rowData.getHours() + hours;
                combinedData.put(key, rowData);
            }

            // Add hours to the appropriate Full Name entry
            combinedData.get(key).addHours(fullName, hours);
        }
    }

    // Write combined data to the result sheet
    private void writeCombinedDataToSheet(Sheet resultSheet, Map<String, RowData> combinedData) {

        Workbook workbook = resultSheet.getWorkbook();
        CellStyle headerStyle = workbook.createCellStyle();

        // Set background color to light blue
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Set font style for header row (bold)
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Write header row
        Row headerRow = resultSheet.createRow(0);
        headerRow.setHeightInPoints(20.00F);
        headerRow.createCell(0).setCellValue("Work Package ID");
        headerRow.createCell(1).setCellValue("Activity Type");
        headerRow.createCell(2).setCellValue("Project BU ID");
        headerRow.createCell(3).setCellValue("Project BU");
        headerRow.createCell(4).setCellValue("Project Cost Center");
        headerRow.createCell(5).setCellValue("Project Cost Center");
        headerRow.createCell(6).setCellValue("Short Text");
        headerRow.createCell(7).setCellValue("Personnel Number");
        headerRow.createCell(8).setCellValue("Employee ID");
        headerRow.createCell(9).setCellValue("Full Name");
        headerRow.createCell(10).setCellValue("Date");
        headerRow.createCell(11).setCellValue("Start Time");
        headerRow.createCell(12).setCellValue("End Time");
        headerRow.createCell(13).setCellValue("Status Text");
        headerRow.createCell(14).setCellValue("Hours");
        headerRow.forEach(cell -> cell.setCellStyle(headerStyle));

        int rowIndex = 1;
        for (RowData rowData : combinedData.values()) {
            Row row = resultSheet.createRow(rowIndex++);
            row.setHeightInPoints(15);
            row.createCell(0).setCellValue(rowData.getWorkPackageId());
            row.createCell(1).setCellValue(rowData.getActivityType()); // Placeholder value, customize as needed
            row.createCell(2).setCellValue(rowData.getProjectBUID()); // Placeholder value, customize as needed
            row.createCell(3).setCellValue(rowData.getProjectBU()); // Placeholder value, customize as needed
            row.createCell(4).setCellValue(rowData.getProjectCost()); // Placeholder value, customize as needed
            row.createCell(5).setCellValue(rowData.getProjectCostTag()); // Placeholder value, customize as needed
            row.createCell(6).setCellValue(rowData.getShortText()); // Placeholder value, customize as needed
            row.createCell(7).setCellValue(rowData.getPersonnelNumber()); // Placeholder value, customize as needed
            row.createCell(8).setCellValue(rowData.getEmployeeId());
            row.createCell(9).setCellValue(rowData.getFullName());
            row.createCell(10).setCellValue(rowData.getDate());
            row.createCell(11).setCellValue(rowData.getStart());
            row.createCell(12).setCellValue(rowData.getEnd());
            row.createCell(13).setCellValue(rowData.getStatusText());
            row.createCell(14).setCellValue(rowData.getHours());
        }

        resultSheet.setAutoFilter(new CellRangeAddress(0,rowIndex-1,0,14));
    }

    // Create a pivot table in resultSheet2 based on the combined data
    public void createPivotTable(Sheet sheet, Sheet pivotSheet) {
//        XSSFSheet xssfSheet = (XSSFSheet) pivotSheet;
//        pivotSheet.createRow(0).createCell(0).setCellValue("");
//        CellReference start = new CellReference(sheet.getRow(0).getCell(0));
//        CellReference end = new CellReference(sheet.getRow(sheet.getLastRowNum()-1).getCell(14));
//        XSSFPivotTable pivotTable = ((XSSFSheet) pivotSheet).createPivotTable(new AreaReference(start, end, SpreadsheetVersion.EXCEL2007), new CellReference("A1"), sheet);
//        pivotTable.addRowLabel(0);
//        pivotTable.addRowLabel(1);
//        pivotTable.addColumnLabel(DataConsolidateFunction.SUM, 14, "Total Hours");
//        pivotSheet =(Sheet) xssfSheet;
    }

    // Inner class to represent row data
    private static class RowData {
        private final String workPackageId;
        private final String activityType;
        private final String ProjectBUID;
        private final String projectBU;
        private final String projectCost;
        private final String projectCostTag;
        private final String shortText;
        private final String personnelNumber;
        private final String employeeId;
        private final String fullName;
        private final String date;
        private final String start;
        private final String end;
        private final String statusText;
        private double hours;
        private final Map<String, Double> hoursByFullName;

        public RowData(String workPackageId, String activityType, String projectBUID, String projectBU, String projectCost, String projectCostTag, String shortText, String personnelNumber, String employeeId, String fullName, String date, String start, String end, String statusText, double hours) {
            this.workPackageId = workPackageId;
            this.activityType = activityType;
            ProjectBUID = projectBUID;
            this.projectBU = projectBU;
            this.projectCost = projectCost;
            this.projectCostTag = projectCostTag;
            this.shortText = shortText;
            this.personnelNumber = personnelNumber;
            this.employeeId = employeeId;
            this.fullName = fullName;
            this.date = date;
            this.start = start;
            this.end = end;
            this.statusText = statusText;
            this.hours = hours;
            this.hoursByFullName = new HashMap<>();
        }

        public String getWorkPackageId() {
            return workPackageId;
        }

        public String getDate() {
            return date;
        }

        public String getActivityType() {
            return activityType;
        }

        public String getProjectBUID() {
            return ProjectBUID;
        }

        public String getProjectBU() {
            return projectBU;
        }

        public String getProjectCost() {
            return projectCost;
        }

        public String getProjectCostTag() {
            return projectCostTag;
        }

        public String getShortText() {
            return shortText;
        }

        public String getPersonnelNumber() {
            return personnelNumber;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public String getStart() {
            return start;
        }

        public String getFullName(){
            return fullName;
        }

        public String getEnd() {
            return end;
        }

        public String getStatusText() {
            return statusText;
        }

        public double getHours() {
            return hours;
        }


        public Map<String, Double> getHoursByFullName() {
            return hoursByFullName;
        }



        public void addHours(String fullName, double hours) {
            hoursByFullName.put(fullName, hoursByFullName.getOrDefault(fullName, 0.0) + hours);
        }

        public double getTotalHours() {
            return hoursByFullName.values().stream().mapToDouble(Double::doubleValue).sum();
        }
    }
}

