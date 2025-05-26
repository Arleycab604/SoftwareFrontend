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
    requires java.net.http;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires static lombok;
    requires java.compiler;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.datatype.jsr310;

    // --- EXPORTS NECESARIOS ---
    exports com.SaberPro.SoftwareFront;
    exports com.SaberPro.SoftwareFront.Models;
    exports com.SaberPro.SoftwareFront.Models.accionMejora;
    exports com.SaberPro.SoftwareFront.Utils.Enums to com.fasterxml.jackson.databind;

    exports com.SaberPro.SoftwareFront.Controllers.Acciones to javafx.fxml;
    exports com.SaberPro.SoftwareFront.Controllers.Acciones.Detalles to javafx.fxml; // ✅ Exportar paquete del controlador
    exports com.SaberPro.SoftwareFront.Utils;

    // --- OPENS PARA REFLEXIÓN (FXML y Jackson) ---
    opens com.SaberPro.SoftwareFront to javafx.fxml;
    opens com.SaberPro.SoftwareFront.Controllers to javafx.fxml;
    opens com.SaberPro.SoftwareFront.Controllers.Acciones to javafx.fxml, com.fasterxml.jackson.datatype.jsr310;
    opens com.SaberPro.SoftwareFront.Controllers.Acciones.Detalles to javafx.fxml; // ✅ Necesario para FXMLLoader
    opens com.SaberPro.SoftwareFront.Controllers.AccionesMejora to javafx.fxml;
    opens com.SaberPro.SoftwareFront.Controllers.Login to javafx.fxml;
    opens com.SaberPro.SoftwareFront.Controllers.ConsultarHistoricos to javafx.fxml;
    opens com.SaberPro.SoftwareFront.Controllers.Roles to javafx.fxml;
    opens com.SaberPro.SoftwareFront.Controllers.Cumplimiento to javafx.fxml;

    opens com.SaberPro.SoftwareFront.Models to com.fasterxml.jackson.databind, javafx.fxml;
    opens com.SaberPro.SoftwareFront.Models.accionMejora to com.fasterxml.jackson.databind, javafx.fxml;
    opens com.SaberPro.SoftwareFront.Utils.Enums to com.fasterxml.jackson.databind;
}
