package GamingLads.authenticationservice.Controller;

import GamingLads.authenticationservice.Model.User;
import GamingLads.authenticationservice.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    AuthenticationService authService;

    //get params User user
    @GetMapping("/signIn")
    public ResponseEntity<Void> signIn(){
        User user = new User("1", "Sharr", "1234");
        boolean validated = authService.signIn(user);
        if(validated){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    //post -> params User user
    @GetMapping("/signUp")
    public ResponseEntity<Void> signUp(){
        User user = new User("1", "Sharr", "1234");
        boolean succeeded = authService.signUp(user);
        if(succeeded){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

}
