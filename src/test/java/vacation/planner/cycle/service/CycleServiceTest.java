package vacation.planner.cycle.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import vacation.planner.cycle.calculation.PeriodForecastCalculator;
import vacation.planner.cycle.dto.CycleForeCastDto;
import vacation.planner.cycle.entities.User;
import vacation.planner.cycle.repo.CycleRepo;

@ExtendWith(MockitoExtension.class)
 class CycleServiceTest {
    @InjectMocks
    private CycleService cycleService;

    @Mock
    private CycleRepo cycleRepo;

    @Mock
    private UserCycleService userCycleService;

    private static final String USER_NAME = "testUser";


    @Test
    void getForeCastByDate_ShouldReturnForecast() {

        LocalDate startDate = LocalDate.of(2025, 3, 10);
        LocalDate endDate = LocalDate.of(2025, 3, 20);
        User user = new User();
        CycleForeCastDto expectedForecast = new CycleForeCastDto(new HashMap<>()); // Populate fields accordingly

        when(userCycleService.getUserByName(USER_NAME)).thenReturn(user);
        mockStatic(PeriodForecastCalculator.class).when(() -> PeriodForecastCalculator.calcForeCast(user, startDate, endDate))
                .thenReturn(expectedForecast);

        CycleForeCastDto result = cycleService.getForeCastByDate(USER_NAME, startDate, endDate);

        assertNotNull(result);
        assertEquals(expectedForecast, result);

        verify(userCycleService, times(1)).getUserByName(USER_NAME);
    }
}
