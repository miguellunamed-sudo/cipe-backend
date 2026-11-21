package com.papeleria.cipe.controller;

import com.papeleria.cipe.model.Articulo;
import com.papeleria.cipe.repository.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articulos")
@CrossOrigin(origins = "*") // Permite que Flutter se conecte sin bloqueos de seguridad CORS
public class ArticuloController {
    
    @Autowired
    private ArticuloRepository repository;

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Articulo> buscarPorCodigo(@PathVariable String codigo) {
        return repository.findByCodigoBarras(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}