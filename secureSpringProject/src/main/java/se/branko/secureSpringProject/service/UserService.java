package se.branko.secureSpringProject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.branko.secureSpringProject.dto.UserRegistrationDto;
import se.branko.secureSpringProject.entity.AppUser;
import se.branko.secureSpringProject.exception.UserNotFoundException;
import se.branko.secureSpringProject.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.userRepository = repo;
        this.passwordEncoder = encoder;
    }

    public void register(UserRegistrationDto dto) {
        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        String role = dto.getRole();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role.toUpperCase();
        }
        user.setRole(role);
        user.setConsentGiven(dto.isConsentGiven());

        userRepository.save(user);
        logger.info("Registrerat användare: {}", dto.getUsername());
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            logger.warn("Försökte ta bort icke-existerande användare med id: {}", id);
            throw new UserNotFoundException("Användare med id " + id + " finns inte");
        }
        userRepository.deleteById(id);
        logger.info("Tog bort användare med id: {}", id);
    }
    public AppUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Användare med id " + id + " finns inte"));
    }

    public List<AppUser> getAllUsers() {
        logger.debug("Hämtar alla användare från databasen");
        return userRepository.findAll();
    }
}
