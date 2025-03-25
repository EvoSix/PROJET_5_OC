package com.openclassrooms.starterjwt.services;

import static org.mockito.Mockito.*;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        session = new Session();
        session.setId(1L);
        session.setName("Test Session");
        session.setDescription("Description");
        session.setUsers(new ArrayList<>());

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
    }


    @Test
    @DisplayName("Create Session - Success")
    void create_ShouldReturnSavedSession() {

        when(sessionRepository.save(any(Session.class))).thenReturn(session);


        Session savedSession = sessionService.create(session);


        assertNotNull(savedSession);
        assertEquals(session.getName(), savedSession.getName());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    @DisplayName("Find all sessions - Success")
    void findAll_ShouldReturnListOfSessions() {
        // Arrange
        List<Session> sessions = List.of(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.findAll();


        assertEquals(1, result.size());
        verify(sessionRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Find session by ID - Success")
    void getById_ShouldReturnSession_WhenExists() {

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));


        Session result = sessionService.getById(1L);


        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find session by ID - Not Found")
    void getById_ShouldReturnNull_WhenNotExists() {

        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());


        Session result = sessionService.getById(1L);


        assertNull(result);
    }
    @Test
    @DisplayName("Update Session - Success")
    void update_ShouldReturnUpdatedSession() {
        // Arrange

        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        session.setName("Updated Session");


        Session updatedSession = sessionService.update(1L, session);


        assertNotNull(updatedSession);
        assertEquals("Updated Session", updatedSession.getName());
        verify(sessionRepository, times(1)).save(session);
    }
    @Test
    @DisplayName("User participate in session - Success")
    void participate_ShouldAddUserToSession() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);


        sessionService.participate(1L, 1L);


        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    @DisplayName("User participate in session - Already Participating")
    void participate_ShouldThrowException_WhenUserAlreadyParticipating() {
        // Arrange
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));


        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
    }
    @Test
    @DisplayName("User no longer participate in session - Success")
    void noLongerParticipate_ShouldRemoveUserFromSession() {
        // Arrange
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.noLongerParticipate(1L, 1L);


        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    @DisplayName("User no longer participate in session - Not Participating")
    void noLongerParticipate_ShouldThrowException_WhenUserNotParticipating() {

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));



        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    @Test
    public void whenDeleteWithId1_repositoryShouldDeletById1() {

        Long id = 1L;

        sessionService.delete(id);

        verify(sessionRepository).deleteById(id);
    }
}
