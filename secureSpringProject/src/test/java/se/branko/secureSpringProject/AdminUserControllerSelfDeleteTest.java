package se.branko.secureSpringProject;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import se.branko.secureSpringProject.entity.AppUser;
import se.branko.secureSpringProject.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AdminUserControllerSelfDeleteTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long adminUserId;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        AppUser admin = new AppUser();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("AdminPass12!!"));
        admin.setRole("ROLE_ADMIN");
        admin.setConsentGiven(true);
        userRepository.save(admin);
        adminUserId = admin.getId();

        AppUser otherUser = new AppUser();
        otherUser.setUsername("otherUser");
        otherUser.setPassword(passwordEncoder.encode("UserPass12!!"));
        otherUser.setRole("ROLE_USER");
        otherUser.setConsentGiven(true);
        userRepository.save(otherUser);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteUser_whenDeletingSelf_shouldShowErrorAndSameView() throws Exception {
        mockMvc.perform(post("/admin/users/delete/" + adminUserId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-users"))
                .andExpect(model().attributeExists("selfDeleteError"))
                .andExpect(model().attribute("selfDeleteError", is(true)))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("currentUsername"));
    }
}
