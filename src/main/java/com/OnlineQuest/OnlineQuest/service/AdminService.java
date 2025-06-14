package com.OnlineQuest.OnlineQuest.service;

import com.OnlineQuest.OnlineQuest.model.Role;
import com.OnlineQuest.OnlineQuest.model.User;
import com.OnlineQuest.OnlineQuest.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get all users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Delete user by id
     */
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUserRole(Long userId, String roleName) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("Користувача з таким ID не знайдено"));

        try {
            Role newRole = Role.valueOf(roleName.toUpperCase()); // перетворюємо в enum
            user.setRole(newRole);
            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new Exception("Недійсна роль: " + roleName);
        }
    }

}
