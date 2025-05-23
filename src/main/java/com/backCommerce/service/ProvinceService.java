package com.backCommerce.service;

import com.backCommerce.dto.ProvinceDto;
import java.util.List;

public interface ProvinceService {
    ProvinceDto createProvince(ProvinceDto provinceDto);
    ProvinceDto getProvinceById(Long id);
    List<ProvinceDto> getAllProvinces();
    List<ProvinceDto> getProvincesByCountryId(Long countryId);
    ProvinceDto updateProvince(Long id, ProvinceDto provinceDto);
    void deleteProvince(Long id);
}
