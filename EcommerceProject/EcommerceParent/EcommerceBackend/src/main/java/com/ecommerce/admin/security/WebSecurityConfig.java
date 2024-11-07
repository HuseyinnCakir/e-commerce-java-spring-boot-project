package com.ecommerce.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Bean
    public  UserDetailsService UserDetailsService(){
        return  new EcommerceUserDetailsService();
    }

    @Bean
    public PasswordEncoder PasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(UserDetailsService());
        authProvider.setPasswordEncoder(PasswordEncoder());
        return authProvider;
    }



    @Bean
    SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/**").hasAuthority("Admin")
                        .requestMatchers("/categories/**","/brands/**").hasAnyAuthority("Admin","Editor")
                        .requestMatchers("/products/**").hasAnyAuthority("Admin","Editor","SalesPerson","Shipper")
                        .anyRequest()
                .authenticated()).formLogin(form -> form
                .loginPage("/login").usernameParameter("email").permitAll())
                .logout(logout -> logout.permitAll())
                .rememberMe(rem -> rem
                        .key("key123")
                        .tokenValiditySeconds(7 * 24 * 60 * 60)
                );
        return  http.build();

    }
    @Bean
    WebSecurityCustomizer configureWebSecurity() throws  Exception{
    return  (web -> web.ignoring().requestMatchers("/images/**","/js/**","/webjars/**"));
    }
}
