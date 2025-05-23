package com.backCommerce.service;

import com.backCommerce.dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {

    ResponseEntity<List<UserDto>> getUsers();

    ResponseEntity<UserDto> getUserById(Long id);

    ResponseEntity<String> createUser(UserDto userDto);

    ResponseEntity<String> deleteUser(Long id);

    ResponseEntity<UserDto> updateUser(Long userId, UserDto userDto);
    
    ResponseEntity<Map<String, String>> login(String userName, String password);

}
