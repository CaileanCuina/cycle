package vacation.planner.cycle.calculation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import vacation.planner.cycle.dto.CycleForeCastDto;
import vacation.planner.cycle.entities.CycleInformation;
import vacation.planner.cycle.entities.User;

@Component
public class PeriodForecastCalculator{

    private PeriodForecastCalculator(){

    }

    private static final double DEFAULT_CYCLE_LENGTH = 28.0;

    private static final double  DEFAULT_MENS_LENGTH = 4.0;

    private static final double  DEFAULT_CYCLE_STD = 0.0;

    private static final double  AMOUNT_OF_SECTIONS = 3.0;
    public static CycleForeCastDto calcForeCast(User user, LocalDate vacationStart, LocalDate vacationEnd) {

        return new CycleForeCastDto(calculateDailyProbabilities(
                user, vacationStart, vacationEnd));

    }

    private static Map<LocalDate, PeriodProbability> calculateDailyProbabilities(User user,
            LocalDate vacationStart, LocalDate vacationEnd
    ) {
        Map<LocalDate, PeriodProbability> probabilities = createInitMap(vacationStart, vacationEnd);

        LocalDate lastPeriodStart = getLastStartDate(user);
        long avgCycleLength = Math.round(user.getAvgCycleLen() == null ? DEFAULT_CYCLE_LENGTH : user.getAvgCycleLen());
        long avgMensLength = Math.round(user.getAvgMensLen() == null ? DEFAULT_MENS_LENGTH : user.getAvgMensLen());
        double avgCycleStd = Math.round(user.getCycleStd() == null ? DEFAULT_CYCLE_STD : user.getCycleStd());

        int cycleNumber = 1;
        for (LocalDate cycleStart = lastPeriodStart; cycleStart.isBefore(
                vacationEnd); cycleStart = cycleStart.plusDays(avgCycleLength), cycleNumber++) {

            double stdDev = avgCycleStd * Math.sqrt(cycleNumber);
            long cycleWindowVar = Math.round(stdDev / 2.0);
            Map<LocalDate, PeriodProbability> probabilityMap =
                    calculateProbability(cycleStart, avgMensLength, cycleWindowVar);

            for (LocalDate day = cycleStart.minusDays(cycleWindowVar);
                 !day.isEqual(cycleStart.plusDays(avgMensLength + cycleWindowVar));
                 day = day.plusDays(
                         1L)) {
                if (day.isAfter(vacationStart.minusDays(1L)) && day.isBefore(vacationEnd.plusDays(1L))) {
                    probabilities.put(day, probabilityMap.get(day));
                }
            }

        }
        return new TreeMap<>(probabilities);
    }

    static Map<LocalDate, PeriodProbability> createInitMap(LocalDate vacationStart, LocalDate vacationEnd) {
        Map<LocalDate, PeriodProbability> probabilities = new LinkedHashMap<>();
        for (LocalDate day = vacationStart; !day.isAfter(vacationEnd); day = day.plusDays(1)) {
            probabilities.put(day, PeriodProbability.NONE);
        }
        return probabilities;
    }

    static LocalDate getLastStartDate(User user) {
        return user.getCycleInformation().stream()
                .sorted(Comparator.comparing(CycleInformation::getStartDate))
                .toList().get(user.getCycleInformation().size() - 1).getStartDate();
    }

    static Map<LocalDate, PeriodProbability> calculateProbability(LocalDate cycleStart, long mensLen,
            long stdHalf) {
        Map<LocalDate, PeriodProbability> resultMap = new LinkedHashMap<>();
        LocalDate startDate = cycleStart.minusDays(stdHalf);
        LocalDate endDate = cycleStart.plusDays(mensLen + stdHalf);
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate);
        long sectionSize = Math.round(totalDays / AMOUNT_OF_SECTIONS);

        int iterator = 1;
        for (LocalDate day = startDate; !day.isAfter(endDate.minusDays(1)); day = day.plusDays(1), iterator++) {
            resultMap.put(day, determinePeriodProbability(iterator, totalDays, sectionSize, stdHalf));
        }

        return resultMap;
    }

    static PeriodProbability determinePeriodProbability(int iterator, long totalDays, long sectionSize,
            long stdHalf) {
        if (stdHalf == 0) {
            return PeriodProbability.HIGH;
        }
        if (stdHalf == 1) {
            return (iterator == 1 || iterator == totalDays) ? PeriodProbability.MEDIUM : PeriodProbability.HIGH;
        }
        if (stdHalf == 2) {
            return getProbabilityForMediumStd(totalDays, iterator);
        }
        return getProbabilityForHighStd(sectionSize, totalDays, iterator);
    }

    private static PeriodProbability getProbabilityForMediumStd(long totalDays, int iterator) {
        if (iterator == 1 || iterator == totalDays)
            return PeriodProbability.LOW;
        if (iterator == 2 || iterator == totalDays - 1)
            return PeriodProbability.MEDIUM;
        return PeriodProbability.HIGH;
    }

    private static PeriodProbability getProbabilityForHighStd(long sectionSize, long totalDays, int iterator) {
        if ((iterator < (1 + sectionSize / 2)) || iterator == totalDays ||
                (iterator > (totalDays - sectionSize / 2 + 1)))
            return PeriodProbability.LOW;
        if ((iterator < (1 + sectionSize)) || (iterator > (totalDays - sectionSize + 1)))
            return PeriodProbability.MEDIUM;
        return PeriodProbability.HIGH;
    }
}


