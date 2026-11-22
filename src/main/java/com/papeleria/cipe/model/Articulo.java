package com.papeleria.cipe.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "articulos")
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_articulo")
    private Long idArticulo;

    @Column(name = "codigo_barras", length = 100)
    private String codigoBarras;

    @Column(length = 200, nullable = false)
    private String nombre;

    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @Column(name = "id_ubicacion")
    private Integer idUbicacion;

    @Column(name = "costo_compra", precision = 10, scale = 2, nullable = false)
    private BigDecimal costoCompra = BigDecimal.ZERO;

    @Column(name = "margen_ganancia", precision = 5, scale = 2, nullable = false)
    private BigDecimal margenGanancia = BigDecimal.ZERO;

    @Column(name = "precio_final", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioFinal = BigDecimal.ZERO;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual = 0;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 1;

    // Getters y Setters
    public Long getIdArticulo() { return idArticulo; }
    public void setIdArticulo(Long idArticulo) { this.idArticulo = idArticulo; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }

    public Integer getIdUbicacion() { return idUbicacion; }
    public void setIdUbicacion(Integer idUbicacion) { this.idUbicacion = idUbicacion; }

    public BigDecimal getCostoCompra() { return costoCompra; }
    public void setCostoCompra(BigDecimal costoCompra) { this.costoCompra = costoCompra; }

    public BigDecimal getMargenGanancia() { return margenGanancia; }
    public void setMargenGanancia(BigDecimal margenGanancia) { this.margenGanancia = margenGanancia; }

    public BigDecimal getPrecioFinal() { return precioFinal; }
    public void setPrecioFinal(BigDecimal precioFinal) { this.precioFinal = precioFinal; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
}