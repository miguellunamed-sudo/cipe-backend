package com.papeleria.cipe.controller;

import com.papeleria.cipe.model.Articulo;
import com.papeleria.cipe.repository.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articulos")
@CrossOrigin(origins = "*")
public class ArticuloController {

    @Autowired
    private ArticuloRepository repository;

    @GetMapping
    public List<Articulo> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Articulo> buscarPorCodigo(@PathVariable String codigo) {
        return repository.findByCodigoBarras(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Articulo> crear(@RequestBody Articulo articulo) {
        Articulo guardado = repository.save(articulo);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
}