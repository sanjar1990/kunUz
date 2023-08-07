
package com.example.config;
import com.example.utility.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {
    @Autowired
    @Qualifier("customUserDetailsService")
    private UserDetailsService userDetailsService;

//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        UserDetails user= User.builder()
//                .username("user")
//                .password("{noop}12345")
//                .roles("USER")
//                .build();
//        UserDetails admin=User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$10$uWPqHv3Bk/4ruecj0xUtmeZDUhYJrdPUTGafCZUeydMMcPFWGx8Xa")
//                .roles("ADMIN")
//                .build();
//        final DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(user,admin));
//    return authenticationProvider;
//    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        //authentication login password
        final DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    private PasswordEncoder passwordEncoder(){
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return MD5Util.encode(rawPassword.toString());
            }
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return MD5Util.encode(rawPassword.toString()).equals(encodedPassword);
            }
        };
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        //authorization
        http.authorizeHttpRequests((request)->
                 request
                         .requestMatchers("/api/v1/auth/**").permitAll()
                         .requestMatchers("/api/v1/article/public/**").permitAll()
                         .requestMatchers("/api/v1/articleType/public/**").permitAll()
                         .requestMatchers("/api/v1/category/public/**").permitAll()
                         .requestMatchers(HttpMethod.GET,"/api/v1/region/language").permitAll()
                         .requestMatchers("/api/v1/attach/open/**").permitAll()
                         .requestMatchers("/api/v1/region/admin/**").hasRole("ADMIN")
                         .requestMatchers("/api/v1/attach/admin/**").hasAnyRole("ADMIN","MODERATOR")
                         .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        http.csrf(c->c.disable()).cors(AbstractHttpConfigurer::disable);
        return http.build();
    }
}




