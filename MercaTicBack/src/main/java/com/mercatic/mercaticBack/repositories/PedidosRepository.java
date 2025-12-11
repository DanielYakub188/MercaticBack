package com.mercatic.mercaticBack.repositories;

import com.mercatic.mercaticBack.entities.Pedidos;
import com.mercatic.mercaticBack.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidosRepository extends JpaRepository<Pedidos, Long> {

    List<Pedidos> findByUsuarioAndEstado(Usuario usuario, String estado);

}
