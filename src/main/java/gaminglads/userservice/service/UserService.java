package gaminglads.userservice.service;

import gaminglads.userservice.exceptions.*;
import gaminglads.userservice.model.Role;
import gaminglads.userservice.model.SignInRequest;
import gaminglads.userservice.model.SignUpRequest;
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
    public String signIn(SignInRequest signInRequest) throws InvalidCredentialsException, TokenNotCreatedException, UserNotFoundException {
        List<User> usersList = userRepo.findAll();
        for (User user1 : usersList) {
            if (signInRequest.getUsername().equals(user1.getUsername()) && signInRequest.getPassword().equals(user1.getPassword())) {
                return createToken(signInRequest);
            }
        }
        throw new InvalidCredentialsException();
    }

    //returns token
    public String createToken(SignInRequest signInRequest) throws TokenNotCreatedException, UserNotFoundException {
        User user = new User();

        try{
            user = userRepo.findByUsername(signInRequest.getUsername());
        }
        catch(Exception e){
            throw new UserNotFoundException();
        }

        String token = jwtService.generateToken(user);

        if(token == null){
            throw new TokenNotCreatedException();
        }

        return token;
    }

    //saves user, if succeeded, create profile
    //returns boolean
    //TODO: username unique check throws UserNameNotUniqueException
    public void signUp(SignUpRequest signUpRequest) throws UserNotSavedException, ProfileNotCreatedException {
        try{
            saveUser(signUpRequest);
        }
        catch(Exception e){
            throw new UserNotSavedException();
        }
        ResponseEntity<String> entity = createProfile(userRepo.findByUsername(signUpRequest.getUsername()));
        if(entity.getStatusCode() == HttpStatus.CONFLICT){
            throw new ProfileNotCreatedException();
        }
    }

    //saves user
    public void saveUser(SignUpRequest signUpRequest) throws UserNotSavedException{
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setActive(false);
        Role role = roleRepo.findByName("USER");
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        try{
            userRepo.save(user);
        }
        catch (Exception e){
            throw new UserNotSavedException();
        }
    }

    //send request to other service
    public ResponseEntity<String> createProfile(User user) throws ProfileNotCreatedException{
        return restTemplate.postForEntity("https://gaminglads-gateway.herokuapp.com/profile/create", user, String.class);
    }
}


