package GamingLads.userservice.service;

import GamingLads.userservice.http.HttpWrapper;
import GamingLads.userservice.model.User;
import GamingLads.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepo;
    private final HttpWrapper httpWrapper;

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
            //token = jwtService.createToken(user);
            token = "1234";
        }
        catch(Exception e){
            return null;
        }
        return token;
    }

    public boolean signUp(User user){
        boolean saved = saveUser(user);
        boolean created = createProfile(userRepo.findByUsername(user.getUsername()));
        return saved && created;
    }

    public boolean saveUser(User user){
        try{
            userRepo.save(user);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean createProfile(User user){
        return httpWrapper.invokeHttpCall(HttpMethod.POST, user).getStatusCode() == HttpStatus.CREATED;
    }
}


