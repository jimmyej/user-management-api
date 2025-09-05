package com.jse.manage_user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jse.manage_user.model.entity.PhoneEntity;
import com.jse.manage_user.model.entity.UserEntity;
import com.jse.manage_user.model.properties.ValidationProperties;
import com.jse.manage_user.model.request.PhoneRequest;
import com.jse.manage_user.model.request.UserRequest;
import com.jse.manage_user.repository.UserRepository;
import com.jse.manage_user.service.impl.UserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({UserServiceImpl.class, ValidationProperties.class})
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    UserRepository userRepository;


    String token1 = "6ad27e7a-c371-41c2-be67-d2238aef56ec";
    String token2 = "5f5a6257-ed4a-406e-943c-ea43957525aa";

    PhoneRequest phoneRequest1 = PhoneRequest.builder()
            .number("999999999")
            .cityCode("1")
            .countryCode("51")
            .build();

    UserRequest userRequest = UserRequest.builder()
            .name("Jimmy Sanchez")
            .email("jimmy.sanchez@gmail.com")
            .password("JS53$abc!KJ")
            .phones(Set.of(phoneRequest1))
            .build();

    UserRequest badUserRequest = UserRequest.builder()
            .name("Jimmy Sanchez")
            .email("!234@ee@gmail.com")
            .password("123")
            .phones(Set.of(phoneRequest1))
            .build();

    PhoneEntity phoneEntity1 = PhoneEntity.builder().id(UUID.randomUUID()).number("999999999").cityCode("1").countryCode("51").build();
    PhoneEntity phoneEntity2 = PhoneEntity.builder().id(UUID.randomUUID()).number("988888888").cityCode("1").countryCode("51").build();
    PhoneEntity phoneEntity3 = PhoneEntity.builder().id(UUID.randomUUID()).number("977777777").cityCode("1").countryCode("51").build();

    UserEntity userEntity1 = UserEntity.builder()
            .id(UUID.randomUUID())
            .name("Jimmy Sanchez")
            .email("jimmy.sanchez@gmail.com")
            .password("JS53$abc!KJ")
            .token(UUID.fromString(token1).toString())
            .created(LocalDateTime.now())
            .modified(LocalDateTime.now())
            .lastLogin(LocalDateTime.now())
            .phones(Set.of(phoneEntity1, phoneEntity2))
            .activated(true)
            .build();

    UserEntity userEntity2 = UserEntity.builder()
            .id(UUID.randomUUID())
            .name("Salvador Sanchez")
            .email("salvador.sanchez@gmail.com")
            .password("SV53$abc!KJ")
            .token(UUID.fromString(token2).toString())
            .created(LocalDateTime.now())
            .modified(LocalDateTime.now())
            .lastLogin(LocalDateTime.now())
            .phones(Set.of(phoneEntity3))
            .activated(true)
            .build();

    private UserEntity prepareUserRequest(){
        UserEntity userEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .name("Jimmy Sanchez")
                .email("lucjimmyiana.sanchez@gmail.com")
                .password("JS53$abc!KJ")
                .token(UUID.fromString(token1).toString())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .activated(true)
                .build();
        PhoneEntity phoneEntity = PhoneEntity.builder().id(UUID.randomUUID()).number("999999999").cityCode("1").countryCode("51").user(userEntity).build();
        userEntity.setPhones(Set.of(phoneEntity));
        return userEntity;
    }

    @Test
    void registerUser_ok() throws Exception {
        Mockito.when(userRepository.existsByEmail("jimmy.sanchez@gmail.com")).thenReturn(false);
        Mockito.when(userRepository.save(any())).thenReturn(prepareUserRequest());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.token", Matchers.is(token1)));
    }

    @Test
    void registerUser_conflict() throws Exception {
        Mockito.when(userRepository.existsByEmail("jimmy.sanchez@gmail.com")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is("Email already exists!")));
    }

    @Test
    void registerUser_badFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(badUserRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.message.email", Matchers.is("Invalid email format")))
                .andExpect(jsonPath("$.message.password", Matchers.startsWith("Password must be at least 8 chars")));
    }

    @Test
    void registerUser_badRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is("Request body is empty!")));
    }
}
