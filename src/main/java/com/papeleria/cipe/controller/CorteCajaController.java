package com.papeleria.cipe.controller;

import com.papeleria.cipe.model.CorteCaja;
import com.papeleria.cipe.repository.CorteCajaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cortes")
@CrossOrigin(origins = "*")
public class CorteCajaController {

    @Autowired
    private CorteCajaRepository corteCajaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private BigDecimal calcularVentasDesde(LocalDateTime apertura) {
        Object res = entityManager.createNativeQuery(
                        "SELECT COALESCE(SUM(total), 0) FROM ventas WHERE fecha >= :apertura")
                .setParameter("apertura", apertura)
                .getSingleResult();
        return new BigDecimal(res.toString());
    }

    // 1. Consultar turno activo (si existe) y su acumulado actual
    @GetMapping("/activo")
    public ResponseEntity<?> obtenerTurnoActivo() {
        Optional<CorteCaja> activoOpt = corteCajaRepository.findFirstByFechaCierreIsNullOrderByFechaAperturaDesc();
        if (activoOpt.isEmpty()) {
            return ResponseEntity.ok(Map.of("abierto", false));
        }

        CorteCaja corte = activoOpt.get();
        BigDecimal ventasTurno = calcularVentasDesde(corte.getFechaApertura());
        BigDecimal esperado = corte.getMontoInicial().add(ventasTurno);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("abierto", true);
        respuesta.put("corte", corte);
        respuesta.put("ventasTurno", ventasTurno);
        respuesta.put("montoEsperado", esperado);
        return ResponseEntity.ok(respuesta);
    }

    // 2. Abrir caja
    @PostMapping("/abrir")
    public ResponseEntity<?> abrirCaja(@RequestBody Map<String, Object> payload) {
        if (corteCajaRepository.findFirstByFechaCierreIsNullOrderByFechaAperturaDesc().isPresent()) {
            return ResponseEntity.badRequest().body("Ya existe un turno de caja abierto.");
        }

        BigDecimal montoInicial = new BigDecimal(payload.getOrDefault("montoInicial", "0").toString());
        Integer idUsuario = payload.get("idUsuario") != null
                ? Integer.parseInt(payload.get("idUsuario").toString())
                : null;

        CorteCaja nuevo = new CorteCaja();
        nuevo.setFechaApertura(LocalDateTime.now());
        nuevo.setMontoInicial(montoInicial);
        nuevo.setIdUsuario(idUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(corteCajaRepository.save(nuevo));
    }

    // 3. Cerrar caja y calcular diferencia
    @PostMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrarCaja(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        return corteCajaRepository.findById(id).map(corte -> {
            if (corte.getFechaCierre() != null) {
                return ResponseEntity.badRequest().body("Este corte de caja ya fue cerrado.");
            }

            BigDecimal montoReal = new BigDecimal(payload.get("montoFinalReal").toString());
            BigDecimal ventasTurno = calcularVentasDesde(corte.getFechaApertura());
            BigDecimal montoCalculado = corte.getMontoInicial().add(ventasTurno);
            BigDecimal diferencia = montoReal.subtract(montoCalculado);

            corte.setFechaCierre(LocalDateTime.now());
            corte.setMontoFinalCalculado(montoCalculado);
            corte.setMontoFinalReal(montoReal);
            corte.setDiferencia(diferencia);

            return ResponseEntity.ok(corteCajaRepository.save(corte));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 4. Historial de cortes realizados
    @GetMapping
    public List<CorteCaja> listarCortes() {
        return corteCajaRepository.findAllByOrderByFechaAperturaDesc();
    }
}