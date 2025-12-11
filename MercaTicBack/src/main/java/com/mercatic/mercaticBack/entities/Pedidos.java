package com.mercatic.mercaticBack.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedidos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonManagedReference // 🔹 indica que al serializar el pedido, incluir los PedidoProductos pero sin volver a Pedidos
    private List<PedidoProductos> pedidoProductos;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private Double total;

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<PedidoProductos> getPedidoProductos() { return pedidoProductos; }
    public void setPedidoProductos(List<PedidoProductos> pedidoProductos) { this.pedidoProductos = pedidoProductos; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}
