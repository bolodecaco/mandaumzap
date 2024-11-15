package com.server.demo.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.UserDTO;
import com.server.demo.models.User;
import com.server.demo.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(
                        user -> new UserDTO(
                                user.getId(),
                                user.getName(),
                                user.getPhone(),
                                user.getAvatar()
                        )
                ).collect(Collectors.toList());
    }

    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(user.getId(), user.getName(), user.getPhone(), user.getAvatar());
    }

    public UserDTO createUser(User user) {
        User currentUser = userRepository.save(user);
        return new UserDTO(currentUser.getId(), currentUser.getName(), currentUser.getPhone(), currentUser.getAvatar());
    }

    public UserDTO updateUser(UUID id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (userDetails.getName() != null) {
            user.setName(userDetails.getName());
        }
        if (userDetails.getPhone() != null) {
            user.setPhone(userDetails.getPhone());
        }
        if (userDetails.getAvatar() != null) {
            user.setAvatar(userDetails.getAvatar());
        }
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPassword() != null) {
            user.setPassword(userDetails.getPassword());
        }
        User updatedUser = userRepository.save(user);
        return new UserDTO(updatedUser.getId(), updatedUser.getName(), updatedUser.getPhone(), updatedUser.getAvatar());
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
