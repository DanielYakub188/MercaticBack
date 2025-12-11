package com.mercatic.mercaticBack.repositories;

import com.mercatic.mercaticBack.entities.Carrito;
import com.mercatic.mercaticBack.entities.Producto;
import com.mercatic.mercaticBack.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarritoRepository extends JpaRepository<Carrito,Long> {
    List<Carrito> findByUsuarioAndCompradoFalse(Usuario usuario);

    Carrito findByUsuarioAndProductoAndCompradoFalse(Usuario usuario, Producto producto);

    void deleteByUsuarioAndProducto(Usuario usuario, Producto producto);

    void deleteByUsuario(Usuario usuario);
}
