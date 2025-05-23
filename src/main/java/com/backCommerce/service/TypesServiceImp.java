package com.backCommerce.service;

import com.backCommerce.dto.CategoryCompleteDto;
import com.backCommerce.dto.CategoryDto;
import com.backCommerce.dto.TypesDto;
import com.backCommerce.model.Category;
import com.backCommerce.model.Types;
import com.backCommerce.repository.CategoryRepository;
import com.backCommerce.repository.TypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TypesServiceImp implements TypesService {

    @Autowired
    TypesRepository typesRepository;

    @Autowired
    CategoryRepository categoryRepository;
    CategoryServiceImp categoryServiceImp;

    @Override
    public ResponseEntity<List<CategoryCompleteDto>> getTypes() {
        try {
            List<TypesDto> types = typesRepository.findAll()
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
            List<CategoryDto> category = categoryRepository.findAll()
                    .stream()
                    .map(this::categoryConvertToDTO)
                    .toList();

            List<CategoryCompleteDto> categoryCompleteDtoList = categoryNew(category , types);

            return new ResponseEntity<>(categoryCompleteDtoList, HttpStatus.OK);
        } catch (Exception e) {
            // Manejo de excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<TypesDto> getTypesById(Long id) {
        try {
            TypesDto type = typesRepository.findById(id)
                    .map(this::convertToDTO)
                    .orElse(null);

            if (type != null) {
                return new ResponseEntity<>(type, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Manejo de excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<TypesDto> createType(TypesDto types) {
        try {
            Types savedType = typesRepository.save(convertToEntity(types));
            return new ResponseEntity<>(convertToDTO(savedType), HttpStatus.CREATED);
        } catch (Exception e) {
            // Manejo de excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> deleteType(Long id) {
        try {
            Optional<Types> types = typesRepository.findById(id);
            if (types.isPresent()) {
                typesRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Manejo de excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<TypesDto> updateType(Long typesId, TypesDto typesDto) {
        try {
            Types types = convertToEntity(typesDto);
            Optional<Types> existingTypes = typesRepository.findById(typesId);
            if (existingTypes.isPresent()) {
                Types updatedTypes = existingTypes.get();
                updatedTypes.setDescription(types.getDescription());
                updatedTypes.setCategory(types.getCategory());
                typesRepository.save(updatedTypes);
                return new ResponseEntity<>(convertToDTO(updatedTypes), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Manejo de excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private TypesDto convertToDTO(Types type) {
        TypesDto typesDto = new TypesDto();
        typesDto.setId(type.getId());
        typesDto.setDescription(type.getDescription());
        typesDto.setCategoryId(type.getCategory().getId());
        return typesDto;
    }

    private Types convertToEntity(TypesDto typesDto) {
        Optional<Category> cat = categoryRepository.findById(typesDto.getCategoryId());
        Types types = new Types();

        types.setId(typesDto.getId());
        types.setDescription(typesDto.getDescription());
        types.setCategory(cat.orElse(null));

        return types;
    }

    private CategoryDto categoryConvertToDTO(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setDescription(category.getDescription());

        return categoryDto;
    }

    private List<CategoryCompleteDto> categoryNew(List<CategoryDto> categoryDtos , List<TypesDto> typesDtos) {
        List<CategoryCompleteDto> categoryCompleteDtoList = new ArrayList<>();
        for (CategoryDto categoryDto:categoryDtos) {
            List<TypesDto> typesDtoList = new ArrayList<>();
            for (TypesDto typesDto:typesDtos){
                if (typesDto.getCategoryId() == categoryDto.getId()){
                    typesDtoList.add(typesDto);
                }
            }
            CategoryCompleteDto categoryCompleteDto = new CategoryCompleteDto();
            categoryCompleteDto.setId(categoryDto.getId());
            categoryCompleteDto.setDescription(categoryDto.getDescription());
            categoryCompleteDto.setTypesDtoList(typesDtoList);
            categoryCompleteDtoList.add(categoryCompleteDto);
        }

        return categoryCompleteDtoList;
    }

}
