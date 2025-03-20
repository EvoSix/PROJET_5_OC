package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SignupRequestTest {

    @Test
    void test(){
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.equals(new SignupRequest());
        signupRequest.hashCode();
        signupRequest.toString();
        assertNotNull(signupRequest.toString());
    }
}