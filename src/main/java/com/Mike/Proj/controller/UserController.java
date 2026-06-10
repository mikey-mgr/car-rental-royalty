package com.Mike.Proj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mike.Proj.dto.ResponseDto;
import com.Mike.Proj.dto.user.SigninResponseDto;
import com.Mike.Proj.dto.user.SignupDto;
import com.Mike.Proj.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
//for configuring access to the backend controller from
//a different server/origin ie. vue app
public class UserController {

    @Autowired
    UserService userService;

    @Autowired(required = false)
    private AuthenticationManager authenticationManager;

    //signup api
    @PostMapping("/signup")
    public ResponseDto signup(@Valid @RequestBody SignupDto signupDto){
        return userService.signUp(signupDto);
    }

    //signin api - returns user info from authenticated principal
    @GetMapping("/signin")
    public SigninResponseDto signin(HttpServletRequest request){
        return userService.signIn(request);
    }

    /**
     * REST API login endpoint - properly authenticates and returns user info
     * Use this instead of form /login for SPA/AJAX requests
     */
    @PostMapping("/api-login")
    public SigninResponseDto apiLogin(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpSession session) {
        try {
            // Authenticate the user using AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Access session to force creation - this triggers JSESSIONID cookie to be sent
            String sessionId = session.getId();
            
            // Store authentication in session - Spring Security will persist it
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            
            System.out.println("Login successful for: " + loginRequest.getEmail() + ", Session ID: " + sessionId + ", Auth: " + authentication.isAuthenticated());

            // Get user info and return
            return userService.signIn(request);
        } catch (AuthenticationException e) {
            System.out.println("Login failed: " + e.getMessage());
            throw new com.Mike.Proj.exceptions.CustomException("Invalid email or password");
        }
    }

    @GetMapping("/login-fail")
    public ResponseDto loginFail(){
        return new ResponseDto("Fail", "Login unsuccessful");
    }

    @GetMapping("/csrf-token")
    public Map<String, String> getCsrfToken(jakarta.servlet.http.HttpServletRequest request) {
        // Properly fetch the CsrfToken object stored by Spring Security on the request
        org.springframework.security.web.csrf.CsrfToken token =
            (org.springframework.security.web.csrf.CsrfToken) request.getAttribute("_csrf");

        String tokenValue = token != null ? token.getToken() : "";

        Map<String, String> response = new HashMap<>();
        response.put("status", "Success");
        response.put("message", "CSRF token generated");
        response.put("token", tokenValue);
        return response;
    }

    @GetMapping("/logout")
    public ResponseDto logout(HttpServletRequest request, HttpSession session) {
        try {
            // Invalidate the session
            if (session != null) {
                session.invalidate();
            }
            // Clear the security context
            SecurityContextHolder.clearContext();
            return new ResponseDto("Success", "Logout successful");
        } catch (Exception e) {
            return new ResponseDto("Fail", "Logout failed: " + e.getMessage());
        }
    }

    /**
     * DTO for API login request
     */
    public static class LoginRequest {
        @jakarta.validation.constraints.Email(message = "Email should be valid")
        @jakarta.validation.constraints.NotBlank(message = "Email is required")
        private String email;
        
        @jakarta.validation.constraints.NotBlank(message = "Password is required")
        @jakarta.validation.constraints.Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}