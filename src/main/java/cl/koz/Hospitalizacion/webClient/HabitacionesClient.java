package cl.koz.Hospitalizacion.webClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
@Component
@RequiredArgsConstructor
public class HabitacionesClient {
    private final WebClient webClient;

    public String obtenerNroHabitacion(Long nroHabitacion) {
        try {
            Map response = webClient
                    .get()
                    .uri("/api/habitacion/" + nroHabitacion)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response != null ? (String) response.get("nroHabitacion") : "Sin habitacion";
        } catch (Exception e) {
            return "Sin numero de habitacion";
        }
    }

    public boolean existeHabitacion(Long nroHabitacion) {
        try {
            Map response = webClient
                    .get()
                    .uri("/api/habitacion/" + nroHabitacion)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return response != null && response.get("nroHabitacion") != null;
        } catch (Exception e) {
            return false;
        }
    }
}
