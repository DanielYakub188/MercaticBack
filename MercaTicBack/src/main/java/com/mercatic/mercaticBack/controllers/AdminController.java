package com.mercatic.mercaticBack.controllers;

import com.mercatic.mercaticBack.modells.dto.UserMeResponse;
import com.mercatic.mercaticBack.repositories.UsuarioRepository;
import com.mercatic.mercaticBack.services.AdminService;
import com.mercatic.mercaticBack.services.UserMeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    private AdminService adminService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UserMeService userMeService;

    // Endpoint para obtener todos los usuarios
    @GetMapping("/users")
    public List<UserMeResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return adminService.getAllUsers(page, size, search, sortBy);
    }

    @PutMapping("/users/{id}")
    public UserMeResponse updateUser(@PathVariable Long id, @RequestBody UserMeResponse userDto) {
        return adminService.updateUser(id, userDto);
    }
}
