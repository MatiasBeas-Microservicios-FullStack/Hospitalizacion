package cl.koz.Hospitalizacion;

import cl.koz.Hospitalizacion.DTO.HospitalizacionRequestDTO;
import cl.koz.Hospitalizacion.DTO.HospitalizacionResponseDTO;
import cl.koz.Hospitalizacion.controller.HospitalizacionController;
import cl.koz.Hospitalizacion.security.JwtAuthFilter;
import cl.koz.Hospitalizacion.security.JwtService;
import cl.koz.Hospitalizacion.service.HospitalizacionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HospitalizacionController.class)
@AutoConfigureMockMvc(addFilters = false)
class HospitalizacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private HospitalizacionService hospitalizacionService;

    private HospitalizacionResponseDTO responseDTO;
    private HospitalizacionRequestDTO requestDTO;
    private Date fechaIngreso;
    private Date fechaSalida;

    @BeforeEach
    void setUp() {
        fechaIngreso = new Date();
        fechaSalida = new Date(fechaIngreso.getTime() + 86400000L * 5);

        responseDTO = new HospitalizacionResponseDTO(
                1L, 5, "Cirugia", fechaIngreso, fechaSalida, "12345678-9", 101L
        );

        requestDTO = new HospitalizacionRequestDTO(
                5, "Cirugia", fechaIngreso, fechaSalida, "12345678-9", 101L
        );
    }

    // ─── GET /Hospitalizacion ────────────────────────────────────────────────

    @Test
    @WithMockUser
    void obtenerTodos_debeRetornar200ConLista() throws Exception {
        when(hospitalizacionService.obtenerTodo()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/Hospitalizacion"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].cod_hospi").value(1))
                .andExpect(jsonPath("$[0].motivo").value("Cirugia"));
    }

    @Test
    @WithMockUser
    void obtenerTodos_debeRetornar200ConListaVacia() throws Exception {
        when(hospitalizacionService.obtenerTodo()).thenReturn(List.of());

        mockMvc.perform(get("/Hospitalizacion"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }


    // ─── GET /Hospitalizacion/{id} ───────────────────────────────────────────

    @Test
    @WithMockUser
    void obtenerPorId_debeRetornar200SiExiste() throws Exception {
        when(hospitalizacionService.obtenerPorId(1L)).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/Hospitalizacion/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cod_hospi").value(1))
                .andExpect(jsonPath("$.pacienteRun").value("12345678-9"));
    }

    @Test
    @WithMockUser
    void obtenerPorId_debeRetornar404SiNoExiste() throws Exception {
        when(hospitalizacionService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/Hospitalizacion/99"))
                .andExpect(status().isNotFound());
    }

    // ─── POST /Hospitalizacion ───────────────────────────────────────────────

    @Test
    @WithMockUser
    void guardar_debeRetornar201ConDatosValidos() throws Exception {
        when(hospitalizacionService.guardar(any(HospitalizacionRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/Hospitalizacion")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cod_hospi").value(1))
                .andExpect(jsonPath("$.motivo").value("Cirugia"));
    }

    @Test
    @WithMockUser
    void guardar_debeRetornar400ConCamposInvalidos() throws Exception {
        HospitalizacionRequestDTO invalido = new HospitalizacionRequestDTO(
                -1, "", null, null, "", null
        );

        mockMvc.perform(post("/Hospitalizacion")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    // ─── PUT /Hospitalizacion/{id} ───────────────────────────────────────────

    @Test
    @WithMockUser
    void actualizar_debeRetornar200SiExiste() throws Exception {
        when(hospitalizacionService.actualizar(eq(1L), any(HospitalizacionRequestDTO.class)))
                .thenReturn(Optional.of(responseDTO));

        mockMvc.perform(put("/Hospitalizacion/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cod_hospi").value(1));
    }

    @Test
    @WithMockUser
    void actualizar_debeRetornar404SiNoExiste() throws Exception {
        when(hospitalizacionService.actualizar(eq(99L), any(HospitalizacionRequestDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/Hospitalizacion/{id}", 99L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    // ─── DELETE /Hospitalizacion/{id} ───────────────────────────────────────

    @Test
    @WithMockUser
    void eliminar_debeRetornar204SiExiste() throws Exception {
        when(hospitalizacionService.obtenerPorId(1L)).thenReturn(Optional.of(responseDTO));
        doNothing().when(hospitalizacionService).eliminar(1L);

        mockMvc.perform(delete("/Hospitalizacion/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void eliminar_debeRetornar404SiNoExiste() throws Exception {
        when(hospitalizacionService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/Hospitalizacion/{id}", 99L)
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(hospitalizacionService, never()).eliminar(99L);
    }
}