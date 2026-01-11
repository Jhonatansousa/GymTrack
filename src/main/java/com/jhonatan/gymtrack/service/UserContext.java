package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.entity.User;
import com.jhonatan.gymtrack.exception.ResourceNotFoundException;
import com.jhonatan.gymtrack.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserContext {

    private final UserRepo repo;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("no user authenticated");

        }

        String userEmail = authentication.getName();
        User user = repo.findByEmail(userEmail);

        if (user == null) {
            throw new ResourceNotFoundException("user not found in context");
        }
        return user;
    }
}
