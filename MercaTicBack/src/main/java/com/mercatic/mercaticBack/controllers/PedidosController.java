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

    // Crear pedidos desde el carrito (posiblemente varios pedidos, uno por vendedor)
    @PostMapping("/crear")
    public List<Pedidos> crearPedido(HttpSession session) {
        return pedidosService.crearPedidosPorVendedor(session);
    }
    //Eliminar pedido
    @DeleteMapping("/eliminar/{id}")
    public boolean eliminarPedido(@PathVariable Long id, HttpSession session) {
        return pedidosService.eliminarPedido(id, session);
    }
    // Completar un pedido en curso
    @PostMapping("/completar/{id}")
    public boolean completarPedido(@PathVariable Long id, HttpSession session) {
        return pedidosService.completarPedidos(id, session);
    }

    // Cancelar un pedido en curso
    @PostMapping("/cancelar/{id}")
    public boolean cancelarPedido(@PathVariable Long id, HttpSession session) {
        return pedidosService.cancelarPedidos(id, session);
    }

    // Listar pedidos en curso del cliente
    @GetMapping("/en-curso")
    public List<Pedidos> listarPedidosEnCurso(HttpSession session) {
        return pedidosService.listarPedidosEnCurso(session);
    }

    // Listar pedidos finalizados del cliente
    @GetMapping("/finalizados")
    public List<Pedidos> listarPedidosFinalizados(HttpSession session) {
        return pedidosService.listarPedidosFinalizados(session);
    }

    // Listar pedidos que contienen productos del vendedor logeado
    @GetMapping("/mis-productos")
    public List<Pedidos> listarPedidosDelVendedor(HttpSession session) {
        return pedidosService.listarPedidosDelVendedor(session);
    }
}
