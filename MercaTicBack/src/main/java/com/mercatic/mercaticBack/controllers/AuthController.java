package com.mercatic.mercaticBack.controllers;
import com.mercatic.mercaticBack.modells.dto.*;
import com.mercatic.mercaticBack.entities.Usuario;
import com.mercatic.mercaticBack.repositories.UsuarioRepository;
import com.mercatic.mercaticBack.services.RegisterLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private RegisterLoginService registerLoginService;

    // Registro
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String result = registerLoginService.register(request);
        if (result.equals("Registro exitoso")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(result);
    }
    // Registro Vendedor
    @PostMapping("/registerSeller")
    public ResponseEntity<String> registerSeller(@RequestBody RegisterRequest request) {
        String result = registerLoginService.registerSeller(request);
        if (result.equals("Registro exitoso")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpSession session) {
        Usuario usuario = registerLoginService.login(request);

        if (usuario != null) {
            session.setAttribute("usuario",usuario);
            return ResponseEntity.ok(
                    new LoginResponse(
                            usuario.getId().toString(),
                            usuario.getCorreoElectronico(),
                            usuario.getRol()
                    )
            );
        }

        return ResponseEntity.status(401).body(new ErrorResponse("Credenciales inválidas"));
    }

}
