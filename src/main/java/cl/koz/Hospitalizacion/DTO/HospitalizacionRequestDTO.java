package cl.koz.Hospitalizacion.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class HospitalizacionRequestDTO {

    @Positive(message = "La cantidad de días debe ser mayor a 0")
    @NotNull(message = "es OBLIGATORIO cuantos dias va estar hospitalizado/a")
    private int cantDias;

    @NotBlank(message = "El motivo de la hospitalizacion es OBLIGATORIA!!!")
    private String motivo;

    @NotNull(message = "la fecha de ingreso es OBlIGATORIA")
    private Date fechaIngreso;

    @NotNull(message = "la fecha de salida es OBlIGATORIA")
    private Date fechaSalida;

    @NotBlank(message = "el rut del paciente es OBLIGATORIO")
    private String pacienteRun;

    @NotNull(message = "El número de habitación es OBLIGATORIO")
    private Long nroHabitacion;

}
