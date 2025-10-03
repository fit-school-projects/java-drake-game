module thedrake {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens thedrake to javafx.fxml;
    opens thedrake.ui to javafx.fxml;
    exports thedrake;
    exports thedrake.ui;
    exports thedrake.src;
}