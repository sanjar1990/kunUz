
package com.example.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public AuthenticationProvider authenticationProvider(){
        //Authentication
//        String password= UUID.randomUUID().toString();
//        System.out.println("User password: "+password);
        UserDetails user= User.builder()
                .username("user")
                .password("{noop}12345")
                .roles("USER")
                .build();
        UserDetails admin=User.builder()
                .username("admin")
                .password("{bcrypt}$2a$10$uWPqHv3Bk/4ruecj0xUtmeZDUhYJrdPUTGafCZUeydMMcPFWGx8Xa")
                .roles("ADMIN")
                .build();

        final DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(user,admin));
    return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        //authorization
        http.authorizeHttpRequests((request)->
                 request
                         .requestMatchers("/api/v1/auth/**").permitAll()
                         .requestMatchers("/api/v1/news/**").permitAll()
                         .requestMatchers(HttpMethod.GET,"/api/v1/region/language").permitAll()
                         .requestMatchers("/api/v1/attach/**").permitAll()
                         .requestMatchers("/api/v1/region/admin/**").hasRole("ADMIN")
                         .requestMatchers("/api/v1/attach/admin/**").hasAnyRole("ADMIN","MODERATOR")
                         .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        http.csrf(c->c.disable());
        return http.build();
    }
}




