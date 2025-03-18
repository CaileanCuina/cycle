package vacation.planner.cycle.mapper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import vacation.planner.cycle.dto.CycleInformationDto;
import vacation.planner.cycle.dto.UserDto;
import vacation.planner.cycle.entities.CycleInformation;
import vacation.planner.cycle.entities.User;

class UserMapperTest {

    private UserMapperImpl userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void toDto_isNullSave(){
        assertDoesNotThrow(() ->userMapper.toDto(null));
    }

    @Test
    void fromDto_isNullSave(){
        assertDoesNotThrow(() ->userMapper.toEntity(null));
    }

    @Test
    void toDto_shouldMapUserToUserDto() {
        User user = new User();
        user.setUserName("john_doe");
        user.setAvgMensLen(5.0);
        CycleInformation cycle1 = new CycleInformation();
        cycle1.setStartDate(LocalDate.of(2025, 1, 1));
        cycle1.setEndDate(LocalDate.of(2025, 1, 5));
        user.setCycleInformation(Arrays.asList(cycle1));

        UserDto userDto = userMapper.toDto(user);


        assertNotNull(userDto);
        assertEquals("john_doe", userDto.userName());
        assertNotNull(userDto.cycleInformation());
        assertEquals(1, userDto.cycleInformation().size());
        assertEquals(LocalDate.of(2025, 1, 1), userDto.cycleInformation().get(0).startDate());
        assertEquals(LocalDate.of(2025, 1, 5), userDto.cycleInformation().get(0).endDate());
    }

    @Test
    void toEntity_shouldMapUserDtoToUser() {

        UserDto userDto = new UserDto("john_doe", Arrays.asList(new CycleInformationDto(1L,
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 5), null)), 5.0);

        User user = userMapper.toEntity(userDto);

        assertNotNull(user);
        assertEquals("john_doe", user.getUserName());
        assertEquals(5.0, user.getAvgMensLen());
        assertNotNull(user.getCycleInformation());
        assertEquals(1, user.getCycleInformation().size());
        assertEquals(LocalDate.of(2025, 1, 1), user.getCycleInformation().get(0).getStartDate());
        assertEquals(LocalDate.of(2025, 1, 5), user.getCycleInformation().get(0).getEndDate());
    }

    @Test
    void cycleInformationToCycleInformationDto_shouldMapCycleInformationToCycleInformationDto() {
        CycleInformation cycleInformation = new CycleInformation();
        cycleInformation.setStartDate(LocalDate.of(2025, 1, 1));
        cycleInformation.setEndDate(LocalDate.of(2025, 1, 5));

        CycleInformationDto cycleInformationDto = userMapper.cycleInformationToCycleInformationDto(cycleInformation);

        // Assert
        assertNotNull(cycleInformationDto);
        assertEquals(LocalDate.of(2025, 1, 1), cycleInformationDto.startDate());
        assertEquals(LocalDate.of(2025, 1, 5), cycleInformationDto.endDate());
    }

}