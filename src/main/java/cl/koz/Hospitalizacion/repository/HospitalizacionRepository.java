package cl.koz.Hospitalizacion.repository;

import cl.koz.Hospitalizacion.model.Hospitalizacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalizacionRepository extends JpaRepository<Hospitalizacion, Long> {
}
