package com.jhonatan.gymtrack.service.impl;


import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetResponseDTO;
import com.jhonatan.gymtrack.dto.exerciseset.ExerciseSetUpdateDTO;
import com.jhonatan.gymtrack.entity.Exercise;
import com.jhonatan.gymtrack.entity.ExerciseSet;
import com.jhonatan.gymtrack.entity.User;
import com.jhonatan.gymtrack.entity.WorkoutDivision;
import com.jhonatan.gymtrack.exception.ResourceNotFoundException;
import com.jhonatan.gymtrack.mapper.ExerciseSetMapper;
import com.jhonatan.gymtrack.repository.ExerciseRepo;
import com.jhonatan.gymtrack.repository.ExerciseSetRepo;
import com.jhonatan.gymtrack.service.UserContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//integra o mockito com o junit5 pra iniciar os mocks automaticamente
@ExtendWith(MockitoExtension.class)
public class ExerciseSetServiceImplTest {

    @Mock // -> objetos falsos / eu CONTROLO o que é RETORNADO
    private ExerciseSetRepo setRepo;

    @Mock
    private ExerciseRepo exerciseRepo;

    @Mock
    private UserContext userContext;

    @Mock
    private ExerciseSetMapper mapper;

    @InjectMocks // -> cria a instância real do service e injeta os Mocks acima
    private ExerciseSetServiceImpl service;

//createNewSet
    @Test
    @DisplayName("Should create a ExerciseSet successfully when data is valid")
    void shouldCreateExerciseSetWhenDataIsValid() {
        //arrange -> preparando o cenario
        UUID userId = UUID.randomUUID();
        User  user = User.builder().id(userId).email("email@test.com").build();

        WorkoutDivision division = WorkoutDivision.builder().id(1L).user(user).build();
        Exercise exercise = Exercise.builder().id(10L).workoutDivision(division).build();

        ExerciseSetDTO dto = new ExerciseSetDTO(10L, "warm-up", 8, 50.0);

        ExerciseSet savedSet = ExerciseSet.builder().id(100L).name("warm-up").build();
        ExerciseSetResponseDTO expectedResponse = new ExerciseSetResponseDTO(10L, 100L, "warm-up", 8, 50.0);

        // digo os mocks o que fazr
        when(userContext.getCurrentUser()).thenReturn(user);
        when(exerciseRepo.findById(10L)).thenReturn(Optional.of(exercise));
        //quando o setRepo salvar qualquer ExerciseSet -> retorna o savedSet que criamos
        //obs: any -> devo importar de: import static org.mockito.ArgumentMatchers.any;
        when(setRepo.save(any(ExerciseSet.class))).thenReturn(savedSet);
        when(mapper.toDTO(savedSet)).thenReturn(expectedResponse);

        //ACT -> execução
        service.createNewSet(dto);

        //ASSERT
        //crio um capturador para a classe ExerciseSet (explicação no docs do obsidian)
        ArgumentCaptor<ExerciseSet> captor = ArgumentCaptor.forClass(ExerciseSet.class);

        //verificamos o save e "capturo" o que foi passado pra ele
        verify(setRepo, times(1)).save(captor.capture());

        //pegamos o valor capturado
        ExerciseSet capturedSet = captor.getValue();

        ///faz o assert normal, mas com uma ressalva que quando der erro, a mensagem fica bem mais clara
        assertEquals("warm-up", capturedSet.getName());
        assertEquals(8, capturedSet.getReps());
        assertEquals(50.0, capturedSet.getWeight());

    }

    @Test
    @DisplayName("Should auto-generate name when ExerciseSet Name is Null")
    void shouldAutoGenerateName_WhenNameIsNull() {
        //arrange
        User user = User.builder().id(UUID.randomUUID()).build();
        WorkoutDivision division = WorkoutDivision.builder().user(user).build();
        Exercise exercise = Exercise.builder().id(10L).workoutDivision(division).build();

        //dto com nome null
        ExerciseSetDTO dto = new ExerciseSetDTO(10L, null, 6, 12.3);

        when(userContext.getCurrentUser()).thenReturn(user);
        when(exerciseRepo.findById(10L)).thenReturn(Optional.of(exercise));
        when(setRepo.countByExercise(exercise)).thenReturn(2); //defino como já tivesse 2 sets

        //captura o objeto que é passado pro save pra verificar o nome gerado
        when(setRepo.save(any(ExerciseSet.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDTO(any())).thenReturn(null); // -> aqui não importa o retorno do dto, pq quero validar o auto name

        // act
        service.createNewSet(dto);

        //assert
        //verifica se o save foi chamado com um set no qual o nome é "3" (2 existentes que defini lá em cima + 1)
        verify(setRepo).save(argThat(set -> set.getName().equals("3")));
    }

    @Test
    @DisplayName("Should auto-generate name when ExerciseSet Name is EMPTY")
    void shouldAutoGenerateName_WhenNameIsEmpty() {
        //arrange
        User user = User.builder().id(UUID.randomUUID()).build();
        WorkoutDivision division = WorkoutDivision.builder().user(user).build();
        Exercise exercise = Exercise.builder().id(10L).workoutDivision(division).build();

        //dto com nome null
        ExerciseSetDTO dto = new ExerciseSetDTO(10L, "", 6, 12.3);

        when(userContext.getCurrentUser()).thenReturn(user);
        when(exerciseRepo.findById(10L)).thenReturn(Optional.of(exercise));
        when(setRepo.countByExercise(exercise)).thenReturn(2); //defino como já tivesse 2 sets

        //captura o objeto que é passado pro save pra verificar o nome gerado
        when(setRepo.save(any(ExerciseSet.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDTO(any())).thenReturn(null); // -> aqui não importa o retorno do dto, pq quero validar o auto name

        // act
        service.createNewSet(dto);

        //assert
        //verifica se o save foi chamado com um set no qual o nome é "3" (2 existentes que defini lá em cima + 1)
        verify(setRepo).save(argThat(set -> set.getName().equals("3")));
    }

    @Test
    @DisplayName("Should set reps & weight to zero when receive null")
    void shouldSetRepsToZero_WhenReceiveNull() {
        User user = User.builder().id(UUID.randomUUID()).build();
        WorkoutDivision division = WorkoutDivision.builder().user(user).build();
        Exercise exercise = Exercise.builder().id(1L).workoutDivision(division).build();
        ExerciseSetDTO dto = new ExerciseSetDTO(10L, null, null, null);

        when(userContext.getCurrentUser()).thenReturn(user);
        when(exerciseRepo.findById(10L)).thenReturn(Optional.of(exercise));
        when(setRepo.countByExercise(exercise)).thenReturn(2);
        when(mapper.toDTO(any())).thenReturn(null);

        service.createNewSet(dto);

        verify(setRepo).save(argThat(set -> set.getReps() == 0 && set.getWeight() == 0.0));
    }

    @Test
    @DisplayName("Should throw Exception when exercise id not valid")
    void shouldThrowException_WhenExerciseIdIsNotValid() {
        User user = User.builder().id(UUID.randomUUID()).build();
        ExerciseSetDTO dto = new ExerciseSetDTO(12L, "warm-up", 8, 50.0);

        when(userContext.getCurrentUser()).thenReturn(user);
        when(exerciseRepo.findById(12L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.createNewSet(dto));

        verify(setRepo, never()).save(any());

    }

    @Test
    @DisplayName("Should throw Exception when user try add ExerciseSet on another User")
    void shouldThrowException_WhenUserTryAddExerciseSetOnAnotherUser() {
        //arrange
        User loggedUser = User.builder().id(UUID.randomUUID()).build();
        User otherUser = User.builder().id(UUID.randomUUID()).build();

        WorkoutDivision division = WorkoutDivision.builder().user(otherUser).build();
        Exercise exercise = Exercise.builder().id(10L).workoutDivision(division).build();
        ExerciseSetDTO dto = new ExerciseSetDTO(10L, "Test", 8, 15.0);

        when(userContext.getCurrentUser()).thenReturn(loggedUser);
        when(exerciseRepo.findById(10L)).thenReturn(Optional.of(exercise));

        //ACT & ASSERT
        //esperamos que o metodo lance ResourceNotFoundException (devo criar outra depois)
        assertThrows(ResourceNotFoundException.class, () -> service.createNewSet(dto));

        //garante que NUNCA foi chamado o save
        verify(setRepo, never()).save(any());
    }
//===========================================================================


    //get tests
    @Test
    @DisplayName("Should return exercise sets when exercise exists for current user")
    void shouldReturnSets_WhenExerciseExistsForCurrentUser() {
        User user = User.builder().id(UUID.randomUUID()).build();
        WorkoutDivision division = WorkoutDivision.builder().user(user).build();
        Exercise exercise = Exercise.builder().id(10L).workoutDivision(division).build();

        //simulando que tem 2 sets no banc
        List<ExerciseSet> setsFromDb = List.of(
                ExerciseSet.builder().id(100L).name("set 1").build(),
                ExerciseSet.builder().id(101L).name("set 2").build()
        );

        //simulando conversão pro DTO
        ExerciseSetResponseDTO dto1 = new ExerciseSetResponseDTO(10L, 100L, "Set 1", 12, 25.5);
        ExerciseSetResponseDTO dto2 = new ExerciseSetResponseDTO(10L, 101L, "Set 2", 12, 25.5);

        when(userContext.getCurrentUser()).thenReturn(user);

        when(exerciseRepo.findByIdAndUser(10L, user)).thenReturn(Optional.of(exercise));
        when(setRepo.findAllByExerciseIdOrderByIdAsc(10L)).thenReturn(setsFromDb);
        when(mapper.toDTO(setsFromDb.get(0))).thenReturn(dto1);
        when(mapper.toDTO(setsFromDb.get(1))).thenReturn(dto2);

        //act
        List<ExerciseSetResponseDTO> result = service.getSetsByExercise(10L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        verify(exerciseRepo).findByIdAndUser(10L, user);
        verify(setRepo).findAllByExerciseIdOrderByIdAsc(10L);


    }

    @Test
    @DisplayName("Should return empty list when exercise exists but has no sets")
    void shouldReturnEmptyList_WhenExerciseNoSetsFound(){
        User user =  User.builder().id(UUID.randomUUID()).build();
        Exercise exercise = Exercise.builder().id(20L).build();

        when(userContext.getCurrentUser()).thenReturn(user);
        when(exerciseRepo.findByIdAndUser(exercise.getId(), user)).thenReturn(Optional.of(exercise));
        when(setRepo.findAllByExerciseIdOrderByIdAsc(20L)).thenReturn(Collections.emptyList());


        List<ExerciseSetResponseDTO> result = service.getSetsByExercise(20L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(setRepo).findAllByExerciseIdOrderByIdAsc(20L);
    }

    @Test
    @DisplayName("Should throw Exception when getting sets from invalid exerccise id")
    void shouldThrowException_WhenExerciseIdIsInvalid() {
        User user = User.builder().id(UUID.randomUUID()).build();

        when(userContext.getCurrentUser()).thenReturn(user);
        when(exerciseRepo.findByIdAndUser(999L, user)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getSetsByExercise(999L));

        verify(setRepo, never()).findAllByExerciseIdOrderByIdAsc(any());
    }
//===========================================================================


    //Update tests
    @Test
    @DisplayName("Should update only provided fields")
    void shouldUpdateOnlyProvidedFields() {
        //range
        User user =  User.builder().id(UUID.randomUUID()).build();

        //set existente no banco mockado
        ExerciseSet existingSet = ExerciseSet.builder()
                .id(50L)
                .name("Old Name")
                .reps(10)
                .weight(23.4)
                .build();

        //dto de atualização com apenas o "PESO" novo (o resto null)
        ExerciseSetUpdateDTO updateDTO = new ExerciseSetUpdateDTO(null, null, 35.0);

        when(userContext.getCurrentUser()).thenReturn(user);
        when(setRepo.findByIdAndUser(50L, user)).thenReturn(Optional.of(existingSet));

        //act
        service.updateExerciseSet(50L, updateDTO);

        //assert
        //como o metodo é o @Transactional, ele altera o objeto "managed". verificamos objetos entre si
        assertEquals(35.0, existingSet.getWeight()); //mudou
        assertEquals("Old Name", existingSet.getName()); //manteve o mesmo nome do antigo
        assertEquals(10, existingSet.getReps()); // manteve o mesmo rep
    }

    @Test
    @DisplayName("Should NOT update fiels when DTO properties are null")
    void shouldNotUpdateFields_WhenDTOPropertiesAreNull() {
        User user =  User.builder().id(UUID.randomUUID()).build();
        ExerciseSet existingSet = ExerciseSet.builder().id(1L).name("Original Name").reps(10).weight(12.3).build();

        ExerciseSetUpdateDTO updateDTO = new ExerciseSetUpdateDTO(null, null, null);

        when(userContext.getCurrentUser()).thenReturn(user);
        when(setRepo.findByIdAndUser(1L, user)).thenReturn(Optional.of(existingSet));

        service.updateExerciseSet(1L, updateDTO);

        assertEquals("Original Name", existingSet.getName());
        assertEquals(10, existingSet.getReps());
        assertEquals(12.3, existingSet.getWeight());
    }

    @Test
    @DisplayName("Should throw NotFoundException when updating non-existent set")
    void shouldThrowNotFoundException_WhenExerciseSetIdIsInvalid() {
        User user = User.builder().id(UUID.randomUUID()).build();
        ExerciseSetUpdateDTO dto = new ExerciseSetUpdateDTO("updating name", 12, 100.0);

        when(userContext.getCurrentUser()).thenReturn(user);
        when(setRepo.findByIdAndUser(99L, user)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateExerciseSet(99L, dto));
    }
//===========================================================================


//delete

    @Test
    @DisplayName("Should delete ExerciseSet Successfully")
    void shouldDeleteExerciseSetSuccessfully() {
        User user = User.builder().id(UUID.randomUUID()).build();
        ExerciseSet set = new ExerciseSet();

        when(userContext.getCurrentUser()).thenReturn(user);
        when(setRepo.findByIdAndUser(1L, user)).thenReturn(Optional.of(set));

        service.deleteExerciseSet(1L);

        verify(setRepo, times(1)).delete(set);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when exerciseSet id is not valid")
    void shouldThrowResourceNotFoundException_WhenExerciseSetIdIsInvalid() {
        User user = User.builder().id(UUID.randomUUID()).build();

        when(userContext.getCurrentUser()).thenReturn(user);
        when(setRepo.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteExerciseSet(1L));

        verify(setRepo, never()).delete(any(ExerciseSet.class));
    }
}
