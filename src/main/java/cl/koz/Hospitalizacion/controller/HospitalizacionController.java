package cl.koz.Hospitalizacion.controller;

import cl.koz.Hospitalizacion.DTO.HospitalizacionRequestDTO;
import cl.koz.Hospitalizacion.DTO.HospitalizacionResponseDTO;
import cl.koz.Hospitalizacion.service.HospitalizacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/Hospitalizacion")
@Tag(name = "Hospitalizacion", description = "Operaciones relacionadas con las hospitalizaciones")
@RequiredArgsConstructor
public class HospitalizacionController {
    private final HospitalizacionService hospitalizacionService;

    @GetMapping
    @Operation(summary = "Obtener todos las hospitalizaciones", description = "Entrega una lista de todas las hospitalizaciones registrados")
    public ResponseEntity<List<HospitalizacionResponseDTO>> obtenerTodosHospi(){
        return ResponseEntity.ok(hospitalizacionService.obtenerTodo());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener hospitalizaciones por ID's", description = "Retorna el hospitalizaciones buscado por ID'S")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "hospitalizacion encontrada"),
            @ApiResponse(responseCode = "404", description = "hospitalizacion ingresada no encontrada!")
    })
    public ResponseEntity<HospitalizacionResponseDTO> obtenerPorId(@PathVariable Long id){
        return hospitalizacionService.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear una nueva Hospitalizacion",description = "Guarda el nuevo hospitalizacion ingresado")
    @ApiResponse(responseCode = "201", description = "hospitalizacion creada exitosamente")
    public ResponseEntity<HospitalizacionResponseDTO> guardarHospitalizacion(@Valid @RequestBody HospitalizacionRequestDTO dto){
        HospitalizacionResponseDTO response = hospitalizacionService.guardar(dto);
        return  ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar hospitalizacion existente",description = "Actualiza y guarda la informacion cambiada en el examen buscado por ID's")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "hospitalizacion actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "El hospitalizacion ingresado no existe")
    })
    public ResponseEntity<HospitalizacionResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody HospitalizacionRequestDTO dto){
        return hospitalizacionService.actualizar(id,dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary ="Eliminar hospitalizacion existente", description = "Elimina un hospitalizacion buscado por ID's")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "hospitalizacion eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "La hospitalizacion ingresado no existe")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        if(hospitalizacionService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        hospitalizacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
