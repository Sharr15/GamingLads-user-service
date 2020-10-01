package GamingLads.authenticationservice.Service;

import GamingLads.authenticationservice.Model.User;
import GamingLads.authenticationservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    @Autowired
    UserRepository userRepo;

    public boolean signIn(User user){
        List<User> usersList = (List<User>) userRepo.findAll();
        for (User user1: usersList) {
            if(user.getUsername().equals(user1.getUsername()) && user.getPassword().equals(user1.getPassword())){
                return true;
            }
        }
        return false;
    }

    public boolean signUp(User user){
        try{
            userRepo.save(user);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
}


