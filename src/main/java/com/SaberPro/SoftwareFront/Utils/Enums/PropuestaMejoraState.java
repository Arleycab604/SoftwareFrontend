package com.SaberPro.SoftwareFront.Utils.Enums;

public enum PropuestaMejoraState {
    RECHAZADA,
    PENDIENTE,
    REQUIERE_CAMBIOS,
    ACEPTADA;

    @Override
    public String toString() {
        return name().replace('_', ' ');
    }
}
