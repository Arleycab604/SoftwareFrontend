module com.example.front {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;

    opens com.SaberPro.SoftwareFront to javafx.fxml;
    opens com.SaberPro.SoftwareFront.Controllers to javafx.fxml;


    opens com.tests.frontbienhecho to javafx.fxml;
    // Exportar cualquier otro paquete necesario si es requerido por otros módulos
    exports com.tests.frontbienhecho;

    exports com.SaberPro.SoftwareFront;

}