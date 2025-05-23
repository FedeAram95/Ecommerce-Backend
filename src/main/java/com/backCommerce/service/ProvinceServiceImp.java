package com.backCommerce.service;

import com.backCommerce.dto.ProvinceDto;
import com.backCommerce.model.Country;
import com.backCommerce.model.Province;
import com.backCommerce.repository.CountryRepository;
import com.backCommerce.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProvinceServiceImp implements ProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Override
    @Transactional
    public ProvinceDto createProvince(ProvinceDto provinceDto) {
        Country country = countryRepository.findById(provinceDto.getCountryId())
                .orElseThrow(() -> new RuntimeException("País no encontrado con id: " + provinceDto.getCountryId()));

        Province province = new Province();
        province.setName(provinceDto.getName());
        province.setCountry(country);
        province.setLocalities(new ArrayList<>());

        Province savedProvince = provinceRepository.save(province);
        return mapToDto(savedProvince);
    }

    @Override
    @Transactional(readOnly = true)
    public ProvinceDto getProvinceById(Long id) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provincia no encontrada con id: " + id));
        return mapToDto(province);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvinceDto> getAllProvinces() {
        List<Province> provinces = provinceRepository.findAll();
        return provinces.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvinceDto> getProvincesByCountryId(Long countryId) {
        List<Province> provinces = provinceRepository.findByCountryId(countryId);
        return provinces.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProvinceDto updateProvince(Long id, ProvinceDto provinceDto) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provincia no encontrada con id: " + id));

        Country country = countryRepository.findById(provinceDto.getCountryId())
                .orElseThrow(() -> new RuntimeException("País no encontrado con id: " + provinceDto.getCountryId()));

        province.setName(provinceDto.getName());
        province.setCountry(country);

        Province updatedProvince = provinceRepository.save(province);
        return mapToDto(updatedProvince);
    }

    @Override
    @Transactional
    public void deleteProvince(Long id) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provincia no encontrada con id: " + id));

        // La eliminación en cascada ocurrirá automáticamente debido a la relación definida
        provinceRepository.delete(province);
    }

    // Método de mapeo de Entity a DTO
    private ProvinceDto mapToDto(Province province) {
        ProvinceDto provinceDto = new ProvinceDto();
        provinceDto.setId(province.getId());
        provinceDto.setName(province.getName());
        provinceDto.setCountryId(province.getCountry().getId());
        return provinceDto;
    }
}
