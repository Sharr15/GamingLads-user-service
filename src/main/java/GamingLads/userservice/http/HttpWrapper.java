package GamingLads.userservice.http;

import GamingLads.userservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpWrapper {

    private final RestTemplate restTemplate;

    private String url = "http://localhost:8083/profile/create";

    @Autowired
    public HttpWrapper(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> invokeHttpCall(HttpMethod httpMethod, User data){
        HttpEntity<User> request = new HttpEntity<>(data);

        return executeHttpCall(request, httpMethod);
    }

    private ResponseEntity<?> executeHttpCall(HttpEntity<?> request, HttpMethod httpMethod) {
        try {
            return restTemplate.exchange(url, httpMethod, request, User.class);
        } catch (HttpStatusCodeException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
