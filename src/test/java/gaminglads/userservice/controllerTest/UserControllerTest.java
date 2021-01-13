package gaminglads.userservice.controllerTest;

import gaminglads.userservice.model.Role;
import gaminglads.userservice.model.SignInRequest;
import gaminglads.userservice.model.SignUpRequest;
import gaminglads.userservice.model.User;
import gaminglads.userservice.repository.RoleRepository;
import gaminglads.userservice.repository.UserRepository;
import gaminglads.userservice.service.JwtService;
import gaminglads.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    private JwtService jwtService;
    private UserService userService;
    private User user;
    private User user1;
    private SignInRequest signInRequest;
    private SignInRequest signInRequest1;
    private SignUpRequest signUpRequest;
    private Role role;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() {
        jwtService = Mockito.mock(JwtService.class);
        userService = new UserService(userRepository, restTemplate, jwtService, roleRepository);
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
    void testSignUp() throws Exception {
        when(restTemplate.postForEntity(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<User>>any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        when(roleRepository.findByName("USER")).thenReturn(role);
        when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        when(userRepository.findByUsername("Sharony")).thenReturn(user);
        RequestBuilder requestCall = MockMvcRequestBuilders.post("/user/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user));
        mockMvc.perform(requestCall).andExpect(status().isOk());
    }

    @Test
    void signIn() throws Exception {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user1);

        when(userRepository.findAll()).thenReturn(userList);
        when(userRepository.findByUsername(signInRequest.getUsername())).thenReturn(user1);
        when(jwtService.generateToken(user1)).thenReturn("test");
        when(userService.createToken(signInRequest)).thenReturn("test");

        RequestBuilder requestCall = MockMvcRequestBuilders.post("/user/signIn/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signInRequest));
        mockMvc.perform(requestCall).andExpect(status().isOk());
    }
}
