package com.SaberPro.SoftwareFront.Models.accionMejora;

import com.SaberPro.SoftwareFront.Utils.Enums.ModulosSaberPro;
import com.SaberPro.SoftwareFront.Utils.Enums.PropuestaMejoraState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.hc.client5.http.entity.mime.MultipartPart;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Data
@RequiredArgsConstructor
public class PropuestaMejoraDTO {

    private long idPropuestaMejora;
    private String nombrePropuesta;

    private String usuarioProponente;

    private PropuestaMejoraState estadoPropuesta;

    private String descripcion;
    private String[] archivos;
    private List<String> urlsDocumentoDetalles = new ArrayList<>();

    private ModulosSaberPro moduloPropuesta;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCreacion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaLimiteEntrega;

}
