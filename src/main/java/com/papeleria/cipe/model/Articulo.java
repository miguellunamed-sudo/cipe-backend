package com.papeleria.cipe.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "articulos") // Nombre exacto de tu tabla en el PDF
public class Articulo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_articulo")
    private Long idArticulo;
    
    @Column(name = "codigo_barras", length = 100)
    private String codigoBarras;
    
    @Column(length = 200, nullable = false)
    private String nombre;
    
    @Column(name = "precio_final", precision = 10, scale = 2)
    private BigDecimal precioFinal;
    
    @Column(name = "stock_actual")
    private Integer stockActual;

    // Generamos los Getters y Setters
    public Long getIdArticulo() { return idArticulo; }
    public void setIdArticulo(Long idArticulo) { this.idArticulo = idArticulo; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecioFinal() { return precioFinal; }
    public void setPrecioFinal(BigDecimal precioFinal) { this.precioFinal = precioFinal; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
}