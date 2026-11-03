package cl.koz.Hospitalizacion.Assemblers;

import cl.koz.Hospitalizacion.DTO.HospitalizacionResponseDTO;
import cl.koz.Hospitalizacion.controller.HospitalizacionController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HospitalizacionModelAssemblers implements RepresentationModelAssembler<HospitalizacionResponseDTO, EntityModel<HospitalizacionResponseDTO>> {

    @Override
    public EntityModel<HospitalizacionResponseDTO> toModel(HospitalizacionResponseDTO hospitalizacion) {
        return EntityModel.of(hospitalizacion,
                linkTo(methodOn(HospitalizacionController.class)
                        .obtenerPorId(hospitalizacion.getCod_hospi())).withSelfRel(),
                linkTo(methodOn(HospitalizacionController.class)
                        .obtenerTodosHospi()).withRel("all-hospitalizaciones")
        );
    }
}