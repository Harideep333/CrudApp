package com.EMS.CrudApp.Config;

import com.EMS.CrudApp.Service.CustomUserDetailsService;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Harideep Reddy Boothpur
 * @created 4/27/25 11:38 PM
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    private CustomUserDetailsService customUserDetailsService;




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/employee/directory/delete/**").hasRole("MANAGER")
                                .requestMatchers("/clockin").hasRole("EMPLOYEE")
                        .requestMatchers("/employee/directory").hasRole("MANAGER")
                                .requestMatchers("/manager/attendance").hasRole("MANAGER")
                        .requestMatchers("/manager/**").hasRole("MANAGER")  // Secure manager-specific URLs
                        // Employee-specific access
                        .requestMatchers("/employee/**").hasRole("EMPLOYEE") // Secure employee-specific URLs
                        // Shared access (if any) - configure as needed
                        .requestMatchers("/shared/**").hasAnyRole("MANAGER", "EMPLOYEE")
                        // Open access for login and static resources
                        .requestMatchers("/login", "/register", "/register/save", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                        // All other requests require authentication
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                        // Set the default success URL based on the role after login.
                        .successHandler((request, response, authentication) -> {
                            String targetUrl = "/employee-dashboard"; // Default URL for employees
                            if (authentication.getAuthorities().stream()
                                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MANAGER"))) {
                                targetUrl = "/manager-dashboard"; // Redirect managers to /manager
                            }
                            response.sendRedirect(targetUrl);
                        })

                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Default logout URL
                        .logoutSuccessUrl("/login") // Redirect to login after logout
                        .invalidateHttpSession(true) // Invalidate session
                        .deleteCookies("JSESSIONID") // Clear session cookies
                );

        http.addFilterBefore(new HiddenHttpMethodFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails employee = User.withUsername("employee")
//                .password(passwordEncoder.encode("employeepass"))
//                .roles("EMPLOYEE")
//                .build();
//
//        UserDetails manager = User.withUsername("manager")
//                .password(passwordEncoder.encode("managerpass"))
//                .roles("MANAGER")
//                .build();
//
//        return new InMemoryUserDetailsManager(employee, manager);
//    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
