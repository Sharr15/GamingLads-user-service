package GamingLads.userservice.controllerTests;

import GamingLads.userservice.model.Role;
import GamingLads.userservice.model.User;
import GamingLads.userservice.repository.UserRepository;
import GamingLads.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
public class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    private UserService authService;
    private User user;

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
        authService = new UserService(userRepository, restTemplate);
        Role role = new Role(1, "USER");
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user = new User(1,"Sharony","1234", roles ,false);
    }

    @Test
    public void testSignUp() throws Exception {
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        RequestBuilder requestCall = MockMvcRequestBuilders.post("/auth/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user));
        mockMvc.perform(requestCall).andExpect(status().isOk());
    }

}
