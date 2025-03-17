package com.example.Week8SecurityApp.services;

import com.example.Week8SecurityApp.models.MyUser;
import com.example.Week8SecurityApp.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.Optional;

//Spring Securityが認証プロセスを実行する際に必要なユーザーの詳細情報を提供します。
// 具体的には、ユーザー名、パスワード、役割をデータベースから取得し、
// 認証に利用できる形式で返します。これにより、
// アプリケーションはユーザーの認証と権限管理が可能になります。
@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Provide the user details to the security framework for the authentication process
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> userOp = userRepository.findByUsername(username);
        if (userOp.isPresent()) {
            MyUser user = userOp.get();
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword()) // (DBから取得したパスワード) get the password from the database
                    .roles(user.getRole())
                    .build();
        } else {
            throw new UsernameNotFoundException("Username not found");
        }
    }

}
