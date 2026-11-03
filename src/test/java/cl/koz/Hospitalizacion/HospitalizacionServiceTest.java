package cl.koz.Hospitalizacion;

import cl.koz.Hospitalizacion.DTO.HospitalizacionRequestDTO;
import cl.koz.Hospitalizacion.DTO.HospitalizacionResponseDTO;
import cl.koz.Hospitalizacion.model.Hospitalizacion;
import cl.koz.Hospitalizacion.repository.HospitalizacionRepository;
import cl.koz.Hospitalizacion.security.JwtAuthFilter;
import cl.koz.Hospitalizacion.security.JwtService;
import cl.koz.Hospitalizacion.service.HospitalizacionService;
import cl.koz.Hospitalizacion.webClient.HabitacionesClient;
import cl.koz.Hospitalizacion.webClient.PacienteClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HospitalizacionServiceTest {

    @Mock
    private HospitalizacionRepository hospitalizacionRepository;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private HabitacionesClient habitacionesClient;

    @InjectMocks
    private HospitalizacionService hospitalizacionService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private JwtService jwtService;

    private Hospitalizacion hospitalizacion;
    private HospitalizacionRequestDTO requestDTO;
    private Date fechaIngreso;
    private Date fechaSalida;

    @BeforeEach
    void setUp() {
        fechaIngreso = new Date();
        fechaSalida = new Date(fechaIngreso.getTime() + 86400000L * 5);

        hospitalizacion = new Hospitalizacion(
                1L, 5, "Cirugía", fechaIngreso, fechaSalida, "12345678-9", 101L
        );

        requestDTO = new HospitalizacionRequestDTO(
                5, "Cirugía", fechaIngreso, fechaSalida, "12345678-9", 101L
        );
    }

    // ─── obtenerTodo ────────────────────────────────────────────────────────

    @Test
    void obtenerTodo_debeRetornarListaDeHospitalizaciones() {
        when(hospitalizacionRepository.findAll()).thenReturn(List.of(hospitalizacion));

        List<HospitalizacionResponseDTO> resultado = hospitalizacionService.obtenerTodo();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCod_hospi()).isEqualTo(1L);
        assertThat(resultado.get(0).getMotivo()).isEqualTo("Cirugía");
        verify(hospitalizacionRepository, times(1)).findAll();
    }

    @Test
    void obtenerTodo_debeRetornarListaVaciaSiNoHayDatos() {
        when(hospitalizacionRepository.findAll()).thenReturn(List.of());

        List<HospitalizacionResponseDTO> resultado = hospitalizacionService.obtenerTodo();

        assertThat(resultado).isEmpty();
    }

    // ─── obtenerPorId ───────────────────────────────────────────────────────

    @Test
    void obtenerPorId_debeRetornarHospitalizacionSiExiste() {
        when(hospitalizacionRepository.findById(1L)).thenReturn(Optional.of(hospitalizacion));

        Optional<HospitalizacionResponseDTO> resultado = hospitalizacionService.obtenerPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCod_hospi()).isEqualTo(1L);
        assertThat(resultado.get().getPacienteRun()).isEqualTo("12345678-9");
    }

    @Test
    void obtenerPorId_debeRetornarVacioSiNoExiste() {
        when(hospitalizacionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<HospitalizacionResponseDTO> resultado = hospitalizacionService.obtenerPorId(99L);

        assertThat(resultado).isEmpty();
    }

    // ─── guardar ────────────────────────────────────────────────────────────

    @Test
    void guardar_debeGuardarCuandoPacienteYHabitacionExisten() {
        when(pacienteClient.existePaciente("12345678-9")).thenReturn(true);
        when(habitacionesClient.existeHabitacion(101L)).thenReturn(true);
        when(hospitalizacionRepository.save(any(Hospitalizacion.class))).thenReturn(hospitalizacion);

        HospitalizacionResponseDTO resultado = hospitalizacionService.guardar(requestDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getCod_hospi()).isEqualTo(1L);
        assertThat(resultado.getMotivo()).isEqualTo("Cirugía");
        verify(hospitalizacionRepository, times(1)).save(any(Hospitalizacion.class));
    }

    @Test
    void guardar_debeLanzarExcepcionSiPacienteNoExiste() {
        when(pacienteClient.existePaciente("12345678-9")).thenReturn(false);

        assertThatThrownBy(() -> hospitalizacionService.guardar(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("12345678-9");

        verify(hospitalizacionRepository, never()).save(any());
    }

    @Test
    void guardar_debeLanzarExcepcionSiHabitacionNoExiste() {
        when(pacienteClient.existePaciente("12345678-9")).thenReturn(true);
        when(habitacionesClient.existeHabitacion(101L)).thenReturn(false);

        assertThatThrownBy(() -> hospitalizacionService.guardar(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("101");

        verify(hospitalizacionRepository, never()).save(any());
    }

    // ─── actualizar ─────────────────────────────────────────────────────────

    @Test
    void actualizar_debeActualizarCamposPermitidos() {
        HospitalizacionRequestDTO dtoActualizar = new HospitalizacionRequestDTO(
                10, "Recuperación", fechaIngreso, fechaSalida, "12345678-9", 101L
        );
        Hospitalizacion actualizado = new Hospitalizacion(
                1L, 10, "Recuperación", fechaIngreso, fechaSalida, "12345678-9", 101L
        );

        when(hospitalizacionRepository.findById(1L)).thenReturn(Optional.of(hospitalizacion));
        when(hospitalizacionRepository.save(any(Hospitalizacion.class))).thenReturn(actualizado);

        Optional<HospitalizacionResponseDTO> resultado = hospitalizacionService.actualizar(1L, dtoActualizar);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCantDias()).isEqualTo(10);
        assertThat(resultado.get().getMotivo()).isEqualTo("Recuperación");
    }

    @Test
    void actualizar_debeLanzarExcepcionSiCambiaRun() {
        HospitalizacionRequestDTO dtoRunDistinto = new HospitalizacionRequestDTO(
                5, "Cirugía", fechaIngreso, fechaSalida, "99999999-9", 101L
        );

        when(hospitalizacionRepository.findById(1L)).thenReturn(Optional.of(hospitalizacion));

        assertThatThrownBy(() -> hospitalizacionService.actualizar(1L, dtoRunDistinto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("RUN");
    }

    @Test
    void actualizar_debeLanzarExcepcionSiCambiaHabitacion() {
        HospitalizacionRequestDTO dtoHabitacionDistinta = new HospitalizacionRequestDTO(
                5, "Cirugía", fechaIngreso, fechaSalida, "12345678-9", 999L
        );

        when(hospitalizacionRepository.findById(1L)).thenReturn(Optional.of(hospitalizacion));

        assertThatThrownBy(() -> hospitalizacionService.actualizar(1L, dtoHabitacionDistinta))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("habitación");
    }

    @Test
    void actualizar_debeRetornarVacioSiNoExisteId() {
        when(hospitalizacionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<HospitalizacionResponseDTO> resultado = hospitalizacionService.actualizar(99L, requestDTO);

        assertThat(resultado).isEmpty();
        verify(hospitalizacionRepository, never()).save(any());
    }

    // ─── eliminar ───────────────────────────────────────────────────────────

    @Test
    void eliminar_debeInvocarDeleteById() {
        doNothing().when(hospitalizacionRepository).deleteById(1L);

        hospitalizacionService.eliminar(1L);

        verify(hospitalizacionRepository, times(1)).deleteById(1L);
    }
}