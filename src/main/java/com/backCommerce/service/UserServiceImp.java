package com.backCommerce.service;

import com.backCommerce.dto.UserDto;
import com.backCommerce.model.Role;
import com.backCommerce.model.RoleType;
import com.backCommerce.model.User;
import com.backCommerce.repository.RoleRepository;
import com.backCommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ResponseEntity<Map<String, String>> login(String username, String password) {
        // Buscar el usuario por su nombre de usuario
        Optional<User> optionalUser = userRepository.findByUsernameAndPasswordWithRoles(username, password);

        // Si el usuario no existe, retornar un mensaje con código 404
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(Map.of(
                "message", "Usuario o contraseña incorrectos",
                "userName", username
            ), HttpStatus.UNAUTHORIZED);
        }

        User user = optionalUser.get(); // Obtén el usuario del Optional
        
        // Roles del usuario se muestran como un string separado por comas
        String rolesStr = user.getRoles().stream()
                .map(role -> role.getName().toString())
                .collect(Collectors.joining(","));

        return new ResponseEntity<>(Map.of(
            "message", "Login exitoso",
            "userName", user.getUsername(),
            "roles", rolesStr
        ), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<UserDto>> getUsers() {
        try {
            List<UserDto> users = userRepository.findAll()
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<UserDto> getUserById(Long id) {
        try {
            UserDto user = userRepository.findById(id)
                    .map(this::convertToDTO)
                    .orElse(null);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> createUser(UserDto userDto) {
        if (userDto.getUsername() == null || userDto.getUsername().trim().isEmpty()) {
            return new ResponseEntity<>("El nombre de usuario no puede estar vacío", HttpStatus.BAD_REQUEST);
        }
        if (userDto.getPassword() == null || userDto.getPassword().trim().isEmpty()) {
            return new ResponseEntity<>("La contraseña no puede estar vacía", HttpStatus.BAD_REQUEST);
        }

        try {
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            user.setCreationDate(LocalDateTime.now());
            
            // Lista de roles
            user.setRoles(new ArrayList<>());

            // Agregar CLIENT como rol por defecto
            Optional<Role> clientRole = roleRepository.findByName(RoleType.CLIENT);
            if (clientRole.isPresent()) {
                user.getRoles().add(clientRole.get());
            } else {
                return new ResponseEntity<>("Error: Rol CLIENT no encontrado", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            userRepository.save(user);

            return new ResponseEntity<>("Usuario creado exitosamente", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear usuario: " + e.getMessage(), 
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            String message = "Usuario con ID " + id + " fue eliminado a las " + LocalDateTime.now();
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<UserDto> updateUser(Long userId, UserDto userDto) {
        try {
            Optional<User> existingUser = userRepository.findById(userId);
            if (existingUser.isPresent()) {
                User updatedUser = existingUser.get();
                updatedUser.setUsername(userDto.getUsername());

                // Actualizar los roles obtenidos del DTO
                if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
                    List<Role> roles = userDto.getRoles().stream()
                            .map(roleType -> roleRepository.findByName(roleType).orElse(null))
                            .filter(role -> role != null)
                            .collect(Collectors.toList());
                    updatedUser.setRoles(roles);
                }

                userRepository.save(updatedUser);
                return new ResponseEntity<>(convertToDTO(updatedUser), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UserDto convertToDTO(User user) {
        UserDto userDTO = new UserDto();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setCreationDate(user.getCreationDate());
        userDTO.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
        return userDTO;
    }
}
