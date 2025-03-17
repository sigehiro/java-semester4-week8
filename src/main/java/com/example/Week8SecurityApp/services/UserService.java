package com.example.Week8SecurityApp.services;

import com.example.Week8SecurityApp.models.MyUser;
import com.example.Week8SecurityApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.passwordEncoder = passwordEncoder;
    }

    //save the user to thr database
    //0 - user already exists. 1- user saved successfully
    public int saveUser(MyUser user) {
        //check if the user already exists
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            return 0;
        }

        //encrypt the password before saving.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //setting Role
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER"); // デフォルトの役割をUSERに設定
        }else if(user.getRole().equals("ADMIN")){
            user.setRole("ADMIN");
        }

        //encrypt the password.
        userRepository.save(user);
        return 1;
    }
}
