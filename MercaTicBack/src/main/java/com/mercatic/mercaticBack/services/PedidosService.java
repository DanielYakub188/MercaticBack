package com.mercatic.mercaticBack.services;

import com.mercatic.mercaticBack.entities.Carrito;
import com.mercatic.mercaticBack.entities.Pedidos;
import com.mercatic.mercaticBack.entities.Producto;
import com.mercatic.mercaticBack.entities.Usuario;
import com.mercatic.mercaticBack.repositories.CarritoRepository;
import com.mercatic.mercaticBack.repositories.PedidosRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidosService {

    @Autowired
    private PedidosRepository PedidosRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    private static final double ENVIO = 5.0;

    @Transactional
    public Pedidos crearPedidos(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return null;

        // Obtener los productos del carrito
        List<Carrito> carrito = carritoRepository.findByUsuarioAndCompradoFalse(usuario);
        if (carrito.isEmpty()) return null;

        Pedidos Pedidos = new Pedidos();
        Pedidos.setUsuario(usuario);
        Pedidos.setEstado("EN_CURSO");

        List<Producto> productos = carrito.stream().map(Carrito::getProducto).collect(Collectors.toList());
        Pedidos.setProductos(productos);

        // Calcular total: suma precios * unidades + 5 de envío
        double total = carrito.stream().mapToDouble(c -> c.getProducto().getPrecio() * c.getUnidades()).sum() + ENVIO;
        Pedidos.setTotal(total);

        // Guardar Pedidos
        Pedidos saved = PedidosRepository.save(Pedidos);

        // Marcar productos como comprados en carrito
        carrito.forEach(c -> {
            c.setComprado(true);
            carritoRepository.save(c);
        });

        return saved;
    }

    @Transactional
    public boolean completarPedidos(Long idPedidos, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return false;

        Pedidos Pedidos = PedidosRepository.findById(idPedidos).orElse(null);
        if (Pedidos == null || !"EN_CURSO".equals(Pedidos.getEstado())) return false;

        // Verificar balance suficiente
        if (usuario.getBalance() < Pedidos.getTotal()) return false;

        // Restar balance del usuario
        usuario.setBalance(usuario.getBalance() - Pedidos.getTotal());

        // Dar balance al vendedor de cada producto
        Pedidos.getProductos().forEach(p -> {
            Usuario vendedor = p.getUsuario();
            vendedor.setBalance(vendedor.getBalance() + p.getPrecio());
        });

        Pedidos.setEstado("FINALIZADO");
        PedidosRepository.save(Pedidos);
        return true;
    }

    @Transactional
    public boolean cancelarPedidos(Long idPedidos, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return false;

        Pedidos Pedidos = PedidosRepository.findById(idPedidos).orElse(null);
        if (Pedidos == null || !"EN_CURSO".equals(Pedidos.getEstado())) return false;

        Pedidos.setEstado("CANCELADO");
        PedidosRepository.save(Pedidos);

        // Opcional: devolver productos al stock si lo deseas
        return true;
    }

    public List<Pedidos> listarPedidosEnCurso(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return List.of();
        return PedidosRepository.findByUsuarioAndEstado(usuario, "EN_CURSO");
    }

    public List<Pedidos> listarPedidosFinalizados(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return List.of();
        return PedidosRepository.findByUsuarioAndEstado(usuario, "FINALIZADO");
    }
}
