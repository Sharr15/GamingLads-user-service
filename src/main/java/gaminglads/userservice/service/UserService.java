package gaminglads.userservice.service;

import gaminglads.userservice.model.Role;
import gaminglads.userservice.model.SignInRequest;
import gaminglads.userservice.model.User;
import gaminglads.userservice.repository.RoleRepository;
import gaminglads.userservice.repository.UserRepository;
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

    //checks if the credentials are correct
    //returns true if it is correct, false if not
    public boolean signIn(SignInRequest signInRequest) {
        List<User> usersList = userRepo.findAll();
        for (User user1 : usersList) {
            if (signInRequest.getUsername().equals(user1.getUsername()) && signInRequest.getPassword().equals(user1.getPassword())) {
                return true;
            }
        }
        return false;
    }

    //returns token
    public String createToken(SignInRequest signInRequest){
        User user = userRepo.findByUsername(signInRequest.getUsername());
        return jwtService.generateToken(user);
    }


    //saves user, if succeeded, create profile
    //returns boolean
    public boolean signUp(User user){
        boolean saved = saveUser(user);
        if(saved){
             ResponseEntity<?> entity = createProfile(userRepo.findByUsername(user.getUsername()));
            return entity.getStatusCode().equals(HttpStatus.CREATED);
        }
        return false;
    }

    //saves user
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

    //send request to other service
    public ResponseEntity<User> createProfile(User user){
        return restTemplate.postForEntity("http://localhost:8089/profile/create", user, User.class);
    }
}


