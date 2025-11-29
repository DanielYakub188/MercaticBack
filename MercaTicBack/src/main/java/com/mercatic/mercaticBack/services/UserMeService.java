package com.mercatic.mercaticBack.services;


import com.mercatic.mercaticBack.entities.DatosUsuario;
import com.mercatic.mercaticBack.entities.Usuario;
import com.mercatic.mercaticBack.modells.dto.AuthRequest;
import com.mercatic.mercaticBack.modells.dto.UserMeResponse;
import com.mercatic.mercaticBack.repositories.DatosUsuarioRepository;
import com.mercatic.mercaticBack.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserMeService {


    @Autowired
    private DatosUsuarioRepository datosUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Devuelve un DTO con los datos del usuario (combinado Usuario + DatosUsuario).
     * Retorna null si no existe el usuario.
     */
    /**
     * Devuelve un DTO con los datos combinados de Usuario + DatosUsuario.
     */
    public UserMeResponse getByUsuarioId(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            return null;
        }
        Usuario usuario = usuarioOpt.get();

        Optional<DatosUsuario> datosOpt = datosUsuarioRepository.findByUsuario_Id(usuarioId);
        DatosUsuario datos = datosOpt.orElse(null);

        return mapToResponse(usuario, datos);
    }

    private UserMeResponse mapToResponse(Usuario usuario, DatosUsuario datos) {
        UserMeResponse r = new UserMeResponse();
        r.setId(usuario.getId());
        r.setEmail(usuario.getCorreoElectronico());
        r.setRole(usuario.getRol());
        r.setBalance(usuario.getBalance()); // 🔹 balance

        if (datos != null) {
            r.setNombre(datos.getNombre());
            r.setPrimerApellido(datos.getPrimer_apellido());
            r.setSegundoApellido(datos.getSegundo_apellido());
            r.setLocalidad(datos.getLocalidad());
            r.setDireccion(datos.getDireccion());
        }
        return r;
    }

}
