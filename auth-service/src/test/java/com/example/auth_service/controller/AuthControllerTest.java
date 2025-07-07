package com.example.auth_service.controller;
import com.example.auth_service.service.AuthService;
import com.example.auth_service.config.JwtTokenProvider;
import com.example.auth_service.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
  controllers = AuthController.class,
  excludeAutoConfiguration = {
    SecurityAutoConfiguration.class,
    SecurityFilterAutoConfiguration.class
  }
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private AuthService authService;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @Test
  void loginEndpoint_ShouldReturnTokenAndExpiry() throws Exception {
    when(authService.login("admin","secret")).thenReturn("jwt-xyz");

    mockMvc.perform(post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(new LoginRequest("admin","secret"))))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.token").value("jwt-xyz"))
      .andExpect(jsonPath("$.expiresIn").value(3600000));
  }


}
