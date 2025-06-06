package com.SaberPro.SoftwareFront.Utils;

import com.SaberPro.SoftwareFront.Models.InputQueryDTO;
import com.SaberPro.SoftwareFront.Utils.Enums.MethodRequest;
import com.SaberPro.SoftwareFront.Utils.parsers.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class BuildRequest {

    private static BuildRequest instancia;

    private final ObjectMapper objectMapper;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Singleton glorificado
    private final HttpClient httpClient;
    private BuildRequest() {
        this.httpClient = HttpClient.newHttpClient();

        // Configuración del ObjectMapper
        this.objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));

        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }


    private static HttpRequest build(
            String url,
            MethodRequest method,
            Map<String, String> headers,
            HttpRequest.BodyPublisher bodyPublisher,
            boolean requiresAuthToken) {

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url));
        if(requiresAuthToken) {
            if(TokenManager.getToken() != null) {
                builder.header("Authorization", "Bearer " + TokenManager.getToken());
            } else {//Error
                System.out.println("Error: token no definido. Verifica si iniciaste sesion.");
                return null;
            }
        }
        // Headers adicionales
        if (headers != null) {
            headers.forEach(builder::header);
        }
        switch (method) {
            case POST:
                builder.POST(bodyPublisher !=null  ? bodyPublisher : HttpRequest.BodyPublishers.noBody());
                break;
            case PUT:
                builder.PUT(bodyPublisher != null ? bodyPublisher : HttpRequest.BodyPublishers.noBody());
                break;
            case DELETE:
                builder.DELETE();
                break;
            case GET:
                builder.GET();
                break;
            default:
                throw new IllegalArgumentException("Método HTTP no soportado: " + method);
        }
        return builder.build();
    }

    private static HttpRequest build(
            String url,
            MethodRequest method,
            Map<String, String> headers,
            HttpRequest.BodyPublisher bodyPublisher) {
        return build(url, method, headers, bodyPublisher, true);
    }

    private static BuildRequest instance;
    public static BuildRequest getInstance() {
        if(instance == null){
            instance = new BuildRequest();
        }
        return instance;
    }

    // Métodos HTTP
    public HttpResponse<String> POSTJson(String url, String json) throws IOException, InterruptedException {
        return POSTJson(url,json, true);
    }
    public HttpResponse<String> POSTJson(String url, String json,boolean requiresAuthToken) throws IOException, InterruptedException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        HttpRequest request = build(
                url,
                MethodRequest.POST,
                headers,
                HttpRequest.BodyPublishers.ofString(json),requiresAuthToken);

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> uploadFileOld(String url, File file, Map<String, String> params) throws IOException, InterruptedException {
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
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "multipart/form-data; boundary=" + boundary);

        HttpRequest request = build(url, MethodRequest.POST, headers, HttpRequest.BodyPublishers.ofByteArray(finalBody));

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
    public HttpResponse<String> uploadFile(String url, File file, Map<String, String> params)
            throws IOException, InterruptedException {

        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
        String lineSeparator = "\r\n";

        // Detectar tipo MIME del archivo
        String mimeType = Files.probeContentType(file.toPath());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        // Preparar flujo para construir el cuerpo de la solicitud
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        // Añadir campos del formulario
        for (Map.Entry<String, String> entry : params.entrySet()) {
            writer.write("--" + boundary + lineSeparator);
            writer.write("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + lineSeparator);
            writer.write(lineSeparator);
            writer.write(entry.getValue() + lineSeparator);
        }
        // Añadir archivo
        writer.write("--" + boundary + lineSeparator);
        writer.write("Content-Disposition: form-data; name=\"" + "files"+ "\"; filename=\"" + file.getName() + "\"" + lineSeparator);
        writer.write("Content-Type: " + mimeType + lineSeparator);
        writer.write(lineSeparator);
        writer.flush(); // Asegurarse de que el texto está escrito antes de añadir binario

        outputStream.write(Files.readAllBytes(file.toPath())); // Archivo binario
        outputStream.write(lineSeparator.getBytes(StandardCharsets.UTF_8));

        // Cerrar la solicitud multipart
        writer.write("--" + boundary + "--" + lineSeparator);
        writer.flush();

        byte[] finalBody = outputStream.toByteArray();

        // Construir headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "multipart/form-data; boundary=" + boundary);

        HttpRequest request = build(url,
                MethodRequest.POST, headers,
                HttpRequest.BodyPublishers.ofByteArray(finalBody));

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
    public HttpResponse<String> GETParams(String url, Map<String, String> params) throws IOException, InterruptedException {
        return GETParams(url,params,true);
    }

    public HttpResponse<String> GETParams(String url, Map<String, String> params, boolean AuthTokenRequiered) throws IOException, InterruptedException {

        //JsonParser tambien parcea Map<String, String> :)

        HttpRequest request ;
        if(params != null){
            String paramString= JsonParser.toQueryParams(params);
             request = build(url + "?" + paramString,
                    MethodRequest.GET, null, null, AuthTokenRequiered);
        }else{
             request = build(url,
                    MethodRequest.GET, null, null, AuthTokenRequiered);
        }

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
        HttpRequest request = build(url + "?" + paramJoiner,
                MethodRequest.PUT, null, null);

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> POSTInputDTO(String url, InputQueryDTO filtros) throws IOException, InterruptedException {
        return POSTJson(url, new ObjectMapper().writeValueAsString(filtros), true);
    }


    public HttpResponse<String> GETJson(String url, String json) throws IOException, InterruptedException {
        String queryParams = JsonParser.toQueryParams(json);
        String fullUrl = url + "?" + queryParams;
        HttpRequest request = build(
                fullUrl,
                MethodRequest.GET, null, null);

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }


    public HttpResponse<String> POSTMultipartWithFiles(String url, Map<String, String> fields, List<File> files)
            throws IOException, InterruptedException {

        String boundary = "===" + System.currentTimeMillis() + "===";
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(byteStream, StandardCharsets.UTF_8), true);

        // Agregar campos de texto
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"\r\n\r\n");
            writer.append(entry.getValue()).append("\r\n");
            writer.flush();
        }

        // Agregar archivos
        for (File file : files) {
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"archivos\"; filename=\"").append(file.getName()).append("\"\r\n");
            writer.append("Content-Type: ").append(mimeType).append("\r\n\r\n");
            writer.flush();

            // Copiar contenido del archivo directamente al byteStream
            Files.copy(file.toPath(), byteStream);
            byteStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
            writer.flush();
        }

        // Fin del multipart/form-data
        writer.append("--").append(boundary).append("--").append("\r\n");
        writer.close();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(byteStream.toByteArray()))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }


}
