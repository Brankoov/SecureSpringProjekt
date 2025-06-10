package se.branko.secureSpringProject.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import se.branko.secureSpringProject.dto.UserRegistrationDto;
import se.branko.secureSpringProject.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService service) {
        this.userService = service;
    }

    @GetMapping("/register")
    public String showForm(Model model) {
        System.out.println(">>> Öppnar register-sidan");
        model.addAttribute("userDto", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDto") UserRegistrationDto dto,
                               BindingResult result) {
        if (userService.usernameExists(dto.getUsername())) {
            result.rejectValue("username", null, "Användarnamnet är redan taget");
        }

        if (result.hasErrors()) {
            return "register";
        }

        userService.register(dto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

