package com.jhonatan.gymtrack.dto.authDto;

import java.util.UUID;

public record AuthenticatedUserDTO(
        UUID id,
        String email,
        String name
) {
}
