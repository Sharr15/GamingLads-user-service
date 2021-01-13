package gaminglads.userservice.controller;

import gaminglads.userservice.exceptions.*;
import gaminglads.userservice.model.SignInRequest;
import gaminglads.userservice.model.SignUpRequest;
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

    //params signInRequest from front-end
    //returns jwt token if validated
    @PostMapping("/signIn/user")
    public ResponseEntity<String> signInUser(@RequestBody SignInRequest signInRequest) throws InvalidCredentialsException, TokenNotCreatedException, UserNotFoundException {
        String jwt = userService.signIn(signInRequest);
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }

    //params signUpRequest from front-end
    //returns ok if succeeded
    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest signUpRequest) throws UserNotSavedException, ProfileNotCreatedException {
        userService.signUp(signUpRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
