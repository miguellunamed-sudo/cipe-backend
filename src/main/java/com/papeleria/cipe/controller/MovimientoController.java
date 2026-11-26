package com.papeleria.cipe.controller;

import com.papeleria.cipe.model.MovimientoInventario;
import com.papeleria.cipe.repository.MovimientoInventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoController {

    @Autowired
    private MovimientoInventarioRepository movimientoRepository;

    @GetMapping
    public List<MovimientoInventario> listarMovimientos() {
        return movimientoRepository.findAll(Sort.by(Sort.Direction.DESC, "idMovimiento"));
    }
}