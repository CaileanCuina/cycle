package vacation.planner.cycle.validation;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;
import vacation.planner.cycle.dto.CycleInformationDto;
import vacation.planner.cycle.dto.UserDto;
import vacation.planner.cycle.exceptions.CycleSequenceException;
import vacation.planner.cycle.exceptions.InvalidDateException;

@Slf4j
public class DateValidator {
    private DateValidator() {
    }
    public static void validateCycleInformation(UserDto user) {
        if (Objects.nonNull(user.cycleInformation())) {
            log.info("Start validation of Dates per Cycle for user {}.", user.userName());
            validateDatesForNewCycles(user.cycleInformation());
            validateCycleSequence(user.cycleInformation());
            log.info("Validation succeeded for user {}.", user.userName());
        }
    }
    protected static void validateDatesForNewCycles(List<CycleInformationDto> cycles) {
        cycles.forEach(DateValidator::validateDatesPerCycle);
    }
    private static void validateDatesPerCycle(CycleInformationDto cycleInformation) {
        if (Objects.isNull(cycleInformation.startDate())) {
            throw new InvalidDateException("Start Date must not be empty.");
        }
        if (Objects.isNull(cycleInformation.endDate())) {
            return;
        }
        validateStartAndEndDate(cycleInformation.startDate(), cycleInformation.endDate());
    }

    public static void validateStartAndEndDate(LocalDate start, LocalDate end) {
        if (Objects.isNull(start) || Objects.isNull(end)){
            throw new InvalidDateException("Start or End Date must not be empty.");
        }
        if (start.isAfter(end) ||
                end.isEqual(start)) {
            throw new InvalidDateException("The Start Date cannot be after the End Date or on the same day.");
        }
    }

    public static void validateEndDateAfterMensDate(List<LocalDate> mensDates, LocalDate end) {
        if (Objects.isNull(mensDates) || Objects.isNull(end)){
           return;
        }
        if (mensDates.stream().anyMatch( date -> date.isAfter(end))) {
            throw new InvalidDateException("The Mens Dates cannot be after the End Date.");
        }
    }

    static void validateCycleSequence(List<CycleInformationDto> cycleInformation) {
        List<CycleInformationDto> sorted = cycleInformation.stream()
                .sorted(Comparator.comparing(CycleInformationDto::startDate))
                .toList();
        IntStream.range(0, sorted.size() - 1)
                .mapToObj(i -> new EventPair(sorted.get(i), sorted.get(i + 1)))
                .forEach(EventPair::checkDateCondition);

    }

    record EventPair(CycleInformationDto first, CycleInformationDto second) {
        void checkDateCondition() {
            var isOneDayBefore = first.endDate().plusDays(1).equals(second.startDate());
            if (!isOneDayBefore) {
                throw new CycleSequenceException("The EndDate is not one Day before the Start Date of the next Cycle.");
            }
        }
    }
}
