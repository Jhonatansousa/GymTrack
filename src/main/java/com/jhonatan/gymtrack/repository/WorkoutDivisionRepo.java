package com.jhonatan.gymtrack.repository;

import com.jhonatan.gymtrack.entity.User;
import com.jhonatan.gymtrack.entity.WorkoutDivision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkoutDivisionRepo extends JpaRepository<WorkoutDivision, Long> {
    boolean existsByNameIgnoreCaseAndUser(String name, User user);

    Optional<WorkoutDivision> findByIdAndUser(Long id, User user);

    List<WorkoutDivision> findAllByUser(User user);
}
