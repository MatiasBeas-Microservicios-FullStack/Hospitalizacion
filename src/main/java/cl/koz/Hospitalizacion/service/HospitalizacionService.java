package cl.koz.Hospitalizacion.service;

import cl.koz.Hospitalizacion.DTO.HospitalizacionRequestDTO;
import cl.koz.Hospitalizacion.DTO.HospitalizacionResponseDTO;
import cl.koz.Hospitalizacion.model.Hospitalizacion;
import cl.koz.Hospitalizacion.repository.HospitalizacionRepository;
import cl.koz.Hospitalizacion.webClient.HabitacionesClient;
import cl.koz.Hospitalizacion.webClient.PacienteClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class HospitalizacionService {
    private final HospitalizacionRepository hospitalizacionRepository;
    private final PacienteClient pacienteClient;
    private final HabitacionesClient habitacionesClient;

    private HospitalizacionResponseDTO mapToDTO(Hospitalizacion hospitalizacion){
        return new HospitalizacionResponseDTO(
                hospitalizacion.getCod_hospi(),
                hospitalizacion.getCantDias(),
                hospitalizacion.getMotivo(),
                hospitalizacion.getFechaIngreso(),
                hospitalizacion.getFechaSalida(),
                hospitalizacion.getPacienteRun(),
                hospitalizacion.getNroHabitacion()
        );
    }
    public List<HospitalizacionResponseDTO> obtenerTodo(){
        return hospitalizacionRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<HospitalizacionResponseDTO> obtenerPorId(Long id){
        return hospitalizacionRepository.findById(id).map(this::mapToDTO);
    }

    public HospitalizacionResponseDTO guardar (HospitalizacionRequestDTO dto){

        if (!pacienteClient.existePaciente(dto.getPacienteRun())) {
            throw new RuntimeException("El paciente con RUN '"+ dto.getPacienteRun() + "' no existe en el sistema.");
        }
        if (!habitacionesClient.existeHabitacion(dto.getNroHabitacion())) {
            throw new RuntimeException("La habitación N°"+ dto.getNroHabitacion() + " no existe en el sistema.");
        }
        Hospitalizacion hospitalizacion = new Hospitalizacion(
                null,
                dto.getCantDias(),
                dto.getMotivo(),
                dto.getFechaIngreso(),
                dto.getFechaSalida(),
                dto.getPacienteRun(),
                dto.getNroHabitacion()
        );
        return mapToDTO(hospitalizacionRepository.save(hospitalizacion));
    }

    public Optional<HospitalizacionResponseDTO> actualizar (Long id, HospitalizacionRequestDTO dto){
        return hospitalizacionRepository.findById(id).map(existente ->{
            if (dto.getPacienteRun() != null && !dto.getPacienteRun().equals(existente.getPacienteRun())) {
                throw new RuntimeException("No está permitido modificar el RUN del paciente...");
            }
            if (dto.getNroHabitacion() != null &&!dto.getNroHabitacion().equals(existente.getNroHabitacion())) {
                throw new RuntimeException("No está permitido modificar el número de habitación...");
            }
            existente.setCantDias(dto.getCantDias());
            existente.setMotivo(dto.getMotivo());
            existente.setFechaIngreso(dto.getFechaIngreso());
            existente.setFechaSalida(dto.getFechaSalida());
            return mapToDTO(hospitalizacionRepository.save(existente));
        });
    }
    public void eliminar(Long id){
        hospitalizacionRepository.deleteById(id);
    }
}

