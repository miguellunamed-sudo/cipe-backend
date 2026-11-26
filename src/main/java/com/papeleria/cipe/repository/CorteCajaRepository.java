package com.papeleria.cipe.repository;

import com.papeleria.cipe.model.CorteCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CorteCajaRepository extends JpaRepository<CorteCaja, Long> {
    Optional<CorteCaja> findFirstByFechaCierreIsNullOrderByFechaAperturaDesc();
    List<CorteCaja> findAllByOrderByFechaAperturaDesc();
}