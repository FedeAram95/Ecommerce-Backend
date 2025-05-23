package com.backCommerce.service;

import com.backCommerce.dto.LocalityDto;
import java.util.List;

public interface LocalityService {
    LocalityDto createLocality(LocalityDto localityDto);
    LocalityDto getLocalityById(Long id);
    List<LocalityDto> getAllLocalities();
    List<LocalityDto> getLocalitiesByProvinceId(Long provinceId);
    LocalityDto updateLocality(Long id, LocalityDto localityDto);
    void deleteLocality(Long id);
}
