package com.mercatic.mercaticBack.services;

import com.mercatic.mercaticBack.entities.Carrito;
import com.mercatic.mercaticBack.entities.Producto;
import com.mercatic.mercaticBack.entities.Usuario;
import com.mercatic.mercaticBack.repositories.CarritoRepository;
import com.mercatic.mercaticBack.repositories.ProductoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Carrito> listarCarrito(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return List.of();

        return carritoRepository.findByUsuarioAndCompradoFalse(usuario);
    }

    public Carrito añadirProducto(Long idProducto, int unidades, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return null;

        Producto producto = productoRepository.findById(idProducto).orElse(null);
        if (producto == null) return null;

        // Comprobar si ya está en el carrito
        Carrito existente = carritoRepository.findByUsuarioAndProductoAndCompradoFalse(usuario, producto);

        if (existente != null) {
            existente.setUnidades(existente.getUnidades() + unidades);
            return carritoRepository.save(existente);
        }

        Carrito c = new Carrito();
        c.setUsuario(usuario);
        c.setProducto(producto);
        c.setUnidades(unidades);
        c.setComprado(false);

        return carritoRepository.save(c);
    }

    public boolean eliminarProducto(Long idProducto, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return false;

        Producto producto = productoRepository.findById(idProducto).orElse(null);
        if (producto == null) return false;

        carritoRepository.deleteByUsuarioAndProducto(usuario, producto);
        return true;
    }

    public boolean vaciarCarrito(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return false;

        carritoRepository.deleteByUsuario(usuario);
        return true;
    }

    public boolean marcarComprado(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return false;

        List<Carrito> elementos = carritoRepository.findByUsuarioAndCompradoFalse(usuario);

        for (Carrito c : elementos) {
            c.setComprado(true);
            carritoRepository.save(c);
        }

        return true;
    }
}
