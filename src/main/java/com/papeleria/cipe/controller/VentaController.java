package com.papeleria.cipe.controller;

import com.papeleria.cipe.model.*;
import com.papeleria.cipe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoRepository;

    @GetMapping
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> registrarVenta(@RequestBody VentaRequest request) {
        if (request.items == null || request.items.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El carrito está vacío"));
        }

        Venta venta = new Venta();
        venta.setIdUsuario(request.idUsuario);
        BigDecimal totalCalculado = BigDecimal.ZERO;

        for (ItemRequest item : request.items) {
            Articulo articulo = articuloRepository.findById(item.idArticulo)
                    .orElseThrow(() -> new RuntimeException("Artículo no encontrado: " + item.idArticulo));

            if (articulo.getStockActual() < item.cantidad) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Stock insuficiente para: " + articulo.getNombre() +
                                " (disponible: " + articulo.getStockActual() + ")")
                );
            }

            // 1. Descontar stock
            articulo.setStockActual(articulo.getStockActual() - item.cantidad);
            articuloRepository.save(articulo);

            // 2. Armar detalle de venta
            BigDecimal precio = articulo.getPrecioFinal();
            BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(item.cantidad));
            totalCalculado = totalCalculado.add(subtotal);

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setIdArticulo(articulo.getIdArticulo());
            detalle.setCantidad(item.cantidad);
            detalle.setPrecioUnitario(precio);
            detalle.setSubtotal(subtotal);
            venta.getDetalles().add(detalle);

            // 3. Registrar salida en Kardex
            MovimientoInventario mov = new MovimientoInventario();
            mov.setIdArticulo(articulo.getIdArticulo());
            mov.setTipoMovimiento("SALIDA");
            mov.setCantidad(item.cantidad);
            mov.setMotivo("Venta en mostrador");
            mov.setIdUsuario(request.idUsuario);
            movimientoRepository.save(mov);
        }

        venta.setTotal(totalCalculado);
        Venta guardada = ventaRepository.save(venta);

        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    // DTOs internos para recibir el JSON limpio desde Flutter
    public static class VentaRequest {
        public Integer idUsuario;
        public List<ItemRequest> items;
    }

    public static class ItemRequest {
        public Long idArticulo;
        public Integer cantidad;
    }
}