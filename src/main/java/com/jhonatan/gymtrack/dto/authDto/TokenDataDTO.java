package com.jhonatan.gymtrack.dto.authDto;

import java.util.Set;

public record TokenDataDTO(String email, Set<String> roles) {
}
