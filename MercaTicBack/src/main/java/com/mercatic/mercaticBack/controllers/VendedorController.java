package com.mercatic.mercaticBack.controllers;

import com.mercatic.mercaticBack.entities.Producto;
import com.mercatic.mercaticBack.services.VendedorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/vendedor")
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    @PostMapping("/crearProducto")
    public ResponseEntity<?> crearProducto(@RequestParam("nombre_producto") String nombre,
                                           @RequestParam("precio") Double precio,
                                           @RequestParam("stock") Integer stock,
                                           @RequestParam("formato_producto") String formato,
                                           @RequestParam("categoria") String categoria,
                                           @RequestParam("imagen") MultipartFile imagen,
                                           HttpSession session) throws IOException {

        Producto p = vendedorService.crearProducto(nombre, precio,stock, formato, categoria, imagen, session);
        return ResponseEntity.ok(p);
    }
    @GetMapping("/listar")
    public ResponseEntity<?> listarProductos(HttpSession session) {
        List<Producto> productos = vendedorService.listarProductosVendedor(session);
        return ResponseEntity.ok(productos);
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<?> modificar(@PathVariable Long id,
                                       @RequestParam("nombre_producto") String nombre,
                                       @RequestParam("precio") Double precio,
                                       @RequestParam("stock") Integer stock,
                                       @RequestParam("formato_producto") String formato,
                                       @RequestParam("categoria") String categoria,
                                       @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                                       HttpSession session) throws IOException {

        Producto p = vendedorService.modificarProducto(id, nombre, precio,stock, formato, categoria, imagen, session);
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id, HttpSession session) throws IOException {
        vendedorService.borrarProducto(id, session);
        return ResponseEntity.ok("Producto borrado correctamente");
    }
}
