package com.backCommerce.service;

import com.backCommerce.dto.LocalityDto;
import com.backCommerce.model.Locality;
import com.backCommerce.model.Province;
import com.backCommerce.repository.LocalityRepository;
import com.backCommerce.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalityServiceImp implements LocalityService {

    @Autowired
    private LocalityRepository localityRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    @Transactional
    public LocalityDto createLocality(LocalityDto localityDto) {
        Province province = provinceRepository.findById(localityDto.getProvinceId())
                .orElseThrow(() -> new RuntimeException("Provincia no encontrada con id: " + localityDto.getProvinceId()));

        Locality locality = new Locality();
        locality.setName(localityDto.getName());
        locality.setProvince(province);

        Locality savedLocality = localityRepository.save(locality);
        return mapToDto(savedLocality);
    }

    @Override
    @Transactional(readOnly = true)
    public LocalityDto getLocalityById(Long id) {
        Locality locality = localityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Localidad no encontrada con id: " + id));
        return mapToDto(locality);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalityDto> getAllLocalities() {
        List<Locality> localities = localityRepository.findAll();
        return localities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalityDto> getLocalitiesByProvinceId(Long provinceId) {
        List<Locality> localities = localityRepository.findByProvinceId(provinceId);
        return localities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LocalityDto updateLocality(Long id, LocalityDto localityDto) {
        Locality locality = localityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Localidad no encontrada con id: " + id));

        Province province = provinceRepository.findById(localityDto.getProvinceId())
                .orElseThrow(() -> new RuntimeException("Provincia no encontrada con id: " + localityDto.getProvinceId()));

        locality.setName(localityDto.getName());
        locality.setProvince(province);

        Locality updatedLocality = localityRepository.save(locality);
        return mapToDto(updatedLocality);
    }

    @Override
    @Transactional
    public void deleteLocality(Long id) {
        Locality locality = localityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Localidad no encontrada con id: " + id));

        localityRepository.delete(locality);
    }

    // Método de mapeo de Entity a DTO
    private LocalityDto mapToDto(Locality locality) {
        LocalityDto localityDto = new LocalityDto();
        localityDto.setId(locality.getId());
        localityDto.setName(locality.getName());
        localityDto.setProvinceId(locality.getProvince().getId());
        return localityDto;
    }
}
