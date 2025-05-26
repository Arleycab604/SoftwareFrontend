package com.SaberPro.SoftwareFront.Controllers.AccionesEvidencia;

import com.SaberPro.SoftwareFront.Models.accionMejora.PropuestaMejoraDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

public class DetallePropuestaController {

    @FXML
    private Label tituloPropuesta;
    @FXML
    private Label descripcionPropuesta;
    private PropuestaMejoraDTO propuesta;

    public void setPropuesta(PropuestaMejoraDTO propuesta) {
        this.propuesta = propuesta;
        tituloPropuesta.setText(propuesta.getNombrePropuesta());
        descripcionPropuesta.setText(propuesta.getDescripcion());
    }


    public void enviarEvidencia(String nombreDocente, String idPropuestaMejora, List<File> archivos) throws IOException {
        String boundary = UUID.randomUUID().toString();
        String LINE_FEED = "\r\n";
        URL url = new URL("http://localhost:8080/evidencias/crear"); // Reemplaza por tu URL real
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        OutputStream outputStream = connection.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);

        // Campo: nombreDocente
        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"nombreDocente\"").append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=UTF-8").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(nombreDocente).append(LINE_FEED);
        writer.flush();

        // Campo: idPropuestaMejora
        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"idPropuestaMejora\"").append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=UTF-8").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(idPropuestaMejora).append(LINE_FEED);
        writer.flush();

        // Campo: archivos[]
        for (File archivo : archivos) {
            String fileName = archivo.getName();
            writer.append("--").append(boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"archivos\"; filename=\"" + fileName + "\"").append(LINE_FEED);
            writer.append("Content-Type: " + Files.probeContentType(archivo.toPath())).append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

            FileInputStream inputStream = new FileInputStream(archivo);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            writer.append(LINE_FEED);
            writer.flush();
        }

        // Cierre del multipart
        writer.append("--").append(boundary).append("--").append(LINE_FEED);
        writer.close();

        // Leer respuesta del servidor
        int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK || status == HttpURLConnection.HTTP_CREATED) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            System.out.println("Respuesta del servidor: " + response);
        } else {
            System.err.println("Error al enviar. Código HTTP: " + status);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.err.println(line);
            }
            reader.close();
        }
        connection.disconnect();
    }
}
