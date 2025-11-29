package com.mercatic.mercaticBack.config;

import com.mercatic.mercaticBack.entities.DatosUsuario;
import com.mercatic.mercaticBack.entities.Usuario;
import com.mercatic.mercaticBack.repositories.DatosUsuarioRepository;
import com.mercatic.mercaticBack.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class DataInitializr {

    @Autowired
    DatosUsuarioRepository datosUsuarioRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            if (usuarioRepository.count() == 0) { // solo si no hay usuarios aún
                String rawPassword = "dani123";
                String encodedPassword = passwordEncoder().encode(rawPassword);

                // === Crear usuarios ===
                Usuario vendedor = new Usuario("vendedor@mercatic.com", encodedPassword, "vendedor");
                Usuario cliente = new Usuario("cliente@mercatic.com", encodedPassword, "cliente");
                Usuario admin = new Usuario("admin@mercatic.com", encodedPassword, "administrador");

                usuarioRepository.save(vendedor);
                usuarioRepository.save(cliente);
                usuarioRepository.save(admin);

                // === Crear datos de usuario ===
                DatosUsuario datosVendedor = new DatosUsuario();
                datosVendedor.setUsuarioId(vendedor);
                datosVendedor.setNombre("Carlos");
                datosVendedor.setPrimer_apellido("Martínez");
                datosVendedor.setSegundo_apellido("López");
                datosVendedor.setLocalidad("Madrid");
                datosVendedor.setDireccion("Calle Mayor 123");

                DatosUsuario datosCliente = new DatosUsuario();
                datosCliente.setUsuarioId(cliente);
                datosCliente.setNombre("Laura");
                datosCliente.setPrimer_apellido("Gómez");
                datosCliente.setSegundo_apellido("Pérez");
                datosCliente.setLocalidad("Barcelona");
                datosCliente.setDireccion("Avenida Diagonal 45");

                DatosUsuario datosAdmin = new DatosUsuario();
                datosAdmin.setUsuarioId(admin);
                datosAdmin.setNombre("Ana");
                datosAdmin.setPrimer_apellido("Rodríguez");
                datosAdmin.setSegundo_apellido("Fernández");
                datosAdmin.setLocalidad("Valencia");
                datosAdmin.setDireccion("Plaza del Ayuntamiento 7");

                datosUsuarioRepository.save(datosVendedor);
                datosUsuarioRepository.save(datosCliente);
                datosUsuarioRepository.save(datosAdmin);

                System.out.println("✅ Usuarios iniciales creados con éxito (3 tipos: vendedor, cliente, administrador).");
            }

        };
    }

}
