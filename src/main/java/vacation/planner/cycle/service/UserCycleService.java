package vacation.planner.cycle.service;

import static vacation.planner.cycle.validation.DateValidator.validateCycleInformation;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vacation.planner.cycle.calculation.CycleParamCalculator;
import vacation.planner.cycle.dto.UserDto;
import vacation.planner.cycle.entities.User;
import vacation.planner.cycle.exceptions.NoUserFoundException;
import vacation.planner.cycle.exceptions.UserNameNotUniqueException;
import vacation.planner.cycle.mapper.UserMapper;
import vacation.planner.cycle.repo.UserRepo;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserCycleService {

    private static final String USER_ALREADY_EXISTS = "Username is already existing!";

    private final UserRepo userRepo;

    private final UserMapper userMapper;

    private static final String USER_NOT_FOUND = "User with name %s not found.";

    public UserDto getUserDtoByName(String userName) {
        log.info("Try fetching user with name {}", userName);
        return userRepo.findById(userName).map(userMapper::toDto)
                .orElseThrow(() -> new NoUserFoundException(String.format(USER_NOT_FOUND, userName)));
    }

    public User getUserByName(String userName) {
        log.info("Try fetching user with name {}", userName);
        return userRepo.findById(userName)
                .orElseThrow(() -> new NoUserFoundException(String.format(USER_NOT_FOUND, userName)));
    }

    public UserDto persistUser(UserDto newUser) {

        if (userRepo.existsById(newUser.userName())) {
            log.error(USER_ALREADY_EXISTS);
            throw new UserNameNotUniqueException(USER_ALREADY_EXISTS);
        }

        validateCycleInformation(newUser);

        var user = userMapper.toEntity(newUser);
        updateUserParams(user);
        return userMapper.toDto(userRepo.save(user));
    }

    public UserDto updateUser(UserDto user) {

        if (!userRepo.existsById(user.userName())) {
            throw new NoUserFoundException(String.format(USER_NOT_FOUND, user.userName()));
        }

        validateCycleInformation(user);

        return userMapper.toDto(updateUserData(user));
    }

    public void updateUserParams(User user) {
        user.setAvgCycleLen(CycleParamCalculator.calculateAvgCycleLen(user.getCycleInformation()));
        user.setCycleStd(CycleParamCalculator.calculateStd(user.getCycleInformation(), user.getAvgCycleLen()));
    }

    private User updateUserData(UserDto userDto) {
        User user = userRepo.findById(userDto.userName()).orElseThrow();
        user.updateFromUser(userMapper.toEntity(userDto));
        updateUserParams(user);
        return userRepo.save(user);
    }
}
