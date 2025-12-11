package com.mercatic.mercaticBack.services;

import com.mercatic.mercaticBack.entities.DatosUsuario;
import com.mercatic.mercaticBack.entities.Usuario;
import com.mercatic.mercaticBack.modells.dto.UserMeResponse;
import com.mercatic.mercaticBack.repositories.DatosUsuarioRepository;
import com.mercatic.mercaticBack.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DatosUsuarioRepository datosUsuarioRepository;

    @Autowired
    private UserMeService userMeService;

    /**
     * Obtiene todos los usuarios con búsqueda, orden y paginación.
     */
    public List<UserMeResponse> getAllUsers(int page, int size, String search, String sortBy) {

        // Obtener todos los usuarios y mapear a UserMeResponse
        List<UserMeResponse> users = usuarioRepository.findAll()
                .stream()
                .map(u -> userMeService.getByUsuarioId(u.getId().longValue()))
                .collect(Collectors.toList());

        // Filtrado por búsqueda
        if (search != null && !search.isEmpty()) {
            String lower = search.toLowerCase();
            users = users.stream().filter(u ->
                    u.getEmail().toLowerCase().contains(lower) ||
                            u.getNombre().toLowerCase().contains(lower) ||
                            u.getPrimerApellido().toLowerCase().contains(lower) ||
                            u.getSegundoApellido().toLowerCase().contains(lower) ||
                            u.getRole().toLowerCase().contains(lower)
            ).collect(Collectors.toList());
        }

        // 3. Ordenación básica
        users.sort((a, b) -> {
            switch (sortBy) {
                case "email": return a.getEmail().compareToIgnoreCase(b.getEmail());
                case "nombre": return a.getNombre().compareToIgnoreCase(b.getNombre());
                case "role": return a.getRole().compareToIgnoreCase(b.getRole());
                default: return Long.compare(a.getId(), b.getId());
            }
        });

        // 4. Paginación manual
        int start = page * size;
        int end = Math.min(start + size, users.size());
        if (start > end) start = end;

        return users.subList(start, end);
    }

    // Actualizar usuario
    public UserMeResponse updateUser(Long id, UserMeResponse userDto) {

        // 1. Buscar usuario
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Buscar datosUsuario
        DatosUsuario datosUsuario = datosUsuarioRepository.findByUsuario_Id(id)
                .orElseThrow(() -> new RuntimeException("Datos de usuario no encontrados"));

        // 3. Actualizar campos de Usuario
        usuario.setCorreoElectronico(userDto.getEmail());
        usuario.setRol(userDto.getRole());

        usuarioRepository.save(usuario);

        // 4. Actualizar campos de DatosUsuario
        datosUsuario.setNombre(userDto.getNombre());
        datosUsuario.setPrimer_apellido(userDto.getPrimerApellido());
        datosUsuario.setSegundo_apellido(userDto.getSegundoApellido());
        datosUsuario.setLocalidad(userDto.getLocalidad());
        datosUsuario.setDireccion(userDto.getDireccion());

        datosUsuarioRepository.save(datosUsuario);

        // 5. Retornar DTO actualizado
        return userMeService.getByUsuarioId(usuario.getId());
    }
}