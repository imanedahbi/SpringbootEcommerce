package com.ace.userservice.config;

import com.ace.userservice.model.User;
import com.ace.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                User admin = new User();
                admin.setEmail("admin@gmail.com");
                admin.setName("admin");
                admin.setPassword(encoder.encode("admin"));
                admin.setRole("ROLE_ADMIN");

                userRepository.save(admin);

                System.out.println("✅ Compte ADMIN créé avec succès");
            } else {
                System.out.println("ℹ️ Compte ADMIN existe déjà");
            }
        };
    }
}
