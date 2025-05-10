package com.SaberPro.SoftwareFront.Models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class ReporteYearDTO {
    private int year;
    private int periodo;

    private double mediaPeriodo;
    private double varianzaPeriodo;
    private double coefVarPeriodo;
    private String tipoModulo;
    private double mediaModulo;
    private double varianzaModulo;
    private double coefVarModulo;

}