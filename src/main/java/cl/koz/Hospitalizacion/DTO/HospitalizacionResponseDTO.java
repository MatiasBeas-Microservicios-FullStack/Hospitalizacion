package cl.koz.Hospitalizacion.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class HospitalizacionResponseDTO {

    private Long cod_hospi;

    private int cantDias;
    private String motivo;
    private Date fechaIngreso;
    private Date fechaSalida;

    private String pacienteRun;
    private Long nroHabitacion;
}
