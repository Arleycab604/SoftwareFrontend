package com.SaberPro.SoftwareFront.Utils;

import com.SaberPro.SoftwareFront.Models.InputQueryDTO;
import com.SaberPro.SoftwareFront.Utils.parsers.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class BuildRequest {

    private static BuildRequest instancia;

    private final HttpClient httpClient;

    // Constructor privado para evitar instanciación externa
    private BuildRequest() {
        this.httpClient = HttpClient.newHttpClient();
    }

    // Método estático para obtener la instancia única
    public static synchronized BuildRequest getInstance() {
        if (instancia == null) {
            instancia = new BuildRequest();
        }
        return instancia;
    }

    // Métodos HTTP
    public HttpResponse<String> POSTJson(String url, String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user:password".getBytes()))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> uploadFile(String url, File file, Map<String, String> params) throws IOException, InterruptedException {
        // Crear el límite (boundary) para la solicitud multipart
        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();

        // Construir el cuerpo multipart
        StringBuilder bodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            bodyBuilder.append("--").append(boundary).append("\r\n")
                    .append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"\r\n\r\n")
                    .append(entry.getValue()).append("\r\n");
        }
        bodyBuilder.append("--").append(boundary).append("\r\n")
                .append("Content-Disposition: form-data; name=\"files\"; filename=\"").append(file.getName()).append("\"\r\n")
                .append("Content-Type: application/octet-stream\r\n\r\n");

        // Leer el contenido del archivo
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        byte[] bodyBytes = bodyBuilder.toString().getBytes(StandardCharsets.UTF_8);
        byte[] endBoundaryBytes = ("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8);

        // Combinar el cuerpo y el archivo en un solo array de bytes
        byte[] finalBody = new byte[bodyBytes.length + fileBytes.length + endBoundaryBytes.length];
        System.arraycopy(bodyBytes, 0, finalBody, 0, bodyBytes.length);
        System.arraycopy(fileBytes, 0, finalBody, bodyBytes.length, fileBytes.length);
        System.arraycopy(endBoundaryBytes, 0, finalBody, bodyBytes.length + fileBytes.length, endBoundaryBytes.length);

        // Crear la solicitud HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user:password".getBytes()))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(finalBody))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> GETParams(String url, Map<String, String> params) throws IOException, InterruptedException {
        String paramString = params.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "="
                        + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "?" + paramString))
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user:password".getBytes()))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> PUTParams(String url, Map<String, String> params) throws IOException, InterruptedException {
// Construir la URL con parámetros codificados
        StringJoiner paramJoiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramJoiner.add(
                    URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                            URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8)
            );
        }
        String fullUrl = url + "?" + paramJoiner.toString();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user:password".getBytes()))
                .PUT(HttpRequest.BodyPublishers.noBody())  // PUT sin body
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
    public HttpResponse<String> POSTInputDTO(String url, InputQueryDTO filtros) throws IOException, InterruptedException {
        return POSTJson(url, new ObjectMapper().writeValueAsString(filtros));
    }


    public HttpResponse<String> GETJson(String url, String json) throws IOException, InterruptedException {
        String queryParams = JsonParser.toQueryParams(json);
        String fullUrl = url + "?" + queryParams;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user:password".getBytes()))
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
