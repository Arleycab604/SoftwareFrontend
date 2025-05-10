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
    private Double mediaPeriodoMin;
    private Double mediaPeriodoMax;
    private Double coefVarPeriodoMin;
    private Double coefVarPeriodoMax;
    private Double mediaModuloMin;
    private Double mediaModuloMax;
    private Double coefVarModuloMin;
    private Double coefVarModuloMax;
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