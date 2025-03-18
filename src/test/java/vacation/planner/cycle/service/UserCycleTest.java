package vacation.planner.cycle.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import vacation.planner.cycle.dto.UserDto;
import vacation.planner.cycle.entities.User;
import vacation.planner.cycle.exceptions.NoUserFoundException;
import vacation.planner.cycle.exceptions.UserNameNotUniqueException;
import vacation.planner.cycle.mapper.UserMapper;
import vacation.planner.cycle.repo.UserRepo;

@ExtendWith(MockitoExtension.class)
class UserCycleTest {

    private final static double CYCLE_LENGTH = 28.0;
    @InjectMocks
    private UserCycleService userCycleService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserMapper userMapper;

    private static final String USER_NAME = "testUser";

    @Test
    void getUserDtoByName_ShouldReturnUserDto_WhenUserExists() {
        User user = createUser();
        UserDto expectedUserDto = new UserDto(USER_NAME, List.of(), CYCLE_LENGTH);

        when(userRepo.findById(USER_NAME)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expectedUserDto);

        UserDto result = userCycleService.getUserDtoByName(USER_NAME);

        assertNotNull(result);
        assertEquals(expectedUserDto, result);
        verify(userRepo, times(1)).findById(USER_NAME);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void getUserDtoByName_ShouldThrowException_WhenUserNotFound() {

        when(userRepo.findById(USER_NAME)).thenReturn(Optional.empty());

        NoUserFoundException thrown = assertThrows(NoUserFoundException.class,
                () -> userCycleService.getUserDtoByName(USER_NAME));

        assertEquals("User with name testUser not found.", thrown.getMessage());
    }

    @Test
    void persistUser_ShouldSaveUser_WhenUserDoesNotExist() {
        // Given
        UserDto newUserDto = new UserDto(USER_NAME, List.of(), CYCLE_LENGTH);
        User newUser = createUser();
        UserDto savedUserDto = new UserDto(USER_NAME, List.of(), CYCLE_LENGTH);

        when(userRepo.existsById(USER_NAME)).thenReturn(false);
        when(userMapper.toEntity(newUserDto)).thenReturn(newUser);
        when(userRepo.save(newUser)).thenReturn(newUser);
        when(userMapper.toDto(newUser)).thenReturn(savedUserDto);

        // When
        UserDto result = userCycleService.persistUser(newUserDto);

        // Then
        assertNotNull(result);
        assertEquals(savedUserDto, result);

        verify(userRepo, times(1)).existsById(USER_NAME);
        verify(userRepo, times(1)).save(newUser);
        verify(userMapper, times(1)).toEntity(newUserDto);
        verify(userMapper, times(1)).toDto(newUser);
    }

    @Test
    void persistUser_ShouldThrowException_WhenUserAlreadyExists() {

        UserDto newUserDto = new UserDto(USER_NAME, List.of(), CYCLE_LENGTH);
        when(userRepo.existsById(USER_NAME)).thenReturn(true);

        UserNameNotUniqueException thrown = assertThrows(UserNameNotUniqueException.class,
                () -> userCycleService.persistUser(newUserDto));

        assertEquals("Username is already existing!", thrown.getMessage());
        verify(userRepo, times(1)).existsById(USER_NAME);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        // Given
        UserDto userDto = new UserDto(USER_NAME, List.of(), CYCLE_LENGTH);
        when(userRepo.existsById(USER_NAME)).thenReturn(false);

        NoUserFoundException thrown = assertThrows(NoUserFoundException.class,
                () -> userCycleService.updateUser(userDto));

        assertEquals("User with name testUser not found.", thrown.getMessage());
        verify(userRepo, times(1)).existsById(USER_NAME);
    }
    private User createUser(){
        return new User(USER_NAME,0.0, CYCLE_LENGTH, 4.0, List.of());
    }
}
