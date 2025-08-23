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
import com.marcptr.auth_template.model.Role;
import com.marcptr.auth_template.model.User;
import com.marcptr.auth_template.repository.UserRepository;
import com.marcptr.auth_template.security.CustomUserDetails;

@Service
public class AdminService  {

    @Autowired
    private UserRepository userRepository;
   
    public List<User> listUsers(){
        List<User> user=userRepository.findAll();
        return user;
    }
}
