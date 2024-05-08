package com.softclouds.SpringAdvancedConcepts.controller;

import com.softclouds.SpringAdvancedConcepts.dto.PackageDTO;
import com.softclouds.SpringAdvancedConcepts.ratelimit.WithRateLimitProtection;
import com.softclouds.SpringAdvancedConcepts.request.PackageRequest;
import com.softclouds.SpringAdvancedConcepts.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/samplespring/v1")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/packages")
    public ResponseEntity<PackageDTO> createPackage(@RequestBody PackageRequest request) {
        return new ResponseEntity<>(this.packageService.createPackage(request), HttpStatus.CREATED);
    }

    @WithRateLimitProtection
    @GetMapping("/packages")
    public ResponseEntity<List<PackageDTO>> getAllPackages() {
        return new ResponseEntity<>(this.packageService.readPackages(), HttpStatus.OK);
    }

    @GetMapping("/packages/{packageId}")
    public ResponseEntity<PackageDTO> getPackageById(@PathVariable Long packageId) {
        return new ResponseEntity<>(this.packageService.readPackageById(packageId), HttpStatus.FOUND);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/packages/{packageId}")
    public ResponseEntity<PackageDTO> updatePackage(@PathVariable Long packageId, @RequestBody PackageRequest request) {
        return new ResponseEntity<>(this.packageService.updatePackage(packageId, request), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/packages/status/{packageId}")
    public ResponseEntity<PackageDTO> togglePackageStatus(@PathVariable Long packageId) {
        return new ResponseEntity<>(this.packageService.togglePackageStatus(packageId), HttpStatus.OK);
    }
}
