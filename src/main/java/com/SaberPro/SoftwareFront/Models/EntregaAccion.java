package com.SaberPro.SoftwareFront.Models;

import java.time.LocalDateTime;

public class EntregaAccion {
    private String direccionCorreo;
    private String nombreApellido;
    private String nombreUsuario;
    private String ciudad;
    private String estado;

    private LocalDateTime ultimaModificacionEntrega;
    private String nombreArchivo; // Added for file name
    private LocalDateTime fechaEnvioArchivo; // Added for file submission date
    private String comentariosEntrega;

    private String retroalimentacion; // Renamed from comentariosRetroalimentacion

    private String rutaArchivo; // Para guardar el path del archivo subido (opcional)

    public EntregaAccion(String direccionCorreo, String nombreApellido, String nombreUsuario,
                         String ciudad, String estado,
                         LocalDateTime ultimaModificacionEntrega, String nombreArchivo,
                         LocalDateTime fechaEnvioArchivo, String comentariosEntrega,
                         String retroalimentacion) {
        this.direccionCorreo = direccionCorreo;
        this.nombreApellido = nombreApellido;
        this.nombreUsuario = nombreUsuario;
        this.ciudad = ciudad;
        this.estado = estado;
        this.ultimaModificacionEntrega = ultimaModificacionEntrega;
        this.nombreArchivo = nombreArchivo;
        this.fechaEnvioArchivo = fechaEnvioArchivo;
        this.comentariosEntrega = comentariosEntrega;
        this.retroalimentacion = retroalimentacion;
    }

    // Getters y Setters
    public String getDireccionCorreo() {
        return direccionCorreo;
    }

    public void setDireccionCorreo(String direccionCorreo) {
        this.direccionCorreo = direccionCorreo;
    }

    public String getNombreApellido() {
        return nombreApellido;
    }

    public void setNombreApellido(String nombreApellido) {
        this.nombreApellido = nombreApellido;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getUltimaModificacionEntrega() {
        return ultimaModificacionEntrega;
    }

    public void setUltimaModificacionEntrega(LocalDateTime ultimaModificacionEntrega) {
        this.ultimaModificacionEntrega = ultimaModificacionEntrega;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public LocalDateTime getFechaEnvioArchivo() {
        return fechaEnvioArchivo;
    }

    public void setFechaEnvioArchivo(LocalDateTime fechaEnvioArchivo) {
        this.fechaEnvioArchivo = fechaEnvioArchivo;
    }

    public String getComentariosEntrega() {
        return comentariosEntrega;
    }

    public void setComentariosEntrega(String comentariosEntrega) {
        this.comentariosEntrega = comentariosEntrega;
    }

    public String getRetroalimentacion() {
        return retroalimentacion;
    }

    public void setRetroalimentacion(String retroalimentacion) {
        this.retroalimentacion = retroalimentacion;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
}