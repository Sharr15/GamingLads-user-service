package gaminglads.userservice.controllerTests;

import gaminglads.userservice.model.Role;
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
import org.mockito.Mock;
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
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    private JwtService jwtService;
    private UserService userService;
    private User user;
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
        userService = new UserService(userRepository, restTemplate, jwtService, roleRepository);
        role = new Role(1, "USER");
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user = new User(1,"Sharony","1234", roles ,false);
    }

    @Test
    void testSignUp() throws Exception {
        when(restTemplate.postForEntity(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<User>>any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        //HttpHeaders header = new HttpHeaders();
        //header.setContentType(MediaType.APPLICATION_JSON);
        //ResponseEntity<String> entity = new ResponseEntity<>(HttpStatus.CREATED);
        //when(userService.createProfile(user)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        when(roleRepository.findByName("USER")).thenReturn(role);
        when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        when(userRepository.findByUsername("Sharony")).thenReturn(user);
        when(userService.createProfile(user)).thenReturn(new ResponseEntity<User>(user, CREATED));
        RequestBuilder requestCall = MockMvcRequestBuilders.post("/user/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user));
        mockMvc.perform(requestCall).andExpect(status().isOk());

        //MvcResult result = (MvcResult) mockMvc.perform(post("user/signUp")
              //  .contentType(MediaType.APPLICATION_JSON)
               // .content(asJsonString(user)));

      //  mockMvc.perform(asyncDispatch(result))
               // .andExpect(status().isOk());

        //ResponseEntity<?> responseEntity = userService.createProfile(user);
        //assertThat(responseEntity.getStatusCode().equals(CREATED));

    }
}