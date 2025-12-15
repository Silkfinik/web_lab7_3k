package org.example.repository;

import org.example.entity.Invoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Integer> {

    Page<Invoice> findByIsPaidFalse(Pageable pageable);
}