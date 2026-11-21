package com.papeleria.cipe.repository;

import com.papeleria.cipe.model.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArticuloRepository extends JpaRepository<Articulo, Long> {
    // Spring Boot crea el "SELECT * FROM articulos WHERE codigo_barras = ?" solito
    Optional<Articulo> findByCodigoBarras(String codigoBarras);
}