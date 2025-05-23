package com.backCommerce.service;

import com.backCommerce.model.User;
import com.backCommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthServiceImp implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Map<String, String>> login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsernameAndPasswordWithRoles(username, password);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Map<String, String> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("role", user.getRoles().stream()
            .map(role -> role.getId().toString())
            .collect(Collectors.joining(",")));
            response.put("message", "Login exitoso");
            
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body(Map.of("error", "Usuario o contraseña incorrectos"));
    }
}
