package com.example.timesheet;

import com.example.timesheet.controller.TimesheetController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.time.DayOfWeek;

public class TimesheetApplication extends Application {
    private TimesheetController controller;

    @Override
    public void start(Stage primaryStage) {
        controller = new TimesheetController(primaryStage);


        Label startDateLabel = new Label("Start Date:");
        DatePicker startDate = new DatePicker();
        Label endDateLabel = new Label("End Date:");
        DatePicker endDate = new DatePicker();
        endDate.setDisable(true);
        endDate.setVisible(false);

        // Create buttons
        Button selectFile1Button = new Button("Select File 1");
        Button selectFile2Button = new Button("Select File 2");
        Button saveButton = new Button("Save Result");
        Button generateTimeSheetButton = new Button("Generate Final TimeSheet");

        // Event handler for selecting input files
        selectFile1Button.setOnAction(e -> {
            controller.selectFile1();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Selection Confirmation");
            alert.setHeaderText("File Selected Successfully");
            alert.setContentText("Path : " + controller.inputFile1.getAbsolutePath());
            alert.showAndWait();
        });

        selectFile2Button.setOnAction(e -> {
            controller.selectFile2();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Selection Confirmation");
            alert.setHeaderText("File Selected Successfully");
            alert.setContentText("Path : " + controller.inputFile2.getAbsolutePath());
            alert.showAndWait();
        });

        // Event handler for saving result
        saveButton.setOnAction(e -> {
            controller.startDate = startDate.getValue();

            DayOfWeek dayOfWeek = startDate.getValue().getDayOfWeek();
            // Calculate the number of days until next Sunday
            int daysUntilNextSunday = DayOfWeek.SUNDAY.getValue() - dayOfWeek.getValue();
            // If the current day is Sunday, add 7 days to get to the next Sunday
            if (daysUntilNextSunday <= 0) {
                daysUntilNextSunday += 7;
            }

            endDate.setValue(startDate.getValue().plusDays(daysUntilNextSunday));
            controller.endDate = startDate.getValue().plusDays(daysUntilNextSunday);
            controller.saveResult();
            endDate.setVisible(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save Confirmation");
            alert.setHeaderText("Save Successful");
            alert.setContentText("The data has been successfully saved!");
            alert.showAndWait();
        });

        generateTimeSheetButton.setOnAction(e -> {
            controller.generateTimeSheet();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("TimeSheet Confirmation");
            alert.setHeaderText("TimeSheet Generated Successfully");
            alert.setContentText("All the timesheet have been successfully saved!");
            alert.showAndWait();
        });

        // Add all components to layout
        HBox datePikerHBox = new HBox(20);
        datePikerHBox.getChildren().addAll(startDateLabel, startDate, endDateLabel, endDate);

        // Center the buttons within the HBox
        datePikerHBox.setAlignment(Pos.CENTER);


        HBox buttonHBox = new HBox(10);
        buttonHBox.getChildren().addAll(selectFile1Button, selectFile2Button, saveButton, generateTimeSheetButton);

        // Center the buttons within the HBox
        buttonHBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(20); // 20 is the spacing between buttons
        vBox.getChildren().addAll(datePikerHBox, buttonHBox);

        vBox.setAlignment(Pos.CENTER);

        // Create scene and set stage
        Scene scene = new Scene(vBox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TimeSheet App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
