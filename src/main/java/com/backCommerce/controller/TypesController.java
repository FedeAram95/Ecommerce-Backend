package com.backCommerce.controller;

import com.backCommerce.dto.CategoryCompleteDto;
import com.backCommerce.dto.TypesDto;
import com.backCommerce.model.Types;
import com.backCommerce.service.TypesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "TypesController", description = "Endpoints para la gestión de tipos")
@RestController
@RequestMapping(path = "/categoryTypes")
public class TypesController {

    @Autowired
    private TypesService typesService;

    @Operation(summary = "Obtener todos los tipos", description = "Devuelve una lista de todos los tipos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Types.class),
                            examples = @ExampleObject(value = "[{\"id\":1, \"name\":\"Type1\"}, {\"id\":2, \"name\":\"Type2\"}]")
                    ))
    })
    @GetMapping
    public ResponseEntity<List<CategoryCompleteDto>> getAll() {
        return typesService.getTypes();
    }

    @Operation(summary = "Obtener un tipo por ID", description = "Devuelve un tipo basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo obtenido exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Types.class),
                            examples = @ExampleObject(value = "{\"id\":1, \"name\":\"Type1\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    @GetMapping("/{typesId}")
    public ResponseEntity<TypesDto> getById(@PathVariable Long typesId) {
        return typesService.getTypesById(typesId);
    }

    // TODO: create de tipo no funciona
    @Operation(summary = "Crear un nuevo tipo", description = "Crea un nuevo tipo en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo guardado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Types.class),
                            examples = @ExampleObject(value = "{\"id\":1, \"name\":\"Nuevo Tipo\"}")
                    )),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<TypesDto> createType(@RequestBody TypesDto types) {
        return typesService.createType(types);
    }

    // TODO: update de tipo no funciona
    @Operation(summary = "Actualizar un tipo por ID", description = "Actualiza un tipo existente basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Types.class),
                            examples = @ExampleObject(value = "{\"id\":1, \"name\":\"Tipo Actualizado\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PutMapping("/{typesId}")
    public ResponseEntity<TypesDto> updateTypes(@PathVariable Long typesId, @RequestBody TypesDto types) {
        return typesService.updateType(typesId, types);
    }

    // TODO: testear delete
    @Operation(summary = "Eliminar un tipo por ID", description = "Elimina un tipo existente basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    @DeleteMapping("/{typesId}")
    public ResponseEntity<Void> deleteTypes(@PathVariable Long typesId) {
        return typesService.deleteType(typesId);
    }
}
