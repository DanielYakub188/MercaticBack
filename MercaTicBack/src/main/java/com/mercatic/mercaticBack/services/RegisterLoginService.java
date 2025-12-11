package com.mercatic.mercaticBack.services;

import com.mercatic.mercaticBack.entities.DatosUsuario;
import com.mercatic.mercaticBack.entities.Usuario;
import com.mercatic.mercaticBack.modells.dto.AuthRequest;
import com.mercatic.mercaticBack.modells.dto.RegisterRequest;
import com.mercatic.mercaticBack.repositories.DatosUsuarioRepository;
import com.mercatic.mercaticBack.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class RegisterLoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DatosUsuarioRepository datosUsuarioRepository;

    // Registro
    public String register(RegisterRequest request) {
        // Verificar si ya existe el email
        if (usuarioRepository.existsByCorreoElectronico(request.getEmail())) {
            return "El correo ya está registrado";
        }

        // Crear usuario y hashear contraseña
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        Usuario usuario = new Usuario(request.getEmail(), hashedPassword, "cliente"); // por defecto cliente
        usuarioRepository.save(usuario);

        // Crear datos del usuario
        DatosUsuario datos = new DatosUsuario();
        datos.setUsuarioId(usuario);
        datos.setNombre(request.getNombre());
        datos.setPrimer_apellido(request.getApellido1());
        datos.setSegundo_apellido(request.getApellido2());
        datos.setLocalidad(request.getLocalidad());
        datos.setDireccion(request.getDireccion());
        datosUsuarioRepository.save(datos);

        return "Registro exitoso";
    }
    public String registerSeller(RegisterRequest request) {
        // Verificar si ya existe el email
        if (usuarioRepository.existsByCorreoElectronico(request.getEmail())) {
            return "El correo ya está registrado";
        }

        // Crear usuario y hashear contraseña
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        Usuario usuario = new Usuario(request.getEmail(), hashedPassword, "vendedor"); // por defecto cliente
        usuarioRepository.save(usuario);

        // Crear datos del usuario
        DatosUsuario datos = new DatosUsuario();
        datos.setUsuarioId(usuario);
        datos.setNombre(request.getNombre());
        datos.setPrimer_apellido(request.getApellido1());
        datos.setSegundo_apellido(request.getApellido2());
        datos.setLocalidad(request.getLocalidad());
        datos.setDireccion(request.getDireccion());
        datosUsuarioRepository.save(datos);

        return "Registro exitoso";
    }

    // Login
    public Usuario login(AuthRequest request) {
        Usuario usuario = usuarioRepository.findByCorreoElectronico(request.getEmail())
                .orElse(null);

        if (usuario == null) {
            return null;
        }

        if (!BCrypt.checkpw(request.getPassword(), usuario.getPassword())) {
            return null;
        }

        return usuario;
    }
}
