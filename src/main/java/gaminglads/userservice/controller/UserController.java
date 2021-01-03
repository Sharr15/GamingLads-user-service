package gaminglads.userservice.controller;

import gaminglads.userservice.model.SignInRequest;
import gaminglads.userservice.model.User;
import gaminglads.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

//    @PostMapping("/signIn/admin")
//    public ResponseEntity<String> signInAdmin(@RequestBody SignInRequest signInRequest) {
//        boolean validated = userService.signIn(signInRequest);
//        if (validated) {
//            final String jwt = userService.createToken(signInRequest);
//            if (jwt != null) {
//                return new ResponseEntity<>(jwt, HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.CONFLICT);
//            }
//        }
//        return new ResponseEntity<>(HttpStatus.CONFLICT);
//    }

    //params User from front-end
    //returns jwt token if validated
    @PostMapping("/signIn/user")
    public ResponseEntity<String> signInUser(@RequestBody SignInRequest signInRequest) {
        boolean validated = userService.signIn(signInRequest);
        if (validated) {
            final String jwt = userService.createToken(signInRequest);
            if (jwt != null) {
                return new ResponseEntity<>(jwt, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    //params user from front-end
    //returns ok if succeeded or conflict if failed
    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@RequestBody User user) {
        if(userService.signUp(user)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}