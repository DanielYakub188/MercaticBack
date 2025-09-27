package com.mercatic.mercaticBack.controllers;


import com.mercatic.mercaticBack.entities.Usuario;
import com.mercatic.mercaticBack.modells.dto.UserMeResponse;
import com.mercatic.mercaticBack.services.UserMeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMeService userMeService;

    // Usar GET para recuperar "me"
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Object userObj = session.getAttribute("usuario");
        if (userObj == null || !(userObj instanceof Usuario)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("No hay sesión activa") {
                    });
        }

        Usuario usuario = (Usuario) userObj;
        UserMeResponse resp = userMeService.getByUsuarioId(usuario.getId());
        if (resp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Datos no encontrados"));
        }
        return ResponseEntity.ok(resp);
    }

    public static class ErrorResponse {
        private String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
