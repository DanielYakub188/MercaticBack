package com.mercatic.mercaticBack.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ID autoincrementado del carrito

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;  // Usuario dueño del carrito

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    private Producto producto; // Producto dentro del carrito

    @Column(nullable = false)
    private Integer unidades; // Cantidad del producto en el carrito

    @Column(nullable = false)
    private boolean comprado = false; // Si el usuario ha hecho click en comprar

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

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getUnidades() {
        return unidades;
    }

    public void setUnidades(Integer unidades) {
        this.unidades = unidades;
    }

    public boolean isComprado() {
        return comprado;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }
}
