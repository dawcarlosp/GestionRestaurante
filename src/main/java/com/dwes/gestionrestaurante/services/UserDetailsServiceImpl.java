package com.dwes.gestionrestaurante.services;

import com.dwes.gestionrestaurante.repositories.UserEntityRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDetailsServiceImpl implements UserDetailsService {
    private UserEntityRepository userRepository;

    UserDetailsServiceImpl(UserEntityRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Extrae el usuario de la BD
        return this.userRepository.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException(username+" no encontrado")
        );

    }
}
