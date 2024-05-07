package com.example.timesheet.controller;

import com.example.timesheet.operations.PivotFileOperations;
import com.example.timesheet.operations.TimeSheetOperations;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;


public class TimesheetController {
    private Stage stage;
    public LocalDate startDate;
    public LocalDate endDate;
    public File inputFile1;
    public File inputFile2;
    public File resultFile;
    public File campFile;
    public File fleetFile;
    public File salesforceFile;
    public File telematicsFile;

    private final PivotFileOperations pivotFileOperations;
    private final TimeSheetOperations timeSheetOperations;

    public TimesheetController(Stage stage) {
        this.stage = stage;
        pivotFileOperations = new PivotFileOperations();
        timeSheetOperations = new TimeSheetOperations();
    }

    // Method to select the first input file
    @FXML
    public void selectFile1() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        inputFile1 = fileChooser.showOpenDialog(stage);
    }

    // Method to select the second input file
    @FXML
    public void selectFile2() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        inputFile2 = fileChooser.showOpenDialog(stage);
    }

    // Method to save the combined result to a new Excel file
    @FXML
    public void saveResult() {
        if (inputFile1 == null || inputFile2 == null) {
            System.err.println("Please select both input files.");
            return;
        }

        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setInitialFileName("Combined SAP TimeSheet " + startDate.getDayOfMonth() + " " + endDate.getMonth()  + " to " + endDate.getDayOfMonth() + " " + endDate.getMonth() + ".xlsx");
        resultFile = saveFileChooser.showSaveDialog(stage);

        if (resultFile != null) {
            pivotFileOperations.combineAndSaveFiles(inputFile1, inputFile2, resultFile);
        }
    }

    // Method to generate timesheet of all the teams with the member in the respective team
    @FXML
    public void generateTimeSheet() {
        if (resultFile == null) {
            System.err.println("Result File is null.");
            return;
        }

        FileChooser campFileChooser = new FileChooser();
        campFileChooser.setInitialFileName("Nagarro Timesheet_CAMP " + startDate.getDayOfMonth() + " " + endDate.getMonth()  + " to " + endDate.getDayOfMonth() + " " + endDate.getMonth() + ".xlsx");
        FileChooser fleetFileChooser = new FileChooser();
        fleetFileChooser.setInitialFileName("Nagarro Timesheet_FleetServices " + startDate.getDayOfMonth() + " " + endDate.getMonth()  + " to " + endDate.getDayOfMonth() + " " + endDate.getMonth() + ".xlsx");
        FileChooser salesforceFileChooser = new FileChooser();
        salesforceFileChooser.setInitialFileName("Nagarro Timesheet_SalesForce " + startDate.getDayOfMonth() + " " + endDate.getMonth()  + " to " + endDate.getDayOfMonth() + " " + endDate.getMonth() + ".xlsx");
        FileChooser telematicsFileChooser = new FileChooser();
        telematicsFileChooser.setInitialFileName("Nagarro Timesheet_Telematics " + startDate.getDayOfMonth() + " " + endDate.getMonth()  + " to " + endDate.getDayOfMonth() + " " + endDate.getMonth() + ".xlsx");
        campFile = campFileChooser.showSaveDialog(stage);
        fleetFile = fleetFileChooser.showSaveDialog(stage);
        salesforceFile = salesforceFileChooser.showSaveDialog(stage);
        telematicsFile = telematicsFileChooser.showSaveDialog(stage);

        if (campFile != null && fleetFile != null && salesforceFile != null && telematicsFile != null) {
            timeSheetOperations.generateTimeSheetAndSaveFiles(startDate, endDate, resultFile,campFile,fleetFile, salesforceFile,telematicsFile);
        }
    }
}
