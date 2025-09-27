module com.example.courseprifs {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    //requires static lombok;
    //requires com.example.courseprifs;

    opens com.example.courseprifs to javafx.fxml;
    exports com.example.courseprifs;
    exports com.example.courseprifs.fxControllers to javafx.fxml;
    opens com.example.courseprifs.fxControllers to javafx.fxml;
}