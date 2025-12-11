package com.mercatic.mercaticBack.controllers;


import com.mercatic.mercaticBack.entities.Producto;
import com.mercatic.mercaticBack.services.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/busqueda")
    public List<Producto> buscarProductos(@RequestHeader(value="nombre", required=false) String nombre)
    {
        return this.clienteService.buscarProductosPorNombre(nombre);
    }
    @GetMapping("/balance")
    public Double obtenerBalance(HttpSession session) {
        return clienteService.obtenerBalance(session);
    }
}
