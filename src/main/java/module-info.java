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
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires static lombok;

    opens com.SaberPro.SoftwareFront to javafx.fxml;
    opens com.SaberPro.SoftwareFront.Controllers to javafx.fxml;


    opens com.tests.frontbienhecho to javafx.fxml;
    // Exportar cualquier otro paquete necesario si es requerido por otros módulos
    exports com.tests.frontbienhecho;
    exports com.SaberPro.SoftwareFront.Classes;
    opens com.SaberPro.SoftwareFront.Classes to com.fasterxml.jackson.databind;
    exports com.SaberPro.SoftwareFront;

}