package com.mercatic.mercaticBack.repositories;

import com.mercatic.mercaticBack.entities.DatosUsuario;
import com.mercatic.mercaticBack.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DatosUsuarioRepository extends JpaRepository<DatosUsuario, Long> {

    Optional<DatosUsuario> findByUsuario_Id(Long usuarioId);
}
