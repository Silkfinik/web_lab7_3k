package org.example.repository;

import org.example.entity.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Integer> {
    Page<Service> findAll(Pageable pageable);
}