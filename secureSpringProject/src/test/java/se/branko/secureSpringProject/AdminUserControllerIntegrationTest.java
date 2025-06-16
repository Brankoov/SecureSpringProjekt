package se.branko.secureSpringProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import se.branko.secureSpringProject.entity.AppUser;
import se.branko.secureSpringProject.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//BORTAGNING TEST
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AdminUserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long testUserId;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        AppUser user = new AppUser();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole("ROLE_USER");
        user.setConsentGiven(true);
        userRepository.save(user);
        testUserId = user.getId();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUserRemovesUserFromDatabase() throws Exception {
        mockMvc.perform(post("/admin/users/delete/" + testUserId).with(csrf()))
                .andExpect(status().is3xxRedirection());
        assert (userRepository.findById(testUserId).isEmpty());
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllUsers_WithAdminRole_ReturnsViewAndModel() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("currentUsername"));
    }
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAllUsers_WithUserRole_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithAnonymousUser
    void testGetAllUsers_AnonymousUser_IsRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }


}
