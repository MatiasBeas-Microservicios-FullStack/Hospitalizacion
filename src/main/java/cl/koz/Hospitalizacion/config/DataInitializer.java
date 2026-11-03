package cl.koz.Hospitalizacion.config;

import cl.koz.Hospitalizacion.model.Hospitalizacion;
import cl.koz.Hospitalizacion.model.Role;
import cl.koz.Hospitalizacion.model.User;
import cl.koz.Hospitalizacion.repository.HospitalizacionRepository;
import cl.koz.Hospitalizacion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final HospitalizacionRepository hospitalizacionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("Maty").isEmpty()) {
            User user = new User();
            user.setUsername("Maty");
            user.setPassword(passwordEncoder.encode("1234"));
            user.setRole(Role.ADMIN);
            userRepository.save(user);
            log.info(">>> DataInitializer: Usuario Maty creado correctamente.");
        }
        if (hospitalizacionRepository.count() > 0) {
            log.info(">>> DataInitializer: la BD ya tiene datos, se omite la carga inicial.");
            return;
        }
        log.info(">>> DataInitializer: BD vacía detectada, insertando datos de prueba...");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        hospitalizacionRepository.save(new Hospitalizacion(null, 5,  "Apendicitis",          sdf.parse("2025-01-10"), sdf.parse("2025-01-15"), "22.359.190-6", 1L));
        hospitalizacionRepository.save(new Hospitalizacion(null, 3,  "Neumonía",             sdf.parse("2025-02-05"), sdf.parse("2025-02-08"), "18.765.432-1", 2L));
        hospitalizacionRepository.save(new Hospitalizacion(null, 7,  "Fractura de fémur",   sdf.parse("2025-03-12"), sdf.parse("2025-03-19"), "11.111.111-1", 1L));
        hospitalizacionRepository.save(new Hospitalizacion(null, 2,  "Infección urinaria",  sdf.parse("2025-04-20"), sdf.parse("2025-04-22"), "22.222.222-2", 3L));
        hospitalizacionRepository.save(new Hospitalizacion(null, 10, "Cirugía de rodilla",  sdf.parse("2025-05-01"), sdf.parse("2025-05-11"), "10.333.333-3", 2L));

        log.info(">>> DataInitializer: {} hospitalizaciones insertadas.", hospitalizacionRepository.count());
    }

}
