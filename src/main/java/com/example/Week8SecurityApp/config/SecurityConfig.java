package com.example.Week8SecurityApp.config;

import com.example.Week8SecurityApp.handlers.CustomAuthenticationEntryPoint;
import com.example.Week8SecurityApp.services.MyUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MyUserDetailService myUserDetailService;
    public SecurityConfig(MyUserDetailService myUserDetailService) {
        this.myUserDetailService = myUserDetailService;
    }

    // Security filter chain configuration
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/restaurant/home", "login/**", "register/**").permitAll()
            .requestMatchers("/restaurant/menu/**").authenticated()
            .requestMatchers("/restaurant/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
            // Use default login page for form-based authentication
            // Configure logout behavior
//            .formLogin(Customizer.withDefaults())
            .formLogin(form -> form
//                .loginPage("/login")
//                .failureHandler(new CustomAuthenticationFailureHandler()) // カスタムハンドラを追加
                .loginPage("/login") // ログインページを指定
//                .failureUrl("/login?error") // ログイン失敗時のURL
                .defaultSuccessUrl("/restaurant/home") // 成功時のリダイレクト先
            )
                // ログアウト設定(Navでログアウトを押した際の設定)
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/restaurant/home?logout=true")
            )
//             exceptionHandlingを設定
            .exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Create a new instance of DaoAuthenticationProvider
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        // Set the password encoder to use BCryptPasswordEncoder
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        // Set the user details service to use MyUserDetailService
        daoAuthenticationProvider.setUserDetailsService(myUserDetailService);

        // Return the configured DaoAuthenticationProvider instance
        return daoAuthenticationProvider;
    }



    // Ignore security for certain static resources (e.g., CSS files, H2 console)
    @Bean
    public WebSecurityCustomizer ignoreResources() {
        return (webSecurity) -> webSecurity.ignoring().requestMatchers("/css/**", "/h2-console/**");
    }

    // Password encoder bean using BCrypt for secure password hashing
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}