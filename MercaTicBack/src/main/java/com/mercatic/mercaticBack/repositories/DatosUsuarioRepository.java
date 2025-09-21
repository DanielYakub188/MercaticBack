package com.mercatic.mercaticBack.repositories;

import com.mercatic.mercaticBack.entities.DatosUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatosUsuarioRepository extends JpaRepository<DatosUsuario, Long> {
}
