package com.backCommerce.service;

import com.backCommerce.dto.CountryDto;
import java.util.List;

public interface CountryService {
    CountryDto createCountry(CountryDto countryDto);
    CountryDto getCountryById(Long id);
    List<CountryDto> getAllCountries();
    CountryDto updateCountry(Long id, CountryDto countryDto);
    void deleteCountry(Long id);
}