package com.backCommerce.service;

import com.backCommerce.dto.CountryDto;
import com.backCommerce.model.Country;
import com.backCommerce.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryServiceImp implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    @Transactional
    public CountryDto createCountry(CountryDto countryDto) {
        Country country = new Country();
        country.setName(countryDto.getName());
        country.setProvinces(new ArrayList<>());

        Country savedCountry = countryRepository.save(country);
        return mapToDto(savedCountry);
    }

    @Override
    @Transactional(readOnly = true)
    public CountryDto getCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("País no encontrado con id: " + id));
        return mapToDto(country);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryDto> getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        return countries.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CountryDto updateCountry(Long id, CountryDto countryDto) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("País no encontrado con id: " + id));

        country.setName(countryDto.getName());

        Country updatedCountry = countryRepository.save(country);
        return mapToDto(updatedCountry);
    }

    @Override
    @Transactional
    public void deleteCountry(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("País no encontrado con id: " + id));

        // La eliminación en cascada ocurrirá automáticamente debido a la relación definida
        countryRepository.delete(country);
    }

    // Método de mapeo de Entity a DTO
    private CountryDto mapToDto(Country country) {
        CountryDto countryDto = new CountryDto();
        countryDto.setId(country.getId());
        countryDto.setName(country.getName());
        return countryDto;
    }
}
