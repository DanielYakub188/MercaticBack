package com.mercatic.mercaticBack.repositories;

import com.mercatic.mercaticBack.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto,Long> {
    List<Producto> findByUsuarioId(Long usuarioId);
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre);
}
