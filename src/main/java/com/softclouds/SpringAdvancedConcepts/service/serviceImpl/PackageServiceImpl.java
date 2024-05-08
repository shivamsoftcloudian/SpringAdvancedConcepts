package com.softclouds.SpringAdvancedConcepts.service.serviceImpl;

import com.softclouds.SpringAdvancedConcepts.dto.PackageDTO;
import com.softclouds.SpringAdvancedConcepts.entity.Package;
import com.softclouds.SpringAdvancedConcepts.enums.Status;
import com.softclouds.SpringAdvancedConcepts.exception.ElementNotExistException;
import com.softclouds.SpringAdvancedConcepts.mapper.PackageMapper;
import com.softclouds.SpringAdvancedConcepts.repository.PackageRepository;
import com.softclouds.SpringAdvancedConcepts.request.PackageRequest;
import com.softclouds.SpringAdvancedConcepts.service.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;

    private final PackageMapper packageMapper;

    @Override
    public PackageDTO createPackage(PackageRequest request) {
        Package Package = new Package();
        Package.setPackageName(request.getPackageName());
        Package.setPackageDescription(request.getPackageDescription());
        Package.setPrice(request.getPrice());
        Package.setStatus(request.getStatus());
        Package = packageRepository.save(Package);
        return packageMapper.toDTO(Package);
    }

    @Override
    public List<PackageDTO> readPackages() {
        List<Package> Packages = packageRepository.findAll();
        if (!Packages.isEmpty()) {
            return packageMapper.toDTO(Packages);
        }
        throw new RuntimeException("not found");
    }

    @Override
    public PackageDTO readPackageById(Long packageId) {
        Package Package = packageRepository.findById(packageId).orElseThrow(
                () -> new ElementNotExistException("Package does not exist with package-id: " + packageId));
        return packageMapper.toDTO(Package);
    }

    @Override
    public PackageDTO updatePackage(Long packageId, PackageRequest request) {
        Package Package = packageRepository.findById(packageId).orElseThrow(
                () -> new ElementNotExistException("Package does not exist with package-id: " + packageId));
        Package.setPackageName(request.getPackageName());
        Package.setPackageDescription(request.getPackageDescription());
        Package.setPrice(request.getPrice());
        Package.setStatus(request.getStatus());
        Package = packageRepository.save(Package);

        log.info("package is updated: {}", packageId);

        return packageMapper.toDTO(Package);
    }

    @Override
    public PackageDTO togglePackageStatus(Long packageId) {
        Package Package = packageRepository.findById(packageId).orElseThrow(
                () -> new ElementNotExistException("Package does not exist with package-id: " + packageId));

        if (Package.getStatus() == Status.ACTIVE) {
            Package.setStatus(Status.INACTIVE);
        } else {
            Package.setStatus(Status.ACTIVE);
        }
        Package = packageRepository.save(Package);
        log.info("package status is updated: {}", packageId);

        return packageMapper.toDTO(Package);
    }
}
