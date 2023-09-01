package ru.skypro.lessons.springboot.security_web;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@SuppressWarnings("removal")
@AllArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Autowired
    // Внедряем зависимость UserDetailsService
    // для работы с данными пользователя.
    private UserDetailsService userDetailsService;

    @Bean
    // Создаем экземпляр PasswordEncoder для шифрования паролей.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // Создаем экземпляр DaoAuthenticationProvider
    // для работы с аутентификацией через базу данных.
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        // Устанавливаем наш созданный экземпляр PasswordEncoder
        // для возможности использовать его при аутентификации.
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(this::customizeRequest);
        // Цепочка фильтров безопасности для обработки входящих запросов,
        // основанная на настройках безопасности,
        // определенных с помощью метода 'customizeRequest'.
        return http.build();
    }

    private void customizeRequest(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        try {
            //Устанавливаем права на доступ к эндпойнтам
                    registry.requestMatchers(HttpMethod.POST, "/employee/**", "/report/**","/**")
                            .hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/employee/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/employee/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/employee/**", "/report/**")
                            .hasAnyRole("ADMIN", "USER")
                            .requestMatchers(new AntPathRequestMatcher("/**"))
                    .hasAnyRole("USER", "ADMIN")

                    .and()
                    .formLogin().permitAll()
                    // Позволяем всем пользователям доступ к форме входа.
                    .and()
                    .logout().logoutUrl("/logout");
            // Настраиваем механизм выхода из системы
            // с заданием URL "/logout".

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}