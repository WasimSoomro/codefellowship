package com.wasim.codefellowship.controllers;

import com.wasim.codefellowship.models.ApplicationUser;
import com.wasim.codefellowship.repositories.ApplicationUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.servlet.ServletException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Controller
public class ApplicationUserController {
    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    HttpServletRequest request;

    public ApplicationUserController() {
    }

    @GetMapping("/login")
    public String getLoginPage(Model m, Principal p) {
        if (p != null) {
            m.addAttribute("username", p.getName());
        }
        return "login.html";
    }

    @GetMapping("/signup")
    public String getSignupPage() {
        return "signup.html";
    }

    @GetMapping("/")
    public String getIndexPage(Model m, Principal p) {

        System.out.println("Principal " + p);

        if (p != null) {
            String username = p.getName();
            ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);

            m.addAttribute("username", username);
            // m.addAttribute("nickname", applicationUser.getNickname());
        } else {
            throw new ResourceNotFoundException("It's a 404");
        }

        return "index.html";
    }

@ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
        ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @GetMapping ("/test")
    public String getTestPage(Principal p, Model m) {
        if(p != null) {
            String username = p.getName();
            ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);

            m.addAttribute("username", username);
//            m.addAttribute("firstname", applicationUserUser.getFirstName());
        }

        return "test.html";
    }

    @GetMapping("/users/{id}")
    public String getUserInfo(Model m, Principal p, @PathVariable Long id) {
        if (p != null)  // not strictly required if your WebSecurityConfig is correct
        {
            String username = p.getName();
            ApplicationUser applicationBrowsingUser = applicationUserRepository.findByUsername(username);

            m.addAttribute("browsingUserUsername", username);
//            m.addAttribute("browsingUserNickname", applicationBrowsingUser.getNickname());
        }

        ApplicationUser applicationUser = applicationUserRepository.findById(id).orElseThrow();
        m.addAttribute("applicationUserUsername", applicationUser.getUsername());
        m.addAttribute("applicationUserId", applicationUser.getId());
//        m.addAttribute("applicationUserNickname", applicationUser.getNickname());

        m.addAttribute("testDate", LocalDateTime.now());



        return "user-info.html";
    }

    @PutMapping("/users/{id}")
    public RedirectView edituserInfo(Model m, Principal p, @PathVariable Long id, String username, RedirectAttributes redir) {

        if(p != null && (p.getName().equals(username))) {
            ApplicationUser applicationUser = applicationUserRepository.findById(id).orElseThrow();
            applicationUser.setUsername(username);
            applicationUserRepository.save(applicationUser);
        } else {
            redir.addFlashAttribute("errorMessage", "Cannot edit another user's page!");
        }
        return new RedirectView("users/" + id);
    }

    @PutMapping("/followUser/{id}")
    public RedirectView followUser(Principal p, @PathVariable Long id) {
    ApplicationUser userToFollow = applicationUserRepository.findById(id).orElseThrow();
    ApplicationUser browsingUser = applicationUserRepository.findByUsername(p.getName());

        Set<ApplicationUser> usersIFollow = browsingUser.getUsersIFollow();
        if(usersIFollow.contains(userToFollow)){
            usersIFollow.remove(userToFollow);
        } else {
            browsingUser.getUsersIFollow().add(userToFollow);
        }

        applicationUserRepository.save(browsingUser);
        return new RedirectView(("users/" + id));
    }


    @PostMapping("/signup")
    public RedirectView postSignup(String username, String password, String firstName, String lastName, LocalDate dateOfBirth, String bio) {
        ApplicationUser user = new ApplicationUser(username, firstName, lastName, dateOfBirth, bio);
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

    @GetMapping("/allUsers")
    public String getUserInfo(Model m, Principal p) {

        if (p != null) {
            String username = p.getName();
            m.addAttribute("username", username);
        }

        List<ApplicationUser> allUsers = applicationUserRepository.findAll();
        m.addAttribute("allUsers", allUsers);
        return "all-users.html";
    }
}

