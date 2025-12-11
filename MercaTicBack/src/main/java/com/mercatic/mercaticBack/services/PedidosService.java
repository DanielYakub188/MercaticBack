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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PedidosService {

    @Autowired
    private PedidosRepository PedidosRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    private static final double ENVIO = 5.0;
    @Transactional
    public List<Pedidos> crearPedidosPorVendedor(HttpSession session) {
        Usuario cliente = (Usuario) session.getAttribute("usuario");
        if (cliente == null) return List.of();

        // Obtener productos del carrito no comprados
        List<Carrito> carrito = carritoRepository.findByUsuarioAndCompradoFalse(cliente);
        if (carrito.isEmpty()) return List.of();

        // Agrupar productos por vendedor
        Map<Usuario, List<Carrito>> carritoPorVendedor = carrito.stream()
                .collect(Collectors.groupingBy(c -> c.getProducto().getUsuario()));

        List<Pedidos> pedidosCreados = new ArrayList<>();

        for (Map.Entry<Usuario, List<Carrito>> entry : carritoPorVendedor.entrySet()) {
            Usuario vendedor = entry.getKey();
            List<Carrito> items = entry.getValue();

            Pedidos pedido = new Pedidos();
            pedido.setUsuario(cliente); // Cliente que compra
            pedido.setEstado("EN_CURSO");
            List<Producto> productos = items.stream().map(Carrito::getProducto).collect(Collectors.toList());
            pedido.setProductos(productos);

            double total = items.stream().mapToDouble(c -> c.getProducto().getPrecio() * c.getUnidades()).sum() + 5.0;
            pedido.setTotal(total);

            Pedidos saved = PedidosRepository.save(pedido);
            pedidosCreados.add(saved);

            // Marcar productos como comprados
            items.forEach(c -> {
                c.setComprado(true);
                carritoRepository.save(c);
            });
        }

        return pedidosCreados;
    }
    // Devuelve todos los pedidos que contienen productos del vendedor logueado
    public List<Pedidos> listarPedidosDelVendedor(HttpSession session) {
        Usuario vendedor = (Usuario) session.getAttribute("usuario");
        if (vendedor == null) return List.of();

        List<Pedidos> todos = PedidosRepository.findAll();

        return todos.stream()
                .filter(p -> p.getProductos().stream()
                        .anyMatch(prod -> prod.getUsuario().getId().equals(vendedor.getId())))
                .collect(Collectors.toList());
    }
    @Transactional
    public boolean eliminarPedido(Long idPedido, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return false;

        Pedidos pedido = PedidosRepository.findById(idPedido).orElse(null);
        if (pedido == null) return false;

        // Solo puede borrarlo el vendedor dueño de al menos un producto
        boolean pertenece = pedido.getProductos().stream()
                .anyMatch(p -> p.getUsuario().getId().equals(usuario.getId()));

        if (!pertenece) return false;

        PedidosRepository.delete(pedido);
        return true;
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
