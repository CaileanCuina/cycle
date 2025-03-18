package vacation.planner.cycle.calculation;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import vacation.planner.cycle.entities.CycleInformation;

public class CycleParamCalculator {

    private static final int SQUARE_ROOT = 2;

    private CycleParamCalculator() {
    }

    public static Double calculateAvgCycleLen(List<CycleInformation> cycleInformation) {
        if (Objects.isNull(cycleInformation)) {
            return null;
        }
        double totalDays = getTotalDays(cycleInformation);
        return totalDays / getCyclesWithEndDate(cycleInformation).toList().size();
    }

    static double getTotalDays(List<CycleInformation> cycleInformation) {
        return prepareCycleLengthList(cycleInformation)
                .sum();
    }

    public static Double calculateStd(List<CycleInformation> cycleInformation, Double avgCycleLen) {
        if (Objects.isNull(cycleInformation) || Objects.isNull(avgCycleLen)) {
            return null;
        }
        double variance = prepareCycleLengthList(cycleInformation).map(v -> Math.pow(v - avgCycleLen, SQUARE_ROOT))
                .average().orElse(0.0);
        return Math.sqrt(variance);
    }

    static DoubleStream prepareCycleLengthList(List<CycleInformation> cycleInformation) {
        return extractIntervals(getCyclesWithEndDate(cycleInformation));
    }

    private static Stream<CycleInformation> getCyclesWithEndDate(List<CycleInformation> cycleInformation) {
        return cycleInformation.stream()
                .filter(c -> Objects.nonNull(c.getEndDate()));
    }

    static DoubleStream extractIntervals(Stream<CycleInformation> cycleInformation) {
        return cycleInformation.mapToDouble(
                interval -> ChronoUnit.DAYS.between(interval.getStartDate(), interval.getEndDate()));
    }
}
