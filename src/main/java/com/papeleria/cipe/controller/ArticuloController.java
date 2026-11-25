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

    @Autowired
    private com.papeleria.cipe.repository.ArticuloRepository articuloRepository;

    @Autowired
    private com.papeleria.cipe.repository.MovimientoInventarioRepository movimientoRepository;

    // Surtir mercancía (sumar stock y registrar ENTRADA en Kardex)
    @PostMapping("/{id}/surtir")
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<?> surtirStock(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, Object> payload) {
        return articuloRepository.findById(id).map(articulo -> {
            int cantidad = Integer.parseInt(payload.get("cantidad").toString());
            if (cantidad <= 0) {
                return ResponseEntity.badRequest().body("La cantidad a surtir debe ser mayor a 0");
            }

            int stockAnterior = articulo.getStockActual() != null ? articulo.getStockActual() : 0;
            int stockNuevo = stockAnterior + cantidad;
            articulo.setStockActual(stockNuevo);
            articuloRepository.save(articulo);

            com.papeleria.cipe.model.MovimientoInventario mov = new com.papeleria.cipe.model.MovimientoInventario();
            mov.setIdArticulo(articulo.getIdArticulo());
            mov.setTipoMovimiento("ENTRADA");
            mov.setCantidad(cantidad);
            mov.setMotivo(payload.getOrDefault("motivo", "Reabastecimiento de inventario").toString());
            movimientoRepository.save(mov);

            return ResponseEntity.ok(articulo);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Editar datos y precio de un artículo existente
    @PutMapping("/{id}")
    public ResponseEntity<Articulo> actualizarArticulo(
            @PathVariable Long id,
            @RequestBody Articulo datos) {
        return articuloRepository.findById(id).map(articulo -> {
            articulo.setNombre(datos.getNombre());
            articulo.setCodigoBarras(datos.getCodigoBarras());
            articulo.setCostoCompra(datos.getCostoCompra());
            articulo.setMargenGanancia(datos.getMargenGanancia());
            articulo.setPrecioFinal(datos.getPrecioFinal());
            if (datos.getStockMinimo() != null) {
                articulo.setStockMinimo(datos.getStockMinimo());
            }
            return ResponseEntity.ok(articuloRepository.save(articulo));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar artículo
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarArticulo(@PathVariable Long id) {
        if (!articuloRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            articuloRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("No se puede eliminar porque ya tiene ventas o movimientos asociados.");
        }
    }
}