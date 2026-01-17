package com.jhonatan.gymtrack.service;


import com.jhonatan.gymtrack.dto.workoutdivision.DivisionUpdateDTO;
import com.jhonatan.gymtrack.dto.workoutdivision.WorkoutDivisionDTO;
import com.jhonatan.gymtrack.dto.workoutdivision.WorkoutDivisionResponseDTO;
import com.jhonatan.gymtrack.entity.User;
import com.jhonatan.gymtrack.entity.WorkoutDivision;
import com.jhonatan.gymtrack.exception.DuplicatedContentException;
import com.jhonatan.gymtrack.exception.ResourceNotFoundException;
import com.jhonatan.gymtrack.mapper.WorkoutDivisionMapper;
import com.jhonatan.gymtrack.repository.UserRepo;
import com.jhonatan.gymtrack.repository.WorkoutDivisionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl  implements IWorkoutService {

    public final UserRepo userRepo;
    public final WorkoutDivisionRepo repo;
    public final WorkoutDivisionMapper mapper;
    public final UserContext userContext;

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(readOnly = true)
    public List<WorkoutDivisionResponseDTO> getAllDivisions() {

        User user = userContext.getCurrentUser();

        List<WorkoutDivision> divisions = repo.findAllByUser(user);

        return divisions.stream().map(mapper::toDTO).toList();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkoutDivisionResponseDTO updateDivision(Long divisionID, DivisionUpdateDTO dto) {

        User user = userContext.getCurrentUser();

        WorkoutDivision division = repo.findByIdAndUser(divisionID, user)
                .orElseThrow(() -> new  ResourceNotFoundException("Workout division with id " + divisionID + " does not exist"));

        //nome existente != novo nome && se já existe uma divisão com o novo nome dentro do usuário logado
        if(!division.getName().equalsIgnoreCase(dto.newName()) && repo.existsByNameIgnoreCaseAndUser(dto.newName(), user)) {
            throw new DuplicatedContentException("Division name already exists. Please choose a different name,  o erro ta aqui");
        }

        division.setName(dto.newName());

        //aqui posso usar Dirty Checking, sem precisar do repo.save(...)
        //return mapper.toDTO(repo.save(division));
        return mapper.toDTO(division);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDivision(long divisionId) {
        User user = userContext.getCurrentUser();

        WorkoutDivision division = repo.findByIdAndUser(divisionId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Workout Division not found"));

        repo.delete(division);
    }
}
