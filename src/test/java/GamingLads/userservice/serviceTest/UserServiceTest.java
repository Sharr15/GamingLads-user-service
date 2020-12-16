package GamingLads.userservice.serviceTest;

import GamingLads.userservice.model.Role;
import GamingLads.userservice.model.User;
import GamingLads.userservice.repository.RoleRepository;
import GamingLads.userservice.repository.UserRepository;
import GamingLads.userservice.service.JwtService;
import GamingLads.userservice.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {

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

    @BeforeEach
    public void setup() {
        userService = new UserService(userRepository, restTemplate, jwtService, roleRepository);
        RestGatewaySupport gateway = new RestGatewaySupport();
        gateway.setRestTemplate(restTemplate);
        mockServer = MockRestServiceServer.createServer(gateway);
        Role role = new Role(1, "USER");
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user = new User(1, "Sharony", "1234", roles, false);
    }

    @Test
    void testSignUp(){
       // when(restTemplate.postForEntity(
              // ArgumentMatchers.anyString(),
               // ArgumentMatchers.any(HttpMethod.class),
                //ArgumentMatchers.any(),
               // ArgumentMatchers.<Class<String>>any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
        //assertTrue(userService.signUp(user));

        when(restTemplate.postForEntity(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<User>>any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        when(userRepository.findByUsername("Sharony")).thenReturn(user);
        when(userService.createProfile(user)).thenReturn(new ResponseEntity<User>(user, CREATED));

        //doReturn(new ResponseEntity<>(HttpStatus.CREATED)).when(restTemplate).postForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any());
        //HttpHeaders header = new HttpHeaders();
       // header.setContentType(MediaType.APPLICATION_JSON);

        //ResponseEntity<String> entity = new ResponseEntity<>(HttpStatus.CREATED);

        //when(userService.createProfile(any(User.class)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
        assertTrue(userService.signUp(user));
    }

    @Test
    void testSaveUser() {
        assertTrue(userService.saveUser(user));
    }
}
