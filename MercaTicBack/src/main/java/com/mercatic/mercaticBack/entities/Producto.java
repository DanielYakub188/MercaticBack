package com.mercatic.mercaticBack.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vendedor_id")
    private Usuario usuario;

    @Column(name = "nombre_producto")
    private String nombreProducto;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "formato_producto")
    private String formatoProducto;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name= "stock")
    private Integer stock;

    @Column(name = "categoria")
    private String categoria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock(){
        return this.stock;
    }
    public void setStock(Integer stock){
        this.stock = stock;
    }

    public String getFormatoProducto() {
        return formatoProducto;
    }

    public void setFormatoProducto(String formatoProducto) {
        this.formatoProducto = formatoProducto;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
