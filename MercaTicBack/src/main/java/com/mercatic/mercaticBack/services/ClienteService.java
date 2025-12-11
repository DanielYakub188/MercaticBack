package com.mercatic.mercaticBack.services;

import com.mercatic.mercaticBack.entities.Producto;
import com.mercatic.mercaticBack.entities.Usuario;
import com.mercatic.mercaticBack.repositories.ProductoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> buscarProductosPorNombre(String nombre)
    {
        List<Producto> productos;
        if(nombre != null || nombre.isBlank()){
            productos = productoRepository.findByNombreProductoContainingIgnoreCase(nombre);}
        else{
            productos = productoRepository.findAll();
        }
        return productos;
    }

    /*
    public List<Producto> recogerMiCestaCompra(HttpSession session)
    {
        Usuario vendedor = (Usuario) session.getAttribute("usuario");

    }*/

}
