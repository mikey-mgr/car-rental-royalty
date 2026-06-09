package com.Mike.Proj.service;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.Mike.Proj.dto.ResponseDto;
import com.Mike.Proj.dto.user.SigninResponseDto;
import com.Mike.Proj.dto.user.SignupDto;
import com.Mike.Proj.exceptions.CustomException;
import com.Mike.Proj.model.AuthenticationToken;
import com.Mike.Proj.model.User;
import com.Mike.Proj.repository.TokenRepository;
import com.Mike.Proj.repository.UserRepo;
import jakarta.transaction.Transactional;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    UserRepo userRepo;

    

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //signup service
    @Transactional
    public ResponseDto signUp(SignupDto signupDto) {
        //check if user exists
        if (Objects.nonNull(userRepo.findByEmail(signupDto.getEmail()))){
            throw new CustomException("User with email is already present");
        }

        //hash password
        String encryptedPassword = passwordEncoder.encode(signupDto.getPassword());
        
        User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), encryptedPassword, "USER");
        userRepo.save(user);

        // Do not create long-lived bearer tokens. Authentication will rely on
        // the Spring Security session/principal. If a token system is required
        // use short-lived JWTs with rotation and revocation in a secure store.

        ResponseDto responseDto = new ResponseDto("Success", "New User has been created");
        return responseDto;
    }

    //api for login success to print user token and role
    public SigninResponseDto signIn() {

        //get current logged in user in the springsecurity
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepo.findByEmail(email);
        String userRole = null;
        try{
            userRole = user.getRole();
        } catch (Exception e) {
            throw new CustomException("User does not exist");
        }

        // Do not return a long-lived bearer token. Client-side authentication
        // should continue to use browser cookies (Spring Session) and the
        // authenticated principal. Token is intentionally null.
        return new SigninResponseDto("Login Success", null, userRole);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username '" + username +"' not found");
        }
        String encodedPassword;

        try {
            encodedPassword = user.getPassword();
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        
        return org.springframework.security.core.userdetails.User.withUsername(username)
            .password(encodedPassword)
            .roles(user.getRole())
            .build();
    }
}
