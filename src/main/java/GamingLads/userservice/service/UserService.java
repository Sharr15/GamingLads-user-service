package GamingLads.userservice.service;

import GamingLads.userservice.model.Role;
import GamingLads.userservice.model.SignInRequest;
import GamingLads.userservice.model.User;
import GamingLads.userservice.repository.RoleRepository;
import GamingLads.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional
public class UserService {

    private final UserRepository userRepo;
    private final RestTemplate restTemplate;
    private final JwtService jwtService;
    private final RoleRepository roleRepo;

    public boolean signIn(SignInRequest signInRequest) {
        List<User> usersList = userRepo.findAll();
        for (User user1 : usersList) {
            if (signInRequest.getUsername().equals(user1.getUsername()) && signInRequest.getPassword().equals(user1.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public String createToken(SignInRequest signInRequest){
        User user = userRepo.findByUsername(signInRequest.getUsername());
        return jwtService.generateToken(user);
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
        Role role = roleRepo.findByName("USER");
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
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


