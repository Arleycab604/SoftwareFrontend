package com.SaberPro.SoftwareFront.Utils;


import lombok.Getter;
public class TokenManager {

    private static String token;
    private static String tipoUsuario;
    private static String nombreUsuario;

    public static String getNombreUsuario() {
        return nombreUsuario;
    }
    public static void setToken(String token) {
        TokenManager.token = token;
    }

    public static void setTipoUsuario(String tipoUsuario) {
        TokenManager.tipoUsuario = tipoUsuario;
    }
    public static String getToken() {
        return token;
    }

    public static void setNombreUsuario(String nombreUsuario) {
        TokenManager.nombreUsuario = nombreUsuario;
    }

    public static String getTipoUsuario() {
        return tipoUsuario;
    }
}