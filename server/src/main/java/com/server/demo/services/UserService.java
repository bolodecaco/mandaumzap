package com.server.demo.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.RequestUserDTO;
import com.server.demo.dtos.UserDTO;
import com.server.demo.enums.PlanType;
import com.server.demo.mappers.UserMapper;
import com.server.demo.models.Plan;
import com.server.demo.models.User;
import com.server.demo.repositories.PlanRepository;
import com.server.demo.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDTOList(users);
    }

    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Usuário com ID %s não encontrado", id)));
        return userMapper.toDTO(user);
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Usuário com ID %s não encontrado", id)));
    }

    public UserDTO createUser(RequestUserDTO requestedUser) {
        User user = userMapper.toEntity(requestedUser);
        Plan freePlan = planRepository.findByType(PlanType.FREE).orElseThrow(() -> 
        new RuntimeException("Plan not found"));
        System.err.println(freePlan);
        user.setPlan(freePlan);
        User currentUser = userRepository.save(user);
        return userMapper.toDTO(currentUser);
    }

    public UserDTO updateUser(UUID id, RequestUserDTO userDetails) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Usuário com ID %s não encontrado", id)));
        userMapper.updateEntityFromDTO(userDetails, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
