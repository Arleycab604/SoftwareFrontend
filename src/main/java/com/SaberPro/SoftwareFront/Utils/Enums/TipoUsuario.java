package com.SaberPro.SoftwareFront.Utils.Enums;

public enum TipoUsuario {
    ESTUDIANTE,
    DOCENTE,
    OFICINA_DE_ACREDITACION,
    COMITE_DE_PROGRAMA,
    COORDINADOR_SABER_PRO,
    DIRECTOR_DE_PROGRAMA,
    DIRECTOR_DE_ESCUELA,
    DECANATURA;

    @Override
    public String toString() {
        return name().replace('_', ' ');
    }
}
