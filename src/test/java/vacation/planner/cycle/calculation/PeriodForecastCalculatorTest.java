package vacation.planner.cycle.calculation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.*;

import vacation.planner.cycle.dto.CycleForeCastDto;
import vacation.planner.cycle.entities.CycleInformation;
import vacation.planner.cycle.entities.User;

@ExtendWith(MockitoExtension.class)
class PeriodForecastCalculatorTest {


    @Test
    void calcForeCast_validUser_returnsCorrectForecast() {

        User user = createUserMock();

        LocalDate vacationStart = LocalDate.of(2025, 3, 15);
        LocalDate vacationEnd = LocalDate.of(2025, 3, 20);

        CycleForeCastDto result = PeriodForecastCalculator.calcForeCast(user, vacationStart, vacationEnd);

        assertNotNull(result);
        assertFalse(result.foreCast().isEmpty());
    }

    @Test
    void createInitMap_createsCorrectInitMap() {
        LocalDate vacationStart = LocalDate.of(2025, 3, 15);
        LocalDate vacationEnd = LocalDate.of(2025, 3, 20);

        Map<LocalDate, PeriodProbability> result = PeriodForecastCalculator.createInitMap(vacationStart, vacationEnd);

        assertNotNull(result);
        assertEquals(6, result.size());  // From March 15th to March 20th, there should be 6 days
        assertTrue(result.values().stream().allMatch(prob -> prob == PeriodProbability.NONE));  // All days should be NONE initially
    }

    @Test
    void getLastStartDate_validUser_returnsLastCycleStartDate() {
        CycleInformation cycleInfo1 = mock(CycleInformation.class);
        CycleInformation cycleInfo2 = mock(CycleInformation.class);
        when(cycleInfo1.getStartDate()).thenReturn(LocalDate.of(2025, 1, 1));
        when(cycleInfo2.getStartDate()).thenReturn(LocalDate.of(2025, 3, 1));

        User user = mock(User.class);
        when(user.getCycleInformation()).thenReturn(Arrays.asList(cycleInfo1, cycleInfo2));

        LocalDate result = PeriodForecastCalculator.getLastStartDate(user);

        assertNotNull(result);
        assertEquals(LocalDate.of(2025, 3, 1), result);  // The most recent cycle start date is March 1st
    }

    @Test
    void calculateProbability_validData_calculatesCorrectProbabilities() {
        LocalDate cycleStart = LocalDate.of(2025, 3, 1);
        long mensLen = 4;
        long stdHalf = 1;

        Map<LocalDate, PeriodProbability> result = PeriodForecastCalculator.calculateProbability(cycleStart, mensLen, stdHalf);

        assertNotNull(result);
        assertEquals(6, result.size());  // Total days should be 6 for the period window
        assertTrue(result.containsKey(cycleStart));  // The cycle start day should have a probability
    }

    @Test
    void determinePeriodProbability_variousScenarios_returnsCorrectProbability() {
        assertEquals(PeriodProbability.MEDIUM, PeriodForecastCalculator.determinePeriodProbability(1, 6, 2, 1));
        assertEquals(PeriodProbability.HIGH, PeriodForecastCalculator.determinePeriodProbability(2, 6, 2, 1));
        assertEquals(PeriodProbability.LOW, PeriodForecastCalculator.determinePeriodProbability(6, 6, 2, 2));
    }

    private User createUserMock(){
        User user = mock(User.class);
        CycleInformation cycleInfo = mock(CycleInformation.class);
        when(cycleInfo.getStartDate()).thenReturn(LocalDate.of(2025, 3, 1));
        when(user.getCycleInformation()).thenReturn(List.of(cycleInfo));
        when(user.getAvgCycleLen()).thenReturn(28.0);
        when(user.getAvgMensLen()).thenReturn(4.0);
        when(user.getCycleStd()).thenReturn(0.0);

        return user;
    }
}
