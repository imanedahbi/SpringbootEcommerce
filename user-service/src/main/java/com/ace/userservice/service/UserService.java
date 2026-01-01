package com.ace.userservice.service;
import com.ace.userservice.model.User;
import com.ace.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // initialisation
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    // Inscription avec hachage
    // Nouveau
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Cet email existe déjà");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_CLIENT"); // 👈 ICI EXACTEMENT
        return userRepository.save(user);

    }


    // Connexion avec vérification du mot de passe haché
    public Optional<User> loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())){
            return user;
        }
        return Optional.empty();
    }

    public User updateUser(User existingUser, User updatedUser) {
        // Mettre à jour le nom
        existingUser.setName(updatedUser.getName());

        // Mettre à jour l'email seulement si différent
        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            if (userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Cet email est déjà utilisé");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        // Mettre à jour le mot de passe seulement si fourni
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }


}
