package gaminglads.userservice.integrationTests;

import gaminglads.userservice.model.SignInRequest;
import gaminglads.userservice.model.SignUpRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserIntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    private static String randomUsername;

    public static String randomString(int length) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    @BeforeAll
    public static void setUp() {
        randomUsername = randomString(10);
    }

    @Test
    @Order(1)
    void testSignUp() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(randomUsername, "1234", "1234");
        ResponseEntity<String> entity = restTemplate.postForEntity("https://gaminglads-gateway.herokuapp.com/user/signUp", signUpRequest, String.class);
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(2)
    void testSignIn() throws Exception {
        SignInRequest signInRequest = new SignInRequest(randomUsername, "1234");
        ResponseEntity<String> entity = restTemplate.postForEntity("https://gaminglads-gateway.herokuapp.com/user/signIn/user", signInRequest, String.class);
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }
}
