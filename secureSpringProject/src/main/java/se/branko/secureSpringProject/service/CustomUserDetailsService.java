package se.branko.secureSpringProject.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import se.branko.secureSpringProject.entity.AppUser;
import se.branko.secureSpringProject.repository.UserRepository;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository repo) {
        this.userRepository = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Försöker ladda användare: " + username);
        try {
            AppUser user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Användare ej hittad: " + username));
            logger.info("Användare hittad: " + username);
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole())))
                    .build();
        } catch (UsernameNotFoundException e) {
            logger.warn("Inloggningsförsök med okänt användarnamn: " + username);
            throw e;
        }
    }
}
