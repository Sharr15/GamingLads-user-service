package GamingLads.userservice.controller;

import GamingLads.userservice.model.User;
import GamingLads.userservice.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    private final AuthenticationService authService;

    @Autowired
    public UserController(final AuthenticationService authService) {
        this.authService = authService;
    }

    //get params User user
    @GetMapping("/signIn")
    public ResponseEntity<String> signIn(@RequestBody User user) {
        boolean validated = authService.signIn(user);
        if (validated) {
            String token = authService.createToken(user);
            if (token != null) {
                return new ResponseEntity<>(token, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }

    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@RequestBody User user) {
        if(authService.signUp(user)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }
}
