package com.openclassrooms.starterjwt.controllers;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;

import com.openclassrooms.starterjwt.mapper.SessionMapper;
import org.springframework.http.HttpHeaders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;




@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {
    private MockMvc mockMvc;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;



    @InjectMocks
    private SessionController sessionController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private String jwtToken;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();



    }
//GET /api/session/{id}
    @Test
    @DisplayName("Get Session by ID, Should return session when exist")
    void findById_ShouldReturnSession_WhenSessionExists() throws Exception {
        // Arrange
        Session session = new Session(1L, "Math Class", null, "Description", null, Collections.emptyList(), null, null);
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Math Class");

        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act & Assert
        mockMvc.perform(get("/api/session/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Math Class"));
    }

    @Test
    @DisplayName("Get Session by ID, Should return Error when not exist")
    void findById_ShouldReturnNotFound_WhenSessionDoesNotExist() throws Exception {
        when(sessionService.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/session/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }
//GET /api/session

    @Test
    @DisplayName("Get ALL sessions, Should return List Sessions")
    void findAll_ShouldReturnListOfSessions() throws Exception {
        // Arrange
        Session session1 = new Session();
        Session session2 = new Session();
        List<Session> sessions = Arrays.asList(session1, session2);

        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Math Class");

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Science Class");

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(Arrays.asList(sessionDto1, sessionDto2));

        // Act & Assert
        mockMvc.perform(get("/api/session")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }


//POST /api/session

    @Test
    @DisplayName("Post a Session, should return Session DTO in success ")
    void create_ShouldReturnSessionDto_WhenSuccessful() throws Exception {
        // Arrange
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("New Session");

        sessionDto.setDate(new Date()); // Ajout de la date obligatoire
        sessionDto.setDescription("A detailed session description.");
        sessionDto.setTeacher_id(1L);
        Session session = new Session();

        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.create(any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        ResultActions result = mockMvc.perform(post("/api/session")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)));

        System.out.println("Réponse JSON : " + result.andReturn().getResponse().getContentAsString());
        // Act & Assert
        mockMvc.perform(post("/api/session")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Session"));}

    //DELETE /api/session/{id}
    @Test
    @DisplayName("Delete a session, should return ok when session exist")
    void delete_ShouldReturnOk_WhenSessionExists() throws Exception {
        Session session = new Session(1L, "Math Class", null, "Description", null, Collections.emptyList(), null, null);
        when(sessionService.getById(1L)).thenReturn(session);

        mockMvc.perform(delete("/api/session/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete a session, should return error when session doesn't exist")
    void delete_ShouldReturnNotFound_WhenSessionDoesNotExist() throws Exception {
        when(sessionService.getById(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/session/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }
//POST /api/session/{id}/participate/{userId}
@Test
@DisplayName("Post Participant by Session ID, should return ok when success")
void participate_ShouldReturnOk_WhenSuccessful() throws Exception {
    mockMvc.perform(post("/api/session/1/participate/2")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
            .andExpect(status().isOk());
}

}
