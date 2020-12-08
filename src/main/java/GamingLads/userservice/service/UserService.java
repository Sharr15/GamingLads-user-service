package GamingLads.userservice.service;

import GamingLads.userservice.model.User;
import GamingLads.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service @RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final RestTemplate restTemplate;

    public boolean signIn(User user) {
        List<User> usersList = (List<User>) userRepo.findAll();
        for (User user1 : usersList) {
            if (user.getUsername().equals(user1.getUsername()) && user.getPassword().equals(user1.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public String createToken(User user){
        String token;
        try {
            //token = restTemplate.getForObject("http://localhost:8089/auth/create" + user, String.class)
            token = "1234";
        }
        catch(Exception e){
            return null;
        }
        return token;
    }


    public boolean signUp(User user){
        boolean created = false;
        boolean saved = saveUser(user);
        if(saved){
             created = createProfile(userRepo.findByUsername(user.getUsername()));
        }
        return created;
    }

    public boolean saveUser(User user){
        user.setActive(false);
        try{
            userRepo.save(user);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean createProfile(User user){
        ResponseEntity<String> entity = restTemplate.postForEntity("http://localhost:8089/profile/create", user, String.class);
        //String body = entity.getBody();
        //MediaType contentType = entity.getHeaders().getContentType();
        HttpStatus statusCode = entity.getStatusCode();
        return statusCode.equals(HttpStatus.CREATED);
    }
}


