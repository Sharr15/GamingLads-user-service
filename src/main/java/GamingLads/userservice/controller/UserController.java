package GamingLads.userservice.controller;

import GamingLads.userservice.model.User;
import GamingLads.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    //get params User user
    @GetMapping("/signIn/admin")
    public ResponseEntity<String> signInAdmin(@RequestBody User user) {
        boolean validated = userService.signIn(user);
        if (validated) {
            String token = userService.createToken(user);
            if (token != null) {
                return new ResponseEntity<>(token, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }

    //get params User user
    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/signIn/user")
    public ResponseEntity<String> signInUser(@RequestBody User user) {
        boolean validated = userService.signIn(user);
        if (validated) {
            String token = userService.createToken(user);
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
        if(userService.signUp(user)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }
}
