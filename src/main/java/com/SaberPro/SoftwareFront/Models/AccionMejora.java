package com.SaberPro.SoftwareFront.Models;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

public class AccionMejora {
    private String id, nombreTarea, descripcionGeneral, instruccionesActividad, moduloAsignadoId, moduloAsignadoNombre, periodoAcademico,tiposArchivosAceptados,codigoAsignacion,estado;
    private LocalDateTime permitirEntregasDesde,fechaEntregaLimite,fechaLimiteFinal,recordarCalificarEn;
    private int maxArchivosSubidos, anio;
    private long tamanoMaximoArchivoMB;
    private boolean textoEnLinea,archivosEnviados;
    private List<File> archivosAdjuntos;

    // Constructor completo
    public AccionMejora(String id, String periodoAcademico, String codigoAsignacion, String estado, AsignarAccionModel model) {
        this.id = id;
        this.periodoAcademico = periodoAcademico;
        this.codigoAsignacion = codigoAsignacion;
        this.estado = estado;
        this.nombreTarea = model.getNombreTarea();
        this.descripcionGeneral = model.getDescripcionGeneral();
        this.instruccionesActividad = model.getInstruccionesActividad();
        this.moduloAsignadoNombre = model.getModuloAsignado();

        this.permitirEntregasDesde = model.getPermitirEntregasDesde();
        this.fechaEntregaLimite = model.getFechaEntregaLimite();
        this.fechaLimiteFinal = model.getFechaLimiteFinal();
        this.recordarCalificarEn = model.getRecordarCalificarEn();

        this.maxArchivosSubidos = model.getMaxArchivosSubidos();
        this.tamanoMaximoArchivoMB = model.getTamanoMaximoArchivoMB();
        this.tiposArchivosAceptados = model.getTiposArchivosAceptados();
        this.anio = model.getAnio();

        this.textoEnLinea = model.isTextoEnLinea();
        this.archivosEnviados = model.isArchivosEnviados();

        this.archivosAdjuntos = new ArrayList<>(model.getArchivosAdjuntos());
    }

    // --- Getters y Setters (Necesarios para que los controladores accedan a los datos) ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombreTarea() { return nombreTarea; }
    public void setNombreTarea(String nombreTarea) { this.nombreTarea = nombreTarea; }
    public String getDescripcionGeneral() { return descripcionGeneral; }
    public void setDescripcionGeneral(String descripcionGeneral) { this.descripcionGeneral = descripcionGeneral; }
    public String getInstruccionesActividad() { return instruccionesActividad; }
    public void setInstruccionesActividad(String instruccionesActividad) { this.instruccionesActividad = instruccionesActividad; }
    public String getModuloAsignadoId() { return moduloAsignadoId; }
    public void setModuloAsignadoId(String moduloAsignadoId) { this.moduloAsignadoId = moduloAsignadoId; }
    public String getModuloAsignadoNombre() { return moduloAsignadoNombre; }
    public void setModuloAsignadoNombre(String moduloAsignadoNombre) { this.moduloAsignadoNombre = moduloAsignadoNombre; }
    public String getPeriodoAcademico() { return periodoAcademico; }
    public void setPeriodoAcademico(String periodoAcademico) { this.periodoAcademico = periodoAcademico; }
    public LocalDateTime getPermitirEntregasDesde() { return permitirEntregasDesde; }
    public void setPermitirEntregasDesde(LocalDateTime permitirEntregasDesde) { this.permitirEntregasDesde = permitirEntregasDesde; }
    public LocalDateTime getFechaEntregaLimite() { return fechaEntregaLimite; }
    public void setFechaEntregaLimite(LocalDateTime fechaEntregaLimite) { this.fechaEntregaLimite = fechaEntregaLimite; }
    public LocalDateTime getFechaLimiteFinal() { return fechaLimiteFinal; }
    public void setFechaLimiteFinal(LocalDateTime fechaLimiteFinal) { this.fechaLimiteFinal = fechaLimiteFinal; }
    public LocalDateTime getRecordarCalificarEn() { return recordarCalificarEn; }
    public void setRecordarCalificarEn(LocalDateTime recordarCalificarEn) { this.recordarCalificarEn = recordarCalificarEn; }
    public int getMaxArchivosSubidos() { return maxArchivosSubidos; }
    public void setMaxArchivosSubidos(int maxArchivosSubidos) { this.maxArchivosSubidos = maxArchivosSubidos; }
    public long getTamanoMaximoArchivoMB() { return tamanoMaximoArchivoMB; }
    public void setTamanoMaximoArchivoMB(long tamanoMaximoArchivoMB) { this.tamanoMaximoArchivoMB = tamanoMaximoArchivoMB; }
    public String getTiposArchivosAceptados() { return tiposArchivosAceptados; }
    public void setTiposArchivosAceptados(String tiposArchivosAceptados) { this.tiposArchivosAceptados = tiposArchivosAceptados; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public String getCodigoAsignacion() { return codigoAsignacion; }
    public void setCodigoAsignacion(String codigoAsignacion) { this.codigoAsignacion = codigoAsignacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // Getters y Setters para las propiedades de entrega
    public boolean isTextoEnLinea() {
        return textoEnLinea;
    }

    public void setTextoEnLinea(boolean textoEnLinea) {
        this.textoEnLinea = textoEnLinea;
    }

    public boolean isArchivosEnviados() {
        return archivosEnviados;
    }

    public void setArchivosEnviados(boolean archivosEnviados) {
        this.archivosEnviados = archivosEnviados;
    }

    public List<File> getArchivosAdjuntos() {
        return archivosAdjuntos;
    }

    public void setArchivosAdjuntos(List<File> archivosAdjuntos) {
        this.archivosAdjuntos = archivosAdjuntos;
    }

    // Puedes añadir un método para obtener un String formateado de las fechas si lo necesitas
    public String getFormatoFechaHora(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        // Ejemplo de formato: "viernes, 21 de marzo de 2025, 08:00"
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy, HH:mm", new java.util.Locale("es", "ES"));
        return dateTime.format(formatter);
    }
}