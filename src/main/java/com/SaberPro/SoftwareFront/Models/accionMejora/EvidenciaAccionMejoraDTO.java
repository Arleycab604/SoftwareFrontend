package com.SaberPro.SoftwareFront.Models.accionMejora;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Data
public class EvidenciaAccionMejoraDTO {

    private String nombreDocente;
    private Long idPropuestaMejora;
    private String fechaEntrega;
    private String archivos;
    private List<String> urlsEvidencias;
}
