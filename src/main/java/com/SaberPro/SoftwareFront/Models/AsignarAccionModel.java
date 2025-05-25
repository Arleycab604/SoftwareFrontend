package com.SaberPro.SoftwareFront.Models;
import javafx.beans.property.*;
import javafx.collections.FXCollections; // <-- AÑADIDO
import javafx.collections.ObservableList; // <-- AÑADIDO

import java.io.File; // <-- AÑADIDO
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AsignarAccionModel{
    // Propiedades básicas
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty nombreTarea = new SimpleStringProperty();
    private final StringProperty descripcionGeneral = new SimpleStringProperty();
    private final StringProperty instruccionesActividad = new SimpleStringProperty();

    // Relación con el módulo (simplificada como StringProperty para el ejemplo)
    private final StringProperty moduloAsignado = new SimpleStringProperty();

    // Fechas importantes
    private final ObjectProperty<LocalDateTime> permitirEntregasDesde = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> fechaEntregaLimite = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> fechaLimiteFinal = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> recordarCalificarEn = new SimpleObjectProperty<>();

    // Configuración de archivos
    private final LongProperty tamanoMaximoArchivoMB = new SimpleLongProperty();
    private final StringProperty tiposArchivosAceptados = new SimpleStringProperty();
    private final IntegerProperty maxArchivosSubidos = new SimpleIntegerProperty(); // Número máximo de archivos permitidos

    // Propiedades para los checkboxes de tipos de entrega <-- AÑADIDAS
    private final BooleanProperty textoEnLinea = new SimpleBooleanProperty();
    private final BooleanProperty archivosEnviados = new SimpleBooleanProperty();

    // Propiedad para la lista de archivos adjuntos <-- AÑADIDA
    private final ListProperty<File> archivosAdjuntos = new SimpleListProperty<>(FXCollections.observableArrayList());


    // Estado y metadatos
    private final IntegerProperty anio = new SimpleIntegerProperty();
    private final StringProperty codigoAsignacion = new SimpleStringProperty();
    private final StringProperty estado = new SimpleStringProperty();

    // Lista de docentes (simplificada para el ejemplo)
    private final List<String> docentesAsignados = new ArrayList<>();

    public AsignarAccionModel() {
        // Valores por defecto
        this.tamanoMaximoArchivoMB.set(50);
        this.estado.set("Pendiente");
        this.anio.set(LocalDateTime.now().getYear());
        this.maxArchivosSubidos.set(1); // Valor por defecto, si no se especifica

        this.textoEnLinea.set(false); // Valor por defecto para el checkbox "Texto en línea" <-- AÑADIDO
        this.archivosEnviados.set(true); // Valor por defecto para el checkbox "Archivos enviados" <-- AÑADIDO
    }

    // --- Getters y Setters para las propiedades ---

    public String getId() { return id.get(); }
    public StringProperty idProperty() { return id; }
    public void setId(String id) { this.id.set(id); }

    public String getNombreTarea() { return nombreTarea.get(); }
    public StringProperty nombreTareaProperty() { return nombreTarea; }
    public void setNombreTarea(String nombreTarea) { this.nombreTarea.set(nombreTarea); }

    public String getDescripcionGeneral() { return descripcionGeneral.get(); }
    public StringProperty descripcionGeneralProperty() { return descripcionGeneral; }
    public void setDescripcionGeneral(String descripcionGeneral) { this.descripcionGeneral.set(descripcionGeneral); }

    public String getInstruccionesActividad() { return instruccionesActividad.get(); }
    public StringProperty instruccionesActividadProperty() { return instruccionesActividad; }
    public void setInstruccionesActividad(String instruccionesActividad) { this.instruccionesActividad.set(instruccionesActividad); }

    public String getModuloAsignado() { return moduloAsignado.get(); }
    public StringProperty moduloAsignadoProperty() { return moduloAsignado; }
    public void setModuloAsignado(String moduloAsignado) { this.moduloAsignado.set(moduloAsignado); }

    public LocalDateTime getPermitirEntregasDesde() { return permitirEntregasDesde.get(); }
    public ObjectProperty<LocalDateTime> permitirEntregasDesdeProperty() { return permitirEntregasDesde; }
    public void setPermitirEntregasDesde(LocalDateTime permitirEntregasDesde) { this.permitirEntregasDesde.set(permitirEntregasDesde); }

    public LocalDateTime getFechaEntregaLimite() { return fechaEntregaLimite.get(); }
    public ObjectProperty<LocalDateTime> fechaEntregaLimiteProperty() { return fechaEntregaLimite; }
    public void setFechaEntregaLimite(LocalDateTime fechaEntregaLimite) { this.fechaEntregaLimite.set(fechaEntregaLimite); }

    public LocalDateTime getFechaLimiteFinal() { return fechaLimiteFinal.get(); }
    public ObjectProperty<LocalDateTime> fechaLimiteFinalProperty() { return fechaLimiteFinal; }
    public void setFechaLimiteFinal(LocalDateTime fechaLimiteFinal) { this.fechaLimiteFinal.set(fechaLimiteFinal); }

    public LocalDateTime getRecordarCalificarEn() { return recordarCalificarEn.get(); }
    public ObjectProperty<LocalDateTime> recordarCalificarEnProperty() { return recordarCalificarEn; }
    public void setRecordarCalificarEn(LocalDateTime recordarCalificarEn) { this.recordarCalificarEn.set(recordarCalificarEn); }

    public long getTamanoMaximoArchivoMB() { return tamanoMaximoArchivoMB.get(); }
    public LongProperty tamanoMaximoArchivoMBProperty() { return tamanoMaximoArchivoMB; }
    public void setTamanoMaximoArchivoMB(long tamanoMaximoArchivoMB) { this.tamanoMaximoArchivoMB.set(tamanoMaximoArchivoMB); }

    public String getTiposArchivosAceptados() { return tiposArchivosAceptados.get(); }
    public StringProperty tiposArchivosAceptadosProperty() { return tiposArchivosAceptados; }
    public void setTiposArchivosAceptados(String tiposArchivosAceptados) { this.tiposArchivosAceptados.set(tiposArchivosAceptados); }

    public int getMaxArchivosSubidos() { return maxArchivosSubidos.get(); }
    public IntegerProperty maxArchivosSubidosProperty() { return maxArchivosSubidos; }
    public void setMaxArchivosSubidos(int maxArchivosSubidos) { this.maxArchivosSubidos.set(maxArchivosSubidos); }

    // --- Getters y Setters para las nuevas propiedades (Texto en línea, Archivos Enviados, Archivos Adjuntos) <-- AÑADIDOS
    public boolean isTextoEnLinea() {
        return textoEnLinea.get();
    }
    public BooleanProperty textoEnLineaProperty() {
        return textoEnLinea;
    }
    public void setTextoEnLinea(boolean textoEnLinea) {
        this.textoEnLinea.set(textoEnLinea);
    }

    public boolean isArchivosEnviados() {
        return archivosEnviados.get();
    }
    public BooleanProperty archivosEnviadosProperty() {
        return archivosEnviados;
    }
    public void setArchivosEnviados(boolean archivosEnviados) {
        this.archivosEnviados.set(archivosEnviados);
    }

    public ObservableList<File> getArchivosAdjuntos() {
        return archivosAdjuntos.get();
    }
    public ListProperty<File> archivosAdjuntosProperty() {
        return archivosAdjuntos;
    }
    public void setArchivosAdjuntos(ObservableList<File> archivosAdjuntos) {
        this.archivosAdjuntos.set(archivosAdjuntos);
    }
    // --- FIN de Getters y Setters de nuevas propiedades ---


    public List<String> getDocentesAsignados() { return docentesAsignados; }

    public int getAnio() { return anio.get(); }
    public IntegerProperty anioProperty() { return anio; }
    public void setAnio(int anio) { this.anio.set(anio); }

    public String getCodigoAsignacion() { return codigoAsignacion.get(); }
    public StringProperty codigoAsignacionProperty() { return codigoAsignacion; }
    public void setCodigoAsignacion(String codigoAsignacion) { this.codigoAsignacion.set(codigoAsignacion); }

    public String getEstado() { return estado.get(); }
    public StringProperty estadoProperty() { return estado; }
    public void setEstado(String estado) { this.estado.set(estado); }

    // Métodos para manejar docentes
    public void addDocente(String docente) {
        if (!docentesAsignados.contains(docente)) {
            docentesAsignados.add(docente);
        }
    }

    public void removeDocente(String docente) {
        docentesAsignados.remove(docente);
    }

    @Override
    public String toString() {
        return "AsignarAccionModel{" +
                "nombreTarea=" + nombreTarea.get() +
                ", moduloAsignado=" + moduloAsignado.get() +
                ", anio=" + anio.get() +
                ", estado=" + estado.get() +
                ", permitirEntregasDesde=" + (permitirEntregasDesde.get() != null ? permitirEntregasDesde.get().toString() : "N/A") +
                ", fechaEntregaLimite=" + (fechaEntregaLimite.get() != null ? fechaEntregaLimite.get().toString() : "N/A") +
                ", fechaLimiteFinal=" + (fechaLimiteFinal.get() != null ? fechaLimiteFinal.get().toString() : "N/A") +
                ", recordarCalificarEn=" + (recordarCalificarEn.get() != null ? recordarCalificarEn.get().toString() : "N/A") +
                ", tamanoMaximoArchivoMB=" + tamanoMaximoArchivoMB.get() +
                ", tiposArchivosAceptados=" + tiposArchivosAceptados.get() +
                ", maxArchivosSubidos=" + maxArchivosSubidos.get() +
                ", textoEnLinea=" + textoEnLinea.get() + // <-- AÑADIDO
                ", archivosEnviados=" + archivosEnviados.get() + // <-- AÑADIDO
                ", archivosAdjuntos=" + (archivosAdjuntos.get() != null ? archivosAdjuntos.get().size() + " files" : "N/A") + // <-- AÑADIDO
                ", docentesAsignados=" + docentesAsignados +
                '}';
    }
}

