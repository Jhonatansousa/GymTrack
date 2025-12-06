package com.jhonatan.gymtrack.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class AuthToken {
    private String token;

    public AuthToken(String token) {
        this.token = token;
    }
}
