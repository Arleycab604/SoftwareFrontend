package com.SaberPro.SoftwareFront.Utils;

import com.SaberPro.SoftwareFront.Models.InputQueryDTO;
import com.SaberPro.SoftwareFront.Utils.parsers.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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
}
