package com.mercatic.mercaticBack.entities;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private DatosUsuario datosUsuario;

    @Column(name = "correo_electronico", nullable = false, unique = true)
    private String correoElectronico;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "rol", nullable = false)
    private String rol;

    @Column(name = "balance", nullable = false)
    private Double balance = 50.0; // todos empiezan con 50 por defecto

    public Usuario ()
    {

    }
    // Constructor privado para el builder
    public Usuario(String correoElectronico, String password, String rol) {
        this.correoElectronico = correoElectronico;
        this.password = password;
        this.rol = rol;
        this.balance = 50.0; // 🔹 asegura el valor al crear
    }

    public DatosUsuario getDatosUsuario() {
        return datosUsuario;
    }

    public void setDatosUsuario(DatosUsuario datosUsuario) {
        this.datosUsuario = datosUsuario;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
}
