package GamingLads.userservice.controller;

import GamingLads.userservice.model.User;
import GamingLads.userservice.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class UserController {

    private final AuthenticationService authService;

    @Autowired
    public UserController(final AuthenticationService authService){
        this.authService = authService;
    }

    //get params User user
    @GetMapping("/signIn")
    public ResponseEntity<Void> signIn(){
        User user = new User("1", "Sharony", "1234");
        boolean validated = authService.signIn(user);
        if(validated){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@RequestBody User user){
        boolean succeeded = authService.signUp(user);
        if(succeeded){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

}
