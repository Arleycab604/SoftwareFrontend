package com.SaberPro.SoftwareFront.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteDTO {
    @JsonProperty("documento")
    private long documento;

    @JsonProperty("tipoDocumento")
    private String tipoDocumento;

    @JsonProperty("ciudad")
    private String ciudad;

    @JsonProperty("tipoDeEvaluado")
    private String tipoDeEvaluado;

    @JsonProperty("nombreUsuario")
    private String nombreUsuario;

    @JsonProperty("tipoDeUsuario")
    private String tipoDeUsuario;

    @JsonProperty("sniesId")
    private int sniesId;

    @JsonProperty("nombrePrograma")
    private String nombrePrograma;

    @JsonProperty("grupoDeReferencia")
    private String grupoDeReferencia;

    @JsonProperty("numeroRegistro")
    private String numeroRegistro;

    @JsonProperty("year")
    private int anio;

    @JsonProperty("periodo")
    private int periodo;

    @JsonProperty("puntajeGlobal")
    private int puntajeGlobal;

    @JsonProperty("percentilGlobal")
    private int percentilGlobal;

    @JsonProperty("novedades")
    private String novedades;

    @JsonProperty("tipoModulo")
    private String tipoModulo;

    @JsonProperty("puntajeModulo")
    private int puntajeModulo;

    @JsonProperty("nivelDesempeno")
    private String nivelDesempeno;

    @JsonProperty("percentilModulo")
    private int percentilModulo;

    public ReporteDTO() {}
}