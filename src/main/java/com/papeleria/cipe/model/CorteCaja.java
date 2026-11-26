package com.papeleria.cipe.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cortes_caja")
public class CorteCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_corte")
    private Long idCorte;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDateTime fechaApertura;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(name = "monto_inicial", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoInicial;

    @Column(name = "monto_final_calculado", precision = 10, scale = 2)
    private BigDecimal montoFinalCalculado;

    @Column(name = "monto_final_real", precision = 10, scale = 2)
    private BigDecimal montoFinalReal;

    @Column(precision = 10, scale = 2)
    private BigDecimal diferencia;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    public Long getIdCorte() { return idCorte; }
    public void setIdCorte(Long idCorte) { this.idCorte = idCorte; }

    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }

    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }

    public BigDecimal getMontoInicial() { return montoInicial; }
    public void setMontoInicial(BigDecimal montoInicial) { this.montoInicial = montoInicial; }

    public BigDecimal getMontoFinalCalculado() { return montoFinalCalculado; }
    public void setMontoFinalCalculado(BigDecimal montoFinalCalculado) { this.montoFinalCalculado = montoFinalCalculado; }

    public BigDecimal getMontoFinalReal() { return montoFinalReal; }
    public void setMontoFinalReal(BigDecimal montoFinalReal) { this.montoFinalReal = montoFinalReal; }

    public BigDecimal getDiferencia() { return diferencia; }
    public void setDiferencia(BigDecimal diferencia) { this.diferencia = diferencia; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
}