package com.openclassrooms.starterjwt.services;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)

@SpringBootTest
@ActiveProfiles("test")
public class SessionServiceIntegration {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    private Session session;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();

        session = new Session();
        session.setName("Test Session");
        session.setDate(new Date());
        session.setDescription("Description de la session test");

        session = sessionRepository.save(session);
    }

    @Test
    @DisplayName("Create Session - Success")
    void create_ShouldReturnSavedSession() {
        // Arrange
        Session newSession = new Session();
        newSession.setName("New Session");
        newSession.setDate(new Date());
        newSession.setDescription("New session description");

        // Act
        Session savedSession = sessionService.create(newSession);

        // Assert
        assertNotNull(savedSession);
        assertNotNull(savedSession.getId());
        assertEquals("New Session", savedSession.getName());
    }
    @Test
    @DisplayName("Find all sessions - Success")
    void findAll_ShouldReturnListOfSessions() {
        // Act
        List<Session> sessions = sessionService.findAll();

        // Assert
        assertEquals(1, sessions.size());
        assertEquals("Test Session", sessions.get(0).getName());
    }

    @Test
    @DisplayName("Find session by ID - Success")
    void getById_ShouldReturnSession_WhenExists() {
        // Act
        Session foundSession = sessionService.getById(session.getId());

        // Assert
        assertNotNull(foundSession);
        assertEquals(session.getId(), foundSession.getId());
    }

    @Test
    @DisplayName("Find session by ID - Not Found")
    void getById_ShouldReturnNull_WhenNotExists() {
        // Act
        Session foundSession = sessionService.getById(999L);

        // Assert
        assertNull(foundSession);
    }

    @Test
    @DisplayName("Update Session - Success")
    void update_ShouldReturnUpdatedSession() {
        // Arrange
        session.setName("Updated Session");

        // Act
        Session updatedSession = sessionService.update(session.getId(), session);

        // Assert
        assertNotNull(updatedSession);
        assertEquals("Updated Session", updatedSession.getName());
    }

    @Test
    @DisplayName("Delete Session - Success")
    void delete_ShouldRemoveSession() {
        // Act
        sessionService.delete(session.getId());

        // Assert
        Optional<Session> deletedSession = sessionRepository.findById(session.getId());
        assertFalse(deletedSession.isPresent());
    }

    @Test
    @DisplayName("User participate in session - Success")
    void participate_ShouldAddUserToSession() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user = userRepository.save(user);

        // Act
        sessionService.participate(session.getId(), user.getId());

        // Assert
        Session updatedSession = sessionRepository.findById(session.getId()).orElse(null);
        assertNotNull(updatedSession);
        assertEquals(1, updatedSession.getUsers().size());
        assertEquals(user.getId(), updatedSession.getUsers().get(0).getId());
    }

    @Test
    @DisplayName("User participate in session - Already Participating")
    void participate_ShouldThrowException_WhenUserAlreadyParticipating() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user = userRepository.save(user);
        sessionService.participate(session.getId(), user.getId());


        User finalUser = user;
        assertThrows(BadRequestException.class, () -> sessionService.participate(session.getId(), finalUser.getId()));
    }

    @Test
    @DisplayName("User no longer participate in session - Success")
    void noLongerParticipate_ShouldRemoveUserFromSession() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user = userRepository.save(user);
        sessionService.participate(session.getId(), user.getId());

        // Act
        sessionService.noLongerParticipate(session.getId(), user.getId());

        // Assert
        Session updatedSession = sessionRepository.findById(session.getId()).orElse(null);
        assertNotNull(updatedSession);
        assertTrue(updatedSession.getUsers().isEmpty());
    }

    @Test
    @DisplayName("User no longer participate in session - Not Participating")
    void noLongerParticipate_ShouldThrowException_WhenUserNotParticipating() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");


        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(session.getId(), 1L));
    }
}
