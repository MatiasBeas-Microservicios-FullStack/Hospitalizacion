package cl.koz.Hospitalizacion.webClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PacienteClient {
    private final WebClient webClientRun;

    public String obtenerRun(String run) {
        try {
            Map response = webClientRun
                    .get()
                    .uri("/pacientes/" + run)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response != null ? (String) response.get("run") : "Sin RUN";
        } catch (Exception e) {
            return "Sin RUN";
        }
    }
    public boolean existePaciente(String run) {
        try {
            Map response = webClientRun
                    .get()
                    .uri("/pacientes/" + run)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return response != null && response.get("run") != null;
        }catch (Exception e){
            return false;
        }
    }
}
