module com.example.timesheet {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires com.dlsc.formsfx;


    opens com.example.timesheet to javafx.fxml;
    exports com.example.timesheet;
    exports com.example.timesheet.operations;
    opens com.example.timesheet.operations to javafx.fxml;
    exports com.example.timesheet.controller;
    opens com.example.timesheet.controller to javafx.fxml;
}