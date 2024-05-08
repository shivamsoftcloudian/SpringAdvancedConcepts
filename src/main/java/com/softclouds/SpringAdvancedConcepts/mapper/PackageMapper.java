package com.softclouds.SpringAdvancedConcepts.mapper;

import com.softclouds.SpringAdvancedConcepts.dto.PackageDTO;
import com.softclouds.SpringAdvancedConcepts.entity.Package;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PackageMapper {

    Package toEntity(PackageDTO packageDTO);

    PackageDTO toDTO(Package Package);

    List<Package> toEntity(List<PackageDTO> packageDTOS);

    List<PackageDTO> toDTO(List<Package> Packages);

}
