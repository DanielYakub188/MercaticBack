package com.mercatic.mercaticBack.services;

import com.mercatic.mercaticBack.entities.Producto;
import com.mercatic.mercaticBack.entities.Usuario;
import com.mercatic.mercaticBack.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
@Service
public class VendedorService {

    @Autowired
    private ProductoRepository productoRepository;

    private final String UPLOAD_DIR = "uploads/productos/";

    public Producto crearProducto(String nombre,
                                  Double precio,
                                  Integer stock,
                                  String formato,
                                  String categoria,
                                  MultipartFile imagen,
                                  HttpSession session) throws IOException {

        Usuario vendedor = (Usuario) session.getAttribute("usuario");
        System.out.println(vendedor);
        if (vendedor == null) {
            throw new RuntimeException("No hay un vendedor logeado en la sesión.");
        }

        // Crear carpeta si no existe
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generar nombre único para la imagen
        String nombreImagen = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
        Path rutaImagen = uploadPath.resolve(nombreImagen);

        // Guardar físicamente la imagen
        Files.copy(imagen.getInputStream(), rutaImagen, StandardCopyOption.REPLACE_EXISTING);

        // Crear y guardar producto
        Producto p = new Producto();
        p.setUsuario(vendedor);
        p.setNombreProducto(nombre);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setFormatoProducto(formato);
        p.setCategoria(categoria);
        p.setImagenUrl(nombreImagen);

        return productoRepository.save(p);
    }


    public List<Producto> listarProductosVendedor(HttpSession session) {
        Usuario vendedor = (Usuario) session.getAttribute("usuario");

        if (vendedor == null) {
            throw new RuntimeException("No hay un vendedor logeado en la sesión.");
        }

        return productoRepository.findByUsuarioId(vendedor.getId());
    }


    public Producto modificarProducto(Long id,
                                      String nombre,
                                      Double precio,
                                      Integer stock,
                                      String formato,
                                      String categoria,
                                      MultipartFile imagen,
                                      HttpSession session) throws IOException {

        Usuario vendedor = (Usuario) session.getAttribute("usuario");

        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (!p.getUsuario().getId().equals(vendedor.getId())) {
            throw new RuntimeException("No tienes permiso para modificar este producto");
        }

        p.setNombreProducto(nombre);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setFormatoProducto(formato);
        p.setCategoria(categoria);

        if (imagen != null && !imagen.isEmpty()) {
            // Modificar imagen → borrar la vieja + guardar la nueva
            Path viejaRuta = Paths.get(UPLOAD_DIR + p.getImagenUrl());
            if (Files.exists(viejaRuta)) {
                Files.delete(viejaRuta);
            }

            String nuevoNombre = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
            Files.copy(imagen.getInputStream(),
                    Paths.get(UPLOAD_DIR + nuevoNombre),
                    StandardCopyOption.REPLACE_EXISTING);
            p.setImagenUrl(nuevoNombre);
        }

        return productoRepository.save(p);
    }


    public void borrarProducto(Long id, HttpSession session) throws IOException {
        Usuario vendedor = (Usuario) session.getAttribute("usuario");

        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (!p.getUsuario().getId().equals(vendedor.getId())) {
            throw new RuntimeException("No tienes permiso para borrar este producto");
        }

        // Borrar imagen del servidor
        Path ruta = Paths.get(UPLOAD_DIR + p.getImagenUrl());
        if (Files.exists(ruta)) {
            Files.delete(ruta);
        }

        productoRepository.delete(p);
    }

}
