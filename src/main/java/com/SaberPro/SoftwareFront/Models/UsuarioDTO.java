package com.SaberPro.SoftwareFront.Models;

import javafx.beans.property.SimpleStringProperty;

public class UsuarioDTO {
    private final SimpleStringProperty nombreUsuario;
    private final SimpleStringProperty tipoDeUsuario;
    private final SimpleStringProperty correo;

    public UsuarioDTO(String nombreUsuario, String tipoDeUsuario, String correo) {
        this.nombreUsuario = new SimpleStringProperty(nombreUsuario);
        this.tipoDeUsuario = new SimpleStringProperty(tipoDeUsuario);
        this.correo = new SimpleStringProperty(correo);
    }

    public String getNombreUsuario() {
        return nombreUsuario.get();
    }

    public String getTipoDeUsuario() {
        return tipoDeUsuario.get();
    }

    public String getCorreo() {
        return correo.get();
    }
}