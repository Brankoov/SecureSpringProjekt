package se.branko.secureSpringProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.branko.secureSpringProject.entity.AppUser;
import se.branko.secureSpringProject.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    // Visa alla användare
    @GetMapping
    public String getAllUsers(Model model) {
        List<AppUser> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin-users";  // Thymeleaf-sidan, t.ex. admin-admin-users.html
    }

    // Ta bort användare
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users"; // Efter borttagning, återgå till listan
    }
}
