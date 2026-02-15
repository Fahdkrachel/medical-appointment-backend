package com.chufesgesr.repositories;

import com.chufesgesr.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    Optional<Service> findByNomService(String nomService);
    
    boolean existsByNomService(String nomService);
}
