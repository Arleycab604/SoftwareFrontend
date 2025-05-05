package com.SaberPro.SoftwareFront.Utils;


import com.SaberPro.SoftwareFront.Models.InputQueryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class BuildRequest {

    public static Object getInstance() {
    }

    //FUNCION principal
    public HttpResponse<String> JsonPOST(String url, String json) throws IOException, InterruptedException {

        // Crear el cliente HTTP
        HttpClient httpClient = HttpClient.newHttpClient();
        // Convertir los filtros a JSON
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user:password".getBytes()))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // Enviar la solicitud y obtener la respuesta
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }


    public HttpResponse<String> POSTInputDTO(String url, InputQueryDTO filtros) throws IOException, InterruptedException {
        return JsonPOST(url,  new ObjectMapper().writeValueAsString(filtros));
    }
}
