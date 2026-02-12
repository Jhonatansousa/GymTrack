package com.jhonatan.gymtrack.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercise_sets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Integer reps;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    @ToString.Exclude
    private Exercise exercise;
}
