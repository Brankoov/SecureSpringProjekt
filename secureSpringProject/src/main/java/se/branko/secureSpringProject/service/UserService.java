package se.branko.secureSpringProject.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.branko.secureSpringProject.dto.UserRegistrationDto;
import se.branko.secureSpringProject.entity.AppUser;
import se.branko.secureSpringProject.exception.UserNotFoundException;
import se.branko.secureSpringProject.logging.LoggingComponent;
import se.branko.secureSpringProject.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoggingComponent logger;

    public UserService(UserRepository repo, PasswordEncoder encoder, LoggingComponent logger) {
        this.userRepository = repo;
        this.passwordEncoder = encoder;
        this.logger = logger;
    }

    public void register(UserRegistrationDto dto) {
        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Säkerställ att rollen börjar med "ROLE_"
        String role = dto.getRole();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role.toUpperCase(); // exempel: "user" → "ROLE_USER"
        }
        user.setRole(role);

        user.setConsentGiven(dto.isConsentGiven());

        userRepository.save(user);
        logger.log("Registrerat användare: " + dto.getUsername());
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Användare med id " + id + " finns inte");
        }
        userRepository.deleteById(id);
        logger.log("Tog bort användare med id: " + id);
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }
}
