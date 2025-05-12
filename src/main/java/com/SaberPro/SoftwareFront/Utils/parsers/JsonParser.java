package com.SaberPro.SoftwareFront.Utils.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonParser {

    public static String toQueryParams(String json) throws IOException {
        Map<String, String> params = new ObjectMapper().readValue(json, new TypeReference<>() {});
        return toQueryParams(params);
    }
    public static String toQueryParams(Map<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }
    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
