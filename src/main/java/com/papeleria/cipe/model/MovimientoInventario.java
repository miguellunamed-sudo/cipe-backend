package com.papeleria.cipe.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long idMovimiento;

    @Column(name = "id_articulo", nullable = false)
    private Long idArticulo;

    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private String tipoMovimiento;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(length = 255)
    private String motivo;

    @Column(name = "fecha", insertable = false, updatable = false)
    private LocalDateTime fecha;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    public Long getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(Long idMovimiento) { this.idMovimiento = idMovimiento; }

    public Long getIdArticulo() { return idArticulo; }
    public void setIdArticulo(Long idArticulo) { this.idArticulo = idArticulo; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
}