package com.openclassrooms.starterjwt.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    private Authentication authMock;

    @BeforeEach
    public void setupProps() {

        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "jwtKey");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 5000);
    }

    @Test
    public void generateJwtToken_shouldGenerateToken() {

        UserDetails useDetails = new UserDetailsImpl(1L, "bob@test.com", "Bob", "Le Bricoleur", true, "pass4321");
        authMock = mock(Authentication.class);
        when(authMock.getPrincipal()).thenReturn(useDetails);

        String token = jwtUtils.generateJwtToken(authMock);

        System.out.println(token);
        assertThat(token).isNotNull();
    }

    @Test
    public void givenGoodToken_getUserNameFromJwtToken_shouldGetUserNameAsEmail() {

        String userEmail = "bob@test.com";
        String token = SigningHelper.sign(userEmail, 5000, "jwtKey");

        String email = jwtUtils.getUserNameFromJwtToken(token);

        assertThat(email).isEqualTo(userEmail);
    }

    @Test
    public void givenGoodToken_validateJwtToken_shouldReturnTrue() {

        String token = SigningHelper.sign("bob@test.com", 5000, "jwtKey");

        boolean valid = jwtUtils.validateJwtToken(token);

        assertTrue(valid);
    }

    @Test
    @ExtendWith(OutputCaptureExtension.class)
    public void givenExpiredToken_validateJwtToken_ShouldReturnFalseAndLogIt(CapturedOutput capture) {

        String token = SigningHelper.sign("bob@test.com", -1, "jwtKey");

        boolean valid = jwtUtils.validateJwtToken(token);

        assertFalse(valid);
        assertThat(capture.getOut()).contains("JWT token is expired: ");
    }

    @Test
    @ExtendWith(OutputCaptureExtension.class)
    public void givenInvalidToken_validateJwtToken_ShouldReturnFalseAndLogIt(CapturedOutput capture) {

        String token = "fyJhbGciOiJIUzUxMiJ9.eyJz9ZSmR01GH_3nMUM2hqloKKLOJAFF0LoI6a-cITi88jjtF3fOBjVDrZWjVyqzYG9jL-7YVOsSFrAKWAvnw";


        boolean valid = jwtUtils.validateJwtToken(token);

        assertFalse(valid);
        assertThat(capture.getOut()).contains("Invalid JWT token: ");
    }

    @Test
    @ExtendWith(OutputCaptureExtension.class)
    public void givenBadSignedToken_validateJwtToken_ShouldReturnFalseAndLogIt(CapturedOutput capture) {

        String token = SigningHelper.sign("bob@test.com", 5000, "badJwtKey");

        boolean valid = jwtUtils.validateJwtToken(token);

        assertFalse(valid);
        assertThat(capture.getOut()).contains("Invalid JWT signature: ");
    }

    @Test
    @ExtendWith(OutputCaptureExtension.class)
    public void givenUnsupportedToken_validateJwtToken_ShouldReturnFalseAndLogIt(CapturedOutput capture) {

        String token = SigningHelper.unsigned("bob@test.com", 5000, "jwtKey");

        boolean valid = jwtUtils.validateJwtToken(token);

        assertFalse(valid);
        assertThat(capture.getOut()).contains("JWT token is unsupported: ");
    }

    @Test
    @ExtendWith(OutputCaptureExtension.class)
    public void givenEmptyClaimToken_validateJwtToken_ShouldReturnFalseAndLogIt(CapturedOutput capture)
            throws Exception {

        String token = "";

        boolean valid = jwtUtils.validateJwtToken(token);

        assertFalse(valid);
        assertThat(capture.getOut()).contains("JWT claims string is empty: ");
    }

}