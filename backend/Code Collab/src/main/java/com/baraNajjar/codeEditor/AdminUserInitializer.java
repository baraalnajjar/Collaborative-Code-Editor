package com.baraNajjar.codeEditor;

import com.baraNajjar.codeEditor.entites.User;
import com.baraNajjar.codeEditor.enums.Role;
import com.baraNajjar.codeEditor.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminUsername = "admin";
        String adminPassword = "admin";
        String encryptedPassword = passwordEncoder.encode(adminPassword);

        if (!userRepository.existsByLogin(adminUsername)) {
            User adminUser = new User();
            adminUser.setLogin(adminUsername);
            adminUser.setPassword(encryptedPassword);
            adminUser.setFirstName("admin");
            adminUser.setLastName("admin");
            adminUser.setRole(Role.ADMIN);

            userRepository.save(adminUser);
            System.out.println("Admin user created: " + adminUsername);
        } else {
            System.out.println("Admin user already exists: " + adminUsername);
        }
    }
}
