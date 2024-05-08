package com.softclouds.SpringAdvancedConcepts.repository;

import com.softclouds.SpringAdvancedConcepts.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {

    Optional<BlacklistToken> findByToken(String token);
}
