package com.softclouds.SpringAdvancedConcepts.repository;

import com.softclouds.SpringAdvancedConcepts.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
