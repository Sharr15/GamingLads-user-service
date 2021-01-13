package gaminglads.userservice.serviceTest;

import gaminglads.userservice.exceptions.InvalidCredentialsException;
import gaminglads.userservice.exceptions.TokenNotCreatedException;
import gaminglads.userservice.model.Role;
import gaminglads.userservice.model.SignInRequest;
import gaminglads.userservice.model.SignUpRequest;
import gaminglads.userservice.model.User;
import gaminglads.userservice.repository.RoleRepository;
import gaminglads.userservice.repository.UserRepository;
import gaminglads.userservice.service.JwtService;
import gaminglads.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private UserService userService;
    private JwtService jwtService;
    private User user;
    private User user1;
    private SignInRequest signInRequest;
    private SignInRequest signInRequest1;
    private SignUpRequest signUpRequest;
    private Role role;

    @BeforeEach
    public void setup() {
        jwtService = Mockito.mock(JwtService.class);
        userService = new UserService(userRepository, restTemplate, jwtService, roleRepository);
        RestGatewaySupport gateway = new RestGatewaySupport();
        gateway.setRestTemplate(restTemplate);
        mockServer = MockRestServiceServer.createServer(gateway);
        role = new Role(1, "USER");
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user = new User(1, "Sharony", "1234", roles, false);
        user1 = new User(2, "Duncan", "5678", roles, true);
        signInRequest = new SignInRequest("Duncan", "5678");
        signInRequest1 = new SignInRequest("Duncan", "56789");
        signUpRequest = new SignUpRequest("Sharony", "1234", "1234");
    }

    @Test
        //succeeds if no exception is thrown
    void testSignUp() throws Exception {
        when(restTemplate.postForEntity(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())).thenReturn(new ResponseEntity<>(CREATED));

        when(userRepository.findByUsername("Sharony")).thenReturn(user);
        when(userService.createProfile(user)).thenReturn(new ResponseEntity<>(CREATED));
    }

    @Test
    void testSignIn() throws Exception {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user1);

        when(userRepository.findByUsername(signInRequest.getUsername())).thenReturn(user);
        when(userRepository.findAll()).thenReturn(userList);
        when(jwtService.generateToken(user)).thenReturn("test");
        when(userService.createToken(signInRequest)).thenReturn("test");

        assertEquals(userService.signIn(signInRequest), "test");
    }

    @Test
    void testInvalidCredentialsSignIn() throws Exception {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user1);

        when(userRepository.findAll()).thenReturn(userList);

        assertThrows(InvalidCredentialsException.class, () -> userService.signIn(signInRequest1));
    }

    @Test
    void testTokenNotCreatedSignIn() throws Exception {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user1);

        when(userRepository.findByUsername(signInRequest.getUsername())).thenReturn(user);
        when(userRepository.findAll()).thenReturn(userList);
        when(jwtService.generateToken(user)).thenReturn(null);

        assertThrows(TokenNotCreatedException.class, () -> userService.signIn(signInRequest));
    }
}
