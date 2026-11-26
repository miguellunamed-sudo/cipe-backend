package com.papeleria.cipe.controller;

import com.papeleria.cipe.model.Rol;
import com.papeleria.cipe.model.Usuario;
import com.papeleria.cipe.repository.RolRepository;
import com.papeleria.cipe.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void initDefaultUsers() {
        Rol adminRol = rolRepository.findByNombre("Administrador").orElseGet(() -> {
            Rol r = new Rol();
            r.setNombre("Administrador");
            return rolRepository.save(r);
        });

        Rol empRol = rolRepository.findByNombre("Empleado").orElseGet(() -> {
            Rol r = new Rol();
            r.setNombre("Empleado");
            return rolRepository.save(r);
        });

        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombreCompleto("Administrador General");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(adminRol);
            admin.setActivo(true);
            usuarioRepository.save(admin);
        }

        if (usuarioRepository.findByUsername("cajero").isEmpty()) {
            Usuario cajero = new Usuario();
            cajero.setNombreCompleto("Cajero Mostrador");
            cajero.setUsername("cajero");
            cajero.setPassword(passwordEncoder.encode("cajero123"));
            cajero.setRol(empRol);
            cajero.setActivo(true);
            usuarioRepository.save(cajero);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String username = credenciales.getOrDefault("username", "").trim();
        String password = credenciales.getOrDefault("password", "");

        Optional<Usuario> userOpt = usuarioRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario o contraseña incorrectos");
        }

        Usuario usuario = userOpt.get();
        if (Boolean.FALSE.equals(usuario.getActivo())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Este usuario está desactivado");
        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario o contraseña incorrectos");
        }

        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping("/usuarios")
    public ResponseEntity<?> crearUsuario(@RequestBody Map<String, Object> payload) {
        String username = payload.get("username").toString().trim();
        if (usuarioRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya existe");
        }

        String rolNombre = payload.getOrDefault("rol", "Empleado").toString();
        Rol rol = rolRepository.findByNombre(rolNombre).orElseGet(() -> {
            Rol r = new Rol();
            r.setNombre(rolNombre);
            return rolRepository.save(r);
        });

        Usuario nuevo = new Usuario();
        nuevo.setNombreCompleto(payload.get("nombreCompleto").toString().trim());
        nuevo.setUsername(username);
        nuevo.setPassword(passwordEncoder.encode(payload.get("password").toString()));
        nuevo.setRol(rol);
        nuevo.setActivo(true);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(nuevo));
    }
}