package GamingLads.userservice.serviceTest;

import GamingLads.userservice.http.HttpWrapper;
import GamingLads.userservice.model.User;
import GamingLads.userservice.repository.UserRepository;
import GamingLads.userservice.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthenticationServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RestTemplate restTemplate;

    private HttpWrapper httpWrapper;
    private AuthenticationService authService;
    private User user;

    @BeforeEach
    public void setup() {
        httpWrapper = new HttpWrapper(restTemplate);
        authService = new AuthenticationService(userRepository, httpWrapper);
        user = new User("1", "Sharony", "1234");
    }

    @Test
    void testSignUp(){
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
        assertTrue(authService.signUp(user));
    }

    @Test
    void testSaveUser() {
        assertTrue(authService.saveUser(user));
    }
}
