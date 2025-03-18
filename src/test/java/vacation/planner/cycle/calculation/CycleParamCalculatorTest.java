package vacation.planner.cycle.calculation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import vacation.planner.cycle.entities.CycleInformation;

@ExtendWith(MockitoExtension.class)
class CycleParamCalculatorTest {

    @Test
    void calculateAvgCycleLen_nullCycleInformation_returnsNull() {

        Double result = CycleParamCalculator.calculateAvgCycleLen(null);

        assertNull(result);
    }

    @Test
    void calculateAvgCycleLen_validCycles_returnsCorrectAverage() {
        List<CycleInformation> cycleInformation = getCorrectInfo();

        Double result = CycleParamCalculator.calculateAvgCycleLen(cycleInformation);

        assertEquals(4.0, result);
    }

    @Test
    void calculateStd_nullCycleInformation_returnsNull() {
        Double result = CycleParamCalculator.calculateStd(null, 4.0);

        assertNull(result);
    }

    @Test
    void calculateStd_validCycles_returnsCorrectStd() {
        List<CycleInformation> cycleInformation = getCorrectInfo();
        Double avgCycleLen = 4.0;

        Double result = CycleParamCalculator.calculateStd(cycleInformation, avgCycleLen);

        assertEquals(0.0, result);
    }

    @Test
    void getTotalDays_returnsCorrectTotalDays() {
        List<CycleInformation> cycleInformation = getCorrectInfo();
        double result = CycleParamCalculator.getTotalDays(cycleInformation);

        assertEquals(8.0, result);
    }

    @Test
    void extractIntervals_returnsCorrectIntervals() {
        List<CycleInformation> cycleInformation = getCorrectInfo();

        DoubleStream result = CycleParamCalculator.extractIntervals(cycleInformation.stream());

        assertEquals(2, result.count()); // There should be two intervals
    }

    private List<CycleInformation> getCorrectInfo(){
        return Arrays.asList(
                new CycleInformation(null, null, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 5), null),
                new CycleInformation(null, null, LocalDate.of(2023, 1, 6), LocalDate.of(2023, 1, 10), null)
        );
    }
}