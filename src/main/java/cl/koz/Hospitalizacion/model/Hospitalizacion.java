package cl.koz.Hospitalizacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Hospitalizacion")
public class Hospitalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cod_hospi;

    @Column(nullable = false)
    private int cantDias;

    @Column(nullable = false, length = 50)
    private String motivo;

    @Column(nullable = false)
    private Date fechaIngreso;

    @Column(nullable = false)
    private Date fechaSalida;

    @Column(nullable = false, length = 20)
    private String pacienteRun;

    @Column(name = "numero_habitaciones")
    private Long nroHabitacion;
}
