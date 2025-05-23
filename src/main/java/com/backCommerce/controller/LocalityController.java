package com.backCommerce.controller;

import com.backCommerce.dto.LocalityDto;
import com.backCommerce.service.LocalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources/localities")
public class LocalityController {

    @Autowired
    private LocalityService localityService;

    @PostMapping
    public ResponseEntity<LocalityDto> createLocality(@RequestBody LocalityDto localityDto) {
        return new ResponseEntity<>(localityService.createLocality(localityDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocalityDto> getLocalityById(@PathVariable Long id) {
        return ResponseEntity.ok(localityService.getLocalityById(id));
    }

    @GetMapping
    public ResponseEntity<List<LocalityDto>> getAllLocalities() {
        return ResponseEntity.ok(localityService.getAllLocalities());
    }

    @GetMapping("/province/{provinceId}")
    public ResponseEntity<List<LocalityDto>> getLocalitiesByProvinceId(@PathVariable Long provinceId) {
        return ResponseEntity.ok(localityService.getLocalitiesByProvinceId(provinceId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocalityDto> updateLocality(@PathVariable Long id, @RequestBody LocalityDto localityDto) {
        return ResponseEntity.ok(localityService.updateLocality(id, localityDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocality(@PathVariable Long id) {
        localityService.deleteLocality(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
