package com.wasim.codefellowship.controllers;

import com.wasim.codefellowship.models.ApplicationUser;
import com.wasim.codefellowship.repositories.ApplicationUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.servlet.ServletException;
import java.time.LocalDate;

@Controller
public class ApplicationUserController {
    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    HttpServletRequest request;
    @GetMapping("/login")
    public String getLoginPage() {
        return "login.html";
    }

//    @PostMapping("/login")
//    public RedirectView postLoginPage(String username, String password){
//    }

    @GetMapping("/signup")
    public String getSignupPage() {
        return "signup.html";
    }

    @GetMapping("/")
    public String etPhoneHome() {
        return "index.html";
    }
    @PostMapping("/signup")
    public RedirectView postSignup(String username, String password, String firstName, String lastName, String bio, LocalDate dateOfBirth) {
        ApplicationUser user = new ApplicationUser();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBio(bio);

        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        applicationUserRepository.save(user);

        //handle auth
        authWithHttpServletRequest(username, password);

        return new RedirectView("/");
    }

    public void authWithHttpServletRequest(String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            System.out.println("Error while logging in.");
            e.printStackTrace();
        }
    }

}

