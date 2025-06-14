package com.OnlineQuest.OnlineQuest.service;

import com.OnlineQuest.OnlineQuest.model.BCrypt.PasswordUtil;
import com.OnlineQuest.OnlineQuest.model.Role;
import com.OnlineQuest.OnlineQuest.model.User;
import com.OnlineQuest.OnlineQuest.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) throws Exception {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email already exists");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new Exception("Username already exists");
        }

        String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole(Role.USER);

        return userRepository.save(user);
    }
    /**
     * Authorization
     */
    public User loginUser(String email, String rawpassword) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));

        if (!PasswordUtil.checkPassword(rawpassword, user.getPassword())) {
            throw new Exception("Incorrect password");
        }

        return user;
    }

    /**
     * Find User by email
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
