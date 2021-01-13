package gaminglads.userservice.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import gaminglads.userservice.model.SignInRequest;
import gaminglads.userservice.model.SignUpRequest;
import gaminglads.userservice.model.User;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserIntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    private String randomUsername;

    public UserIntegrationTest() {
        randomUsername = randomString(10);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public String randomString(int len){
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    @Test
    void testSignUp() throws Exception{
        SignUpRequest signUpRequest = new SignUpRequest(randomUsername, "1234", "1234");

        ResponseEntity<String> entity = restTemplate.postForEntity("https://gaminglads-gateway.herokuapp.com/user/signUp", signUpRequest, String.class);

        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void signIn() throws Exception{
        SignInRequest signInRequest = new SignInRequest(randomUsername, "1234");

        RequestBuilder requestCall = MockMvcRequestBuilders.post("/user/signIn/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signInRequest));
    }
}
