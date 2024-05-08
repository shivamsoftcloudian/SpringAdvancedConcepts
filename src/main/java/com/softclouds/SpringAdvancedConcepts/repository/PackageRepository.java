package com.softclouds.SpringAdvancedConcepts.repository;

import com.softclouds.SpringAdvancedConcepts.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
}
