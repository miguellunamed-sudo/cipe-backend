package com.papeleria.cipe.controller;

import com.papeleria.cipe.model.Ubicacion;
import com.papeleria.cipe.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
@CrossOrigin(origins = "*")
public class UbicacionController {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @GetMapping
    public List<Ubicacion> listar() {
        return ubicacionRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Ubicacion> crear(@RequestBody Ubicacion ubicacion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ubicacionRepository.save(ubicacion));
    }
}