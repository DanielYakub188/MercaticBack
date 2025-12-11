package com.mercatic.mercaticBack.controllers;

import com.mercatic.mercaticBack.entities.Pedidos;
import com.mercatic.mercaticBack.services.PedidosService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidosController {

    @Autowired
    private PedidosService pedidosService;

    @PostMapping("/crear")
    public Pedidos crearPedido(HttpSession session) {
        return pedidosService.crearPedidos(session);
    }

    @PostMapping("/completar/{id}")
    public boolean completarPedido(@PathVariable Long id, HttpSession session) {
        return pedidosService.completarPedidos(id, session);
    }

    @PostMapping("/cancelar/{id}")
    public boolean cancelarPedido(@PathVariable Long id, HttpSession session) {
        return pedidosService.cancelarPedidos(id, session);
    }

    @GetMapping("/en-curso")
    public List<Pedidos> listarPedidosEnCurso(HttpSession session) {
        return pedidosService.listarPedidosEnCurso(session);
    }

    @GetMapping("/finalizados")
    public List<Pedidos> listarPedidosFinalizados(HttpSession session) {
        return pedidosService.listarPedidosFinalizados(session);
    }
}
