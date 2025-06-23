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
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();

        // Перевірка довжини імені
        if (username == null || username.length() < 3 || username.length() > 8) {
            throw new Exception("Username must be between 3 and 8 characters");
        }

        // Перевірка формату email і домену gmail.com
        if (email == null || !email.matches("^[\\w.-]+@gmail\\.com$")) {
            throw new Exception("Email must be a valid gmail.com address");
        }

        // Перевірка довжини пароля
        if (password == null || password.length() < 4 || password.length() > 10) {
            throw new Exception("Password must be between 4 and 10 characters");
        }

        // Перевірка на існуючий email
        if (userRepository.existsByEmail(email)) {
            throw new Exception("Email already exists");
        }

        // Перевірка на існуюче ім'я користувача
        if (userRepository.existsByUsername(username)) {
            throw new Exception("Username already exists");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        user.setPassword(hashedPassword);
        user.setRole(Role.USER);

        return userRepository.save(user);
    }


//    public User registerUser(User user) throws Exception {
//        if (userRepository.existsByEmail(user.getEmail())) {
//            throw new Exception("Email already exists");
//        }
//
//        if (userRepository.existsByUsername(user.getUsername())) {
//            throw new Exception("Username already exists");
//        }
//
//        String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
//        user.setPassword(hashedPassword);
//        user.setRole(Role.USER);
//
//        return userRepository.save(user);
//    }
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

    public User updateUser(User user) {
        return userRepository.save(user);
    }

}
