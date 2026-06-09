package com.Mike.Proj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mike.Proj.dto.ResponseDto;
import com.Mike.Proj.dto.user.SigninResponseDto;
import com.Mike.Proj.dto.user.SignupDto;
import com.Mike.Proj.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
//for configuring access to the backend controller from
//a different server/origin ie. vue app
public class UserController {

    @Autowired
    UserService userService;

    //signup api
    @PostMapping("/signup")
    public ResponseDto signup(@Valid @RequestBody SignupDto signupDto){
        return userService.signUp(signupDto);
    }

    //signin api
    @GetMapping("/signin")
    public SigninResponseDto signin(){
        return userService.signIn();
    }

    @GetMapping("/login-fail")
    public ResponseDto loginFail(){
        return new ResponseDto("Fail", "Login unsuccessful");
    }

    @GetMapping("/logout")
    public String logout(){
        return ("Logout Success");
    }

}