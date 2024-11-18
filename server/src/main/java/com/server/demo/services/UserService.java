package com.server.demo.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.UserDTO;
import com.server.demo.enums.PlanType;
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

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(
                        user -> new UserDTO(
                                user.getId(),
                                user.getName(),
                                user.getPhone(),
                                user.getAvatar(),
                                user.getPlan().getType()
                        )
                ).collect(Collectors.toList());
    }

    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(user.getId(), user.getName(), user.getPhone(), user.getAvatar(), user.getPlan().getType());
    }

    public UserDTO createUser(User user) {
        Plan freePlan = planRepository.findByType(PlanType.FREE).orElseThrow(() -> new RuntimeException("Plan not found"));
        user.setPlan(freePlan);
        User currentUser = userRepository.save(user);
        return new UserDTO(currentUser.getId(), currentUser.getName(), currentUser.getPhone(), currentUser.getAvatar(), currentUser.getPlan().getType());
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
        if (userDetails.getPlan() != null) {
            user.setPlan(userDetails.getPlan());
        }
        User updatedUser = userRepository.save(user);
        return new UserDTO(updatedUser.getId(), updatedUser.getName(), updatedUser.getPhone(), updatedUser.getAvatar(), updatedUser.getPlan().getType());
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
