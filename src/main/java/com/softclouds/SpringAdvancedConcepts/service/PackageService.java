package com.softclouds.SpringAdvancedConcepts.service;

import com.softclouds.SpringAdvancedConcepts.dto.PackageDTO;
import com.softclouds.SpringAdvancedConcepts.request.PackageRequest;

import java.util.List;

public interface PackageService {

    PackageDTO createPackage(PackageRequest request);

    List<PackageDTO> readPackages();

    PackageDTO readPackageById(Long packageId);

    PackageDTO updatePackage(Long packageId, PackageRequest request);

    PackageDTO togglePackageStatus(Long packageId);
}
