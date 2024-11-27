package com.server.demo.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(user.getId(), user.getName(), user.getPhone(), user.getAvatar(), user.getPlan().getType());
    }

    public UserDTO createUser(RequestUserDTO requestedUser) {
        User user = userMapper.toEntity(requestedUser);
        Plan freePlan = planRepository.findByType(PlanType.FREE).orElseThrow(() -> new RuntimeException("Plan not found"));
        user.setPlan(freePlan);
        User currentUser = userRepository.save(user);
        return new UserDTO(currentUser.getId(), currentUser.getName(), currentUser.getPhone(), currentUser.getAvatar(), currentUser.getPlan().getType());
    }

    public UserDTO updateUser(UUID id, RequestUserDTO userDetails) {
        User updatedUser = userMapper.toEntity(userDetails);
        updatedUser.setId(id);
        updatedUser = userRepository.save(updatedUser);
        return new UserDTO(updatedUser.getId(), updatedUser.getName(), updatedUser.getPhone(), updatedUser.getAvatar(), updatedUser.getPlan().getType());
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
