package com.mercatic.mercaticBack.controllers;

import com.mercatic.mercaticBack.entities.Carrito;
import com.mercatic.mercaticBack.entities.Pedidos;
import com.mercatic.mercaticBack.services.CarritoService;
import com.mercatic.mercaticBack.services.PedidosService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private PedidosService pedidosService;



    @GetMapping("/listar")
    public List<Carrito> listarCarrito(HttpSession session) {
        return carritoService.listarCarrito(session);
    }

    @PostMapping("/add/{idProducto}")
    public Carrito añadirAlCarrito(
            @PathVariable Long idProducto,
            @RequestParam(defaultValue = "1") int unidades,
            HttpSession session
    ) {
        return carritoService.añadirProducto(idProducto, unidades, session);
    }
    @Transactional
    @DeleteMapping("/eliminar/{idProducto}")
    public boolean eliminar(@PathVariable Long idProducto, HttpSession session) {
        return carritoService.eliminarProducto(idProducto, session);
    }

    @Transactional
    @DeleteMapping("/vaciar")
    public boolean vaciar(HttpSession session) {
        return carritoService.vaciarCarrito(session);
    }

    @PostMapping("/comprar")
    public List<Pedidos> comprarCarrito(HttpSession session) {
        // Crear pedidos separados por vendedor
        List<Pedidos> pedidosCreados = pedidosService.crearPedidosPorVendedor(session);
        return pedidosCreados;
    }
}
