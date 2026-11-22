package com.papeleria.cipe.repository;

import com.papeleria.cipe.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}