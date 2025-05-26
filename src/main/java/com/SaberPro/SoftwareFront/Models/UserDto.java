package com.SaberPro.SoftwareFront.Models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserDto {
    private String nombreUsuario;
    private String tipoDeUsuario;
    private String correo;
    private String documento;
}
