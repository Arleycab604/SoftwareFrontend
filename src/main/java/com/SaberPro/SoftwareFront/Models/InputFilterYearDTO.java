package com.SaberPro.SoftwareFront.Models;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Data
public class InputFilterYearDTO {
    private int year;
    private int periodo;
    private Double mediaPeriodoMin = 0.0;
    private Double mediaPeriodoMax = 1.0;
    private Double coefVarPeriodoMin = 0.0;
    private Double coefVarPeriodoMax = 1.0;
    private Double mediaModuloMin = 0.0;
    private Double mediaModuloMax = 300.0;
    private Double coefVarModuloMin = 0.0;
    private Double coefVarModuloMax = 1.0;
    private String tipoModulo; // Comas separadas

    public String toString(){
        return "InputFilterYearDTO{" +
                "year=" + year +
                ", periodo=" + periodo +
                "mediaPeriodoMin=" + mediaPeriodoMin +
                ", mediaPeriodoMax=" + mediaPeriodoMax +
                ", coefVarPeriodoMin=" + coefVarPeriodoMin +
                ", coefVarPeriodoMax=" + coefVarPeriodoMax +
                ", mediaModuloMin=" + mediaModuloMin +
                ", mediaModuloMax=" + mediaModuloMax +
                ", coefVarModuloMin=" + coefVarModuloMin +
                ", coefVarModuloMax=" + coefVarModuloMax +
                ", tipoModulo='" + tipoModulo + '\'' +
                '}';
    }
}