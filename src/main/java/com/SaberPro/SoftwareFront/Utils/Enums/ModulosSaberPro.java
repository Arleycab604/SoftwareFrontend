package com.SaberPro.SoftwareFront.Utils.Enums;

public enum ModulosSaberPro {
    NONE,
    COMUNICACION_ESCRITA,
    LECTURA_CRITICA,
    FORMULACION_DE_PROYECTOS_DE_INGENIERIA,
    COMPETENCIAS_CIUDADANAS,
    INGLES,
    DISENO_DE_SOFTWARE,
    RAZONAMIENTO_CUANTITATIVO,
    PENSAMIENTO_CIENTIFICO_MATEMATICAS_Y_ESTADISTICA;

    @Override
    public String toString() {
        return name();
    }
}
