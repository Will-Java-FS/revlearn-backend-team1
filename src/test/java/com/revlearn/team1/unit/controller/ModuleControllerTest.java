package com.revlearn.team1.unit.controller;

import com.revlearn.team1.controller.ModuleController;
import com.revlearn.team1.dto.module.ModuleResDTO;
import com.revlearn.team1.dto.module.ModuleReqDTO;
import com.revlearn.team1.service.module.ModuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(ModuleController.class)
@WebMvcTest(ModuleController.class)
public class ModuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ModuleService moduleService;

    private ModuleResDTO moduleResDTO;

    @BeforeEach
    void setUp() {
        moduleResDTO = new ModuleResDTO(1L, "Module 1", "Description", 1L, 1L);
    }

    @Test
    void getModuleById_ShouldReturnModuleDTO() throws Exception {
        given(moduleService.getModuleById(1L)).willReturn(moduleResDTO);

        mockMvc.perform(get("/api/v1/module/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Module 1")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.orderIndex", is(1)))
                .andExpect(jsonPath("$.courseId", is(1)));
    }

    @Test
    void createModule_ShouldReturnCreatedModuleDTO() throws Exception {
        given(moduleService.createModule(any(Long.class), any(ModuleReqDTO.class))).willReturn(moduleResDTO);

        mockMvc.perform(post("/api/v1/module/course/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Module 1\", \"description\": \"Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Module 1")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.orderIndex", is(1)))
                .andExpect(jsonPath("$.courseId", is(1)));
    }

    @Test
    void updateModule_ShouldReturnUpdatedModuleDTO() throws Exception {
        given(moduleService.updateModule(eq(1L), any(ModuleReqDTO.class))).willReturn(moduleResDTO);

        mockMvc.perform(put("/api/v1/module/1/course/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Module 1\", \"description\": \"Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Module 1")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.orderIndex", is(1)))
                .andExpect(jsonPath("$.courseId", is(1)));
    }

    @Test
    void deleteModule_ShouldReturnSuccessMessage() throws Exception {
        when(moduleService.deleteModule(1L)).thenReturn("Module deleted successfully");

        mockMvc.perform(delete("/api/v1/module/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Module deleted successfully"));
    }


    @Configuration
    @EnableWebSecurity
    static class TestSecurityConfig {
        @Bean
        protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .authorizeHttpRequests(requests -> requests
                            .anyRequest().permitAll());
            return http.build();
        }

        @Bean
        public UrlBasedCorsConfigurationSource corsConfigurationSource() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", corsConfiguration());
            return source;
        }

        @Bean
        public CorsConfiguration corsConfiguration() {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173")); // TODO add EC2 url to this list
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            corsConfiguration.setAllowedHeaders(List.of("*"));
            corsConfiguration.setAllowCredentials(true);
            return corsConfiguration;
        }
    }
}
