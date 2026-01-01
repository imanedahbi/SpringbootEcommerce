package com.ace.userservice.controller;
import com.ace.userservice.model.User;
import com.ace.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Inscription
    // Nouveau
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Email déjà utilisé ou données invalides"));
        }
    }



    // Connexion




    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        return userService.loginUser(user.getEmail(), user.getPassword())
                .map(u -> ResponseEntity.ok(Map.of(
                        "message", "Connexion réussie",
                        "userId", u.getId(),
                        "userEmail", u.getEmail(),
                        "role", u.getRole()

                )))
                .orElse(ResponseEntity.status(401)
                        .body(Map.of("message", "Email ou mot de passe incorrect")));
    }




    // Récupérer un utilisateur par email
    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(user))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(Map.of("message", "Utilisateur non trouvé")));
    }

    @PutMapping("/{email}")
    public ResponseEntity<?> updateUser(@PathVariable String email, @RequestBody User updatedUser) {
        return userService.findByEmail(email)
                .map(user -> {
                    try {
                        User savedUser = userService.updateUser(user, updatedUser);
                        return ResponseEntity.ok(Map.of(
                                "message", "Profil mis à jour avec succès",
                                "user", savedUser
                        ));
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(Map.of("message", "Utilisateur non trouvé")));
    }



}
