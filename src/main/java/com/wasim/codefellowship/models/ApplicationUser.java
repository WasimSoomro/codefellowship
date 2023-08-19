package com.wasim.codefellowship.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String bio;

    @ManyToMany
    @JoinTable(
    name= "followers_to_followees",
            joinColumns = {@JoinColumn(name="userWhoIsFollowing")},
            inverseJoinColumns = {@JoinColumn(name="FollowedUser")})
    Set<ApplicationUser> usersIFollow = new HashSet<>();

    @ManyToMany(
            mappedBy = "usersIFollow"
    )
    Set<ApplicationUser> usersWhoFollowMe = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ApplicationUser> getUsersIFollow() {
        return usersIFollow;
    }

    public void setUsersIFollow(Set<ApplicationUser> usersIFollow) {
        this.usersIFollow = usersIFollow;
    }

    public Set<ApplicationUser> getUsersWhoFollowMe() {
        return usersWhoFollowMe;
    }

    public void setUsersWhoFollowMe(Set<ApplicationUser> usersWhoFollowMe) {
        this.usersWhoFollowMe = usersWhoFollowMe;
    }

    public ApplicationUser() {
    }

    public ApplicationUser(String username, String firstName, String lastName, LocalDate dateOfBirth, String bio) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
