package com.backCommerce.controller;

import com.backCommerce.dto.ProvinceDto;
import com.backCommerce.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources/provinces")
public class ProvinceController {

    @Autowired
    private ProvinceService provinceService;

    @PostMapping
    public ResponseEntity<ProvinceDto> createProvince(@RequestBody ProvinceDto provinceDto) {
        return new ResponseEntity<>(provinceService.createProvince(provinceDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProvinceDto> getProvinceById(@PathVariable Long id) {
        return ResponseEntity.ok(provinceService.getProvinceById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProvinceDto>> getAllProvinces() {
        return ResponseEntity.ok(provinceService.getAllProvinces());
    }

    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<ProvinceDto>> getProvincesByCountryId(@PathVariable Long countryId) {
        return ResponseEntity.ok(provinceService.getProvincesByCountryId(countryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProvinceDto> updateProvince(@PathVariable Long id, @RequestBody ProvinceDto provinceDto) {
        return ResponseEntity.ok(provinceService.updateProvince(id, provinceDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvince(@PathVariable Long id) {
        provinceService.deleteProvince(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
