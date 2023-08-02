package com.wasim.codefellowship.repositories;

import com.wasim.codefellowship.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    public ApplicationUser findByUsername(String username);

}
