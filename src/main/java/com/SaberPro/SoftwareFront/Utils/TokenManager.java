package com.SaberPro.SoftwareFront.Utils;


public class TokenManager {
    private static String token;
    private static String tipoUsuario;

    public static void setToken(String token) {
        TokenManager.token = token;
    }
    public static String getToken() {
        return token;
    }

    public static void setTipoUsuario(String tipoUsuario) {
        TokenManager.tipoUsuario = tipoUsuario;
    }

    public static String getTipoUsuario() {
        return tipoUsuario;
    }
}