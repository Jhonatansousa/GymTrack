package com.jhonatan.gymtrack.service;


import com.jhonatan.gymtrack.dto.WorkoutDivisionDTO;
import com.jhonatan.gymtrack.dto.WorkoutDivisionResponseDTO;
import com.jhonatan.gymtrack.entity.User;
import com.jhonatan.gymtrack.entity.WorkoutDivision;
import com.jhonatan.gymtrack.exception.DuplicatedContentException;
import com.jhonatan.gymtrack.exception.ResourceNotFoundException;
import com.jhonatan.gymtrack.mapper.WorkoutDivisionMapper;
import com.jhonatan.gymtrack.repository.UserRepo;
import com.jhonatan.gymtrack.repository.WorkoutDivisionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkoutServiceImpl  implements IWorkoutService {

    public final UserRepo userRepo;
    public final WorkoutDivisionRepo repo;
    public final WorkoutDivisionMapper mapper;
    public final UserContext userContext;

    @Override
    @Transactional
    public WorkoutDivisionResponseDTO createDivision(WorkoutDivisionDTO dto) {

        User user = userContext.getCurrentUser();

        if(repo.existsByNameIgnoreCaseAndUser(dto.name(), user)) {
            throw new DuplicatedContentException("Division name already exists. Please choose a different name");
        }
        //obter usuario autenticado

        WorkoutDivision division = mapper.toEntity(dto);
        division.setUser(user);

        WorkoutDivision newDivision = repo.save(division);
        return mapper.toDTO(newDivision);

    }

    @Override
    public List<WorkoutDivisionResponseDTO> getAllDivisions() {

        User user = userContext.getCurrentUser();

        List<WorkoutDivision> divisions = repo.findAllByUser(user);

        return divisions.stream().map(mapper::toDTO).toList();
    }
}
