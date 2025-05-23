package com.backCommerce.service;

import com.backCommerce.dto.CategoryCompleteDto;
import com.backCommerce.dto.TypesDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TypesService {

    ResponseEntity<List<CategoryCompleteDto>> getTypes();

    ResponseEntity<TypesDto> getTypesById(Long typesId);

    ResponseEntity<TypesDto> createType(TypesDto types);

    ResponseEntity<Void> deleteType(Long id);

    ResponseEntity<TypesDto> updateType(Long typesId, TypesDto types);

}