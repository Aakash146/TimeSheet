package com.example.timesheet.operations;

import com.example.timesheet.teams.Camp;
import com.example.timesheet.teams.FleetServices;
import com.example.timesheet.teams.Salesforce;
import com.example.timesheet.teams.Telematics;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class TimeSheetOperations {

    private Camp camp = new Camp();
    private FleetServices fleetServices = new FleetServices();
    private Salesforce salesforce = new Salesforce();
    private Telematics telematics = new Telematics();

    public void generateTimeSheetAndSaveFiles(LocalDate startDate, LocalDate endDate, File resultFile, File campFile, File fleetFile, File salesforceFile, File telematicsFile){
        try (Workbook resultWorkbook = new XSSFWorkbook(new FileInputStream(resultFile));
             Workbook campWorkbook = new XSSFWorkbook();
             Workbook fleetWorkbook = new XSSFWorkbook();
             Workbook salesForceWorkbook = new XSSFWorkbook();
             Workbook telematicsWorkbook = new XSSFWorkbook()) {
            createSheets(campWorkbook, "CAMP");
            createSheets(fleetWorkbook, "FLEET");
            createSheets(salesForceWorkbook, "SALESFORCE");
            createSheets(telematicsWorkbook, "TELEMATICS");

            createSummarySheet(campWorkbook.getSheetAt(0), resultWorkbook, "CAMP", startDate, endDate);
            createSummarySheet(fleetWorkbook.getSheetAt(0), resultWorkbook, "FLEET", startDate, endDate);
            createSummarySheet(salesForceWorkbook.getSheetAt(0), resultWorkbook, "SALESFORCE", startDate, endDate);
            createSummarySheet(telematicsWorkbook.getSheetAt(0), resultWorkbook, "TELEMATICS", startDate, endDate);

            try (FileOutputStream campOut = new FileOutputStream(campFile);
                 FileOutputStream fleetOut = new FileOutputStream(fleetFile);
                 FileOutputStream salesforceOut = new FileOutputStream(salesforceFile);
                 FileOutputStream telematicsOut = new FileOutputStream(telematicsFile)) {
                campWorkbook.write(campOut);
                fleetWorkbook.write(fleetOut);
                salesForceWorkbook.write(salesforceOut);
                telematicsWorkbook.write(telematicsOut);
            }

        } catch (IOException e) {
            System.err.println("An error occurred while processing the Excel files: " + e.getMessage());
        }
    }


    private void createSheets(Workbook workbook, String team) {
        Sheet summary = workbook.createSheet("Summary");
        switch (team){
            case "CAMP" :
                camp.names.forEach(name -> workbook.createSheet(name.get(0)));
                break;
            case "FLEET" :
                fleetServices.names.forEach(name -> workbook.createSheet(name.get(0)));
                break;
            case "SALESFORCE" :
                salesforce.names.forEach(name -> workbook.createSheet(name.get(0)));
                break;
            case "TELEMATICS" :
                telematics.names.forEach(name -> workbook.createSheet(name.get(0)));
                break;
        }
    }

    private void createSummarySheet(Sheet summarySheet, Workbook resultWorkbook, String team, LocalDate startDate, LocalDate endDate) {

        Workbook summaryWorkbook = summarySheet.getWorkbook();
        CellStyle headerStyle = summaryWorkbook.createCellStyle();

        Font font  = summaryWorkbook.createFont();
        font.setFontHeight((short) 30);
        headerStyle.setFont(font);
        // Set background color
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setWrapText(true);


        // Set font style for header row (bold)
        Font headerFont = summaryWorkbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        switch (team){
            case "CAMP" :
                camp.names.forEach(name -> createMemberSheet(name, summaryWorkbook, resultWorkbook, headerStyle, startDate, endDate));
                break;
            case "FLEET" :
                fleetServices.names.forEach(name -> createMemberSheet(name, summaryWorkbook, resultWorkbook, headerStyle, startDate, endDate));
                break;
            case "SALESFORCE" :
                salesforce.names.forEach(name -> createMemberSheet(name, summaryWorkbook, resultWorkbook, headerStyle, startDate, endDate));
                break;
            case "TELEMATICS" :
                telematics.names.forEach(name -> createMemberSheet(name, summaryWorkbook, resultWorkbook, headerStyle, startDate, endDate));
                break;
        }

        Row headerRow = summarySheet.createRow(3);
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Total Hours Weekly");
        headerRow.forEach(cell -> cell.setCellStyle(headerStyle));
        headerRow.setHeightInPoints(35.00F);
        summarySheet.setColumnWidth(1, 20 * 256);
        summarySheet.setColumnWidth(2, 20 * 256);


        switch (team){
                case "CAMP" :
                    int totalNumberOfMember = camp.names.size();
                    int endOfTable = totalNumberOfMember + 4;
                    int rowNum = 4;
                    for( List<String> name : camp.names){
                        Row row = summarySheet.createRow(rowNum++);
                        row.setHeightInPoints(15);
                        row.createCell(1).setCellValue(name.get(0));
                        String sourceCellReference = "'" + summaryWorkbook.getSheet(name.get(0)).getSheetName() + "'!B7";
                        row.createCell(2).setCellFormula(sourceCellReference);
                    }
                    Row totalHoursRow = summarySheet.createRow(rowNum);
                    totalHoursRow.setHeightInPoints(15);
                    String formula = "SUM(C5:C" + rowNum + ")";
                    totalHoursRow.createCell(1).setCellValue("Total Team Hours");
                    totalHoursRow.createCell(2).setCellFormula(formula);
                    XSSFSheet xssfSheet = (XSSFSheet) summarySheet;
                    XSSFTable hourTable = xssfSheet.createTable(new AreaReference("B4:C" + endOfTable, SpreadsheetVersion.EXCEL2007));
                    summarySheet = (Sheet) xssfSheet;
                    break;
                case "FLEET" :
                    int totalNumberOfMemberFleet = fleetServices.names.size();
                    int endOfTableFleet = totalNumberOfMemberFleet + 4;
                    int rowNumFleet = 4;
                    for( List<String> name : fleetServices.names){
                        Row row = summarySheet.createRow(rowNumFleet++);
                        row.setHeightInPoints(15);
                        row.createCell(1).setCellValue(name.get(0));
                        String sourceCellReference = "'" + summaryWorkbook.getSheet(name.get(0)).getSheetName() + "'!B7";
                        row.createCell(2).setCellFormula(sourceCellReference);
                    }
                    Row totalHoursRowFleet = summarySheet.createRow(rowNumFleet);
                    totalHoursRowFleet.setHeightInPoints(15);
                    String formulaFleet = "SUM(C5:C" + rowNumFleet + ")";
                    totalHoursRowFleet.createCell(1).setCellValue("Total Team Hours");
                    totalHoursRowFleet.createCell(2).setCellFormula(formulaFleet);
                    XSSFSheet xssfSheetFleet = (XSSFSheet) summarySheet;
                    XSSFTable hourTableFleet = xssfSheetFleet.createTable(new AreaReference("B4:C" + endOfTableFleet, SpreadsheetVersion.EXCEL2007));
                    summarySheet = (Sheet) xssfSheetFleet;
                    break;
                case "SALESFORCE" :
                    int totalNumberOfMemberSales = salesforce.names.size();
                    int endOfTableSales = totalNumberOfMemberSales + 4;
                    int rowNumSales = 4;
                    for( List<String> name : salesforce.names){
                        Row row = summarySheet.createRow(rowNumSales++);
                        row.setHeightInPoints(15);
                        row.createCell(1).setCellValue(name.get(0));
                        String sourceCellReference = "'" + summaryWorkbook.getSheet(name.get(0)).getSheetName() + "'!B7";
                        row.createCell(2).setCellFormula(sourceCellReference);
                    }
                    Row totalHoursRowSales = summarySheet.createRow(rowNumSales);
                    totalHoursRowSales.setHeightInPoints(15);
                    String formulaSales = "SUM(C5:C" + rowNumSales + ")";
                    totalHoursRowSales.createCell(1).setCellValue("Total Team Hours");
                    totalHoursRowSales.createCell(2).setCellFormula(formulaSales);
                    XSSFSheet xssfSheetSales = (XSSFSheet) summarySheet;
                    XSSFTable hourTableSales = xssfSheetSales.createTable(new AreaReference("B4:C" + endOfTableSales, SpreadsheetVersion.EXCEL2007));
                    summarySheet = (Sheet) xssfSheetSales;
                    break;
                case "TELEMATICS" :
                    int totalNumberOfMemberTele = telematics.names.size();
                    int endOfTableTele = totalNumberOfMemberTele + 4;
                    int rowNumTele = 4;
                    for( List<String> name : telematics.names){
                        Row row = summarySheet.createRow(rowNumTele++);
                        row.setHeightInPoints(15);
                        row.createCell(1).setCellValue(name.get(0));
                        String sourceCellReference = "'" + summaryWorkbook.getSheet(name.get(0)).getSheetName() + "'!B7";
                        row.createCell(2).setCellFormula(sourceCellReference);
                    }
                    Row totalHoursRowTele = summarySheet.createRow(rowNumTele);
                    totalHoursRowTele.setHeightInPoints(15);
                    String formulaTele = "SUM(C5:C" + rowNumTele + ")";
                    totalHoursRowTele.createCell(1).setCellValue("Total Team Hours");
                    totalHoursRowTele.createCell(2).setCellFormula(formulaTele);
                    XSSFSheet xssfSheetTele = (XSSFSheet) summarySheet;
                    XSSFTable hourTableTele = xssfSheetTele.createTable(new AreaReference("B4:C" + endOfTableTele, SpreadsheetVersion.EXCEL2007));
                    summarySheet = (Sheet) xssfSheetTele;
                    break;
            }
    }

    private void createMemberSheet(List<String> name, Workbook summaryWorkbook, Workbook resultWorkbook, CellStyle headerStyle, LocalDate startDate, LocalDate endDate) {
        Sheet memberSheet = summaryWorkbook.getSheet(name.get(0));
        Sheet resultSheet = resultWorkbook.getSheetAt(0);
        Row headerRow = memberSheet.createRow(0);
        headerRow.setHeightInPoints(35.00F);
        memberSheet.setColumnWidth(1, 25 * 256);
        for(int i = 0 ; i < 4; i++){
            headerRow.createCell(i).setCellValue("");
            memberSheet.setColumnWidth(i, 30 * 256);
        }
        CellRangeAddress mergeRange = new CellRangeAddress(0, 0, 0, 3);
        memberSheet.addMergedRegion(mergeRange);
        headerRow.getCell(0).setCellValue("Time Sheet");
        headerRow.forEach(cell -> cell.setCellStyle(headerStyle));

        // Cell with Default or Static values
        Row memberRow = memberSheet.createRow(1);
        memberRow.setHeightInPoints(35.00F);
        memberRow.createCell(0).setCellValue("Employee Details:");
        memberRow.createCell(1).setCellValue(name.get(0));
        memberRow.createCell(2).setCellValue(name.get(4));
        memberRow.createCell(3).setCellValue("Emp ID: " + name.get(8));


        Row managerRow = memberSheet.createRow(2);
        managerRow.setHeightInPoints(35.00F);
        managerRow.createCell(0).setCellValue("Nagarro Manager Details:");
        managerRow.createCell(1).setCellValue(name.get(1));
        managerRow.createCell(2).setCellValue(name.get(5));

        Row pceoRow = memberSheet.createRow(3);
        pceoRow.setHeightInPoints(35.00F);
        pceoRow.createCell(0).setCellValue("Nagarro PCEO Details:");
        pceoRow.createCell(1).setCellValue(name.get(2));
        pceoRow.createCell(2).setCellValue(name.get(6));

        Row coxManagerRow = memberSheet.createRow(4);
        coxManagerRow.setHeightInPoints(35.00F);
        coxManagerRow.createCell(0).setCellValue("Cox Manager Details:");
        coxManagerRow.createCell(1).setCellValue(name.get(3));
        coxManagerRow.createCell(2).setCellValue(name.get(7));

        Row periodRow = memberSheet.createRow(5);
        periodRow.setHeightInPoints(35.00F);
        periodRow.createCell(0).setCellValue("Period Start Date");
        periodRow.createCell(1).setCellValue(startDate.toString());
        periodRow.createCell(2).setCellValue("Period End Date");
        periodRow.createCell(3).setCellValue(endDate.toString());

        Row actualHoursRow = memberSheet.createRow(6);
        actualHoursRow.setHeightInPoints(35.00F);
        actualHoursRow.createCell(0).setCellValue("Total Work Hours");
        actualHoursRow.createCell(1).setCellFormula("SUM(C10:C14)");

        Row tableHeaderRow = memberSheet.createRow(8);
        tableHeaderRow.setHeightInPoints(35.00F);
        tableHeaderRow.createCell(1).setCellValue("Date(s)");
        tableHeaderRow.createCell(2).setCellValue("Hours Worked");

        LocalDate incDate = LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
        for(int i = 9; i < 14; i++){
            Row tableRow = memberSheet.createRow(i);
            tableRow.setHeightInPoints(20.00F);
            tableRow.createCell(1).setCellValue(incDate.toString());
            for(Row row : resultSheet){
                if(row.getCell(1).getStringCellValue() == "T001")
                    continue;
                if(row.getCell(10).getStringCellValue().equalsIgnoreCase("date") || row.getCell(10).getStringCellValue().equalsIgnoreCase("") || row.getCell(10).getStringCellValue() == null)
                    continue;
                String incDateString = incDate.toString();
                String rowDate = row.getCell(10).getStringCellValue();
                String rowDateString = LocalDate.of(Integer.parseInt(rowDate.substring(6)),Integer.parseInt(rowDate.substring(3,5)),Integer.parseInt(rowDate.substring(0,2))).toString();
                if(row.getCell(8).toString().equals(name.get(8)) && incDateString.equalsIgnoreCase(rowDateString)){
                    tableRow.createCell(2).setCellValue(row.getCell(14).getNumericCellValue());
                }
            }
            incDate = incDate.plusDays(1);
            if(incDate.getDayOfWeek() == DayOfWeek.SATURDAY)
                break;
        }

    }
}
