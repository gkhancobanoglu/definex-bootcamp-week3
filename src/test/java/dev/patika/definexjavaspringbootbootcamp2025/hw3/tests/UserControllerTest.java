package dev.patika.definexjavaspringbootbootcamp2025.hw3.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.controller.UserController;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.dto.User;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUsers_ShouldReturnOk() throws Exception {

        User user = User.builder()
                .id(UUID.randomUUID())
                .name("Gökhan Çobanoğlu")
                .email("gokhancobanoglu@example.com")
                .licenseNumber("ABC1234")
                .build();

        List<User> userList = Collections.singletonList(user);

        when(userService.list()).thenReturn(userList);

        mockMvc.perform(get("/api/user/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gökhan Çobanoğlu"))
                .andExpect(jsonPath("$[0].email").value("gokhancobanoglu@example.com"))
                .andExpect(jsonPath("$[0].licenseNumber").value("ABC1234"));

        verify(userService, times(1)).list();
    }


    @Test
    void registerUser_ShouldReturnCreated() throws Exception {

        User user = User.builder()
                .id(UUID.randomUUID())
                .name("Gökhan Çobanoğlu")
                .email("gokhancobanoglu@example.com")
                .licenseNumber("ABC1234")
                .build();

        when(userService.create(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Gökhan Çobanoğlu\", \"email\": \"gokhancobanoglu@example.com\", \"licenseNumber\": \"ABC1234\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Gökhan Çobanoğlu"))
                .andExpect(jsonPath("$.email").value("gokhancobanoglu@example.com"))
                .andExpect(jsonPath("$.licenseNumber").value("ABC1234"));

        verify(userService, times(1)).create(any(User.class));
    }


    @Test
    void getUserProfile_ShouldReturnOk() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .name("Gökhan Çobanoğlu")
                .email("gokhancobanoglu@example.com")
                .licenseNumber("ABC1234")
                .build();

        when(userService.getUserProfile(userId)).thenReturn(user);

        mockMvc.perform(get("/api/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gökhan Çobanoğlu"))
                .andExpect(jsonPath("$.email").value("gokhancobanoglu@example.com"))
                .andExpect(jsonPath("$.licenseNumber").value("ABC1234"));

        verify(userService, times(1)).getUserProfile(userId);
    }


    @Test
    void getUserProfile_ShouldReturnBadRequest() throws Exception {

        String invalidUserId = "invalid-uuid";

        mockMvc.perform(get("/api/user/{id}", invalidUserId))
                .andExpect(status().isBadRequest());

        verify(userService, times(0)).getUserProfile(any(UUID.class));
    }


    @Test
    void updateUser_ShouldReturnOk() throws Exception {

        UUID userId = UUID.randomUUID();
        User updatedUser = User.builder()
                .id(userId)
                .name("Gökhan Çobanoğlu")
                .email("gokhancobanoglu@example.com")
                .licenseNumber("ABC1234")
                .build();

        when(userService.update(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Gökhan Çobanoğlu\", \"email\": \"gokhancobanoglu@example.com\", \"licenseNumber\": \"ABC1234\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gökhan Çobanoğlu"))
                .andExpect(jsonPath("$.email").value("gokhancobanoglu@example.com"))
                .andExpect(jsonPath("$.licenseNumber").value("ABC1234"));

        verify(userService, times(1)).update(any(User.class));
    }

    @Test
    void updateUser_ShouldReturnBadRequest() throws Exception {

        UUID userId = UUID.randomUUID();

        mockMvc.perform(put("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\", \"email\": \"gokhancobanoglu@example.com\", \"licenseNumber\": \"ABC1234\"}"))
                .andExpect(status().isBadRequest());

        verify(userService, times(0)).update(any(User.class));
    }

}
