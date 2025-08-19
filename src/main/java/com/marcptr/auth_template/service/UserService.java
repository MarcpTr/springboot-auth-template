package com.marcptr.auth_template.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.marcptr.auth_template.exceptions.ValidationException;
import com.marcptr.auth_template.model.User;
import com.marcptr.auth_template.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password){
        Map<String, List<String>> errors = new HashMap<>();

        List<String> usernameErrors = new ArrayList<>();
        if (userRepository.findByUsername(username).isPresent()) {
            usernameErrors.add("El nombre de usuario ya está en uso.");
        }
        if (!Pattern.compile("^(?=.*[A-Za-z])[A-Za-z0-9]+$")
                .matcher(username)
                .find()) {
                    usernameErrors.add("El nombre de usuario no es válido. Solo se permiten letras y números.");
        }
        if (!usernameErrors.isEmpty()) {
            errors.put("username", usernameErrors);
        }
        List<String> passwordErrors = new ArrayList<>();
        if (!Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{6,}$")
                .matcher(password)
                .find()) {
                    passwordErrors.add(
                    "La contraseña no cumple con los requisitos. Debe tener al menos 6 caracteres, una minúscula, una mayúscula y un número.");
        }
        if (!passwordErrors.isEmpty()) {
            errors.put("password", passwordErrors);
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("user");
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not fount"));
        org.springframework.security.core.userdetails.User.UserBuilder builder = org.springframework.security.core.userdetails.User
                .withUsername(username);
        builder.password(user.getPassword());
        builder.roles(user.getRole());
        return builder.build();
    }

    public boolean usernameExist(String username) {
        return userRepository.existsByUsername(username);
    }
}
