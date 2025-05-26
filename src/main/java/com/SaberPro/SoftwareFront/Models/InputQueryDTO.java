package com.SaberPro.SoftwareFront.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//Maneja un solo reporte con un solo modulo con su puntaje general
//Igual a como sale en el excel
@Getter
@Setter

@RequiredArgsConstructor
// by Puntaje minimo, maximo, y por puntaje,
// By year periodo, year, periodo
public class InputQueryDTO {
    @JsonProperty("year")
    private Integer year;
    @JsonProperty("periodo")
    private Integer periodo;

    @JsonProperty("nombreUsuario") // nombre estudiante
    private String nombreUsuario;
    @JsonProperty("documento")
    private String documento;
    @JsonProperty("nombrePrograma") // Por ahora solo hay un programa :v
    private String nombrePrograma;

    // Pos funciona
    @JsonProperty("puntajeGlobalMinimo")
    private Integer puntajeGlobalMinimo = 0;
    @JsonProperty("puntajeGlobalMaximo")
    private Integer puntajeGlobalMaximo = 300;

    // Pray to god que funcione
    @JsonProperty("tipoModulo")
    private String tipoModulo;

    @JsonProperty("puntajeModuloMinimo")
    private Integer puntajeModuloMinimo = 0;
    @JsonProperty("puntajeModuloMaximo")
    private Integer puntajeModuloMaximo = 300;

    // Dificil de implementar
    @JsonProperty("nivelDesempeno")
    private String nivelDesempeno;

    @Override
    public String toString() {
        return "InputQueryDTO{" +
                "year=" + year +
                ", periodo=" + periodo +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", nombrePrograma='" + nombrePrograma + '\'' +
                ", puntajeGlobalMinimo=" + puntajeGlobalMinimo +
                ", puntajeGlobalMaximo=" + puntajeGlobalMaximo +
                ", tipoModulo='" + tipoModulo + '\'' +
                ", puntajeModuloMinimo=" + puntajeModuloMinimo +
                ", puntajeModuloMaximo=" + puntajeModuloMaximo +
                ", nivelDesempeno='" + nivelDesempeno + '\'' +
                '}';
    }
}
