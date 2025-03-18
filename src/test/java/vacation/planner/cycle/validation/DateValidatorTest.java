package vacation.planner.cycle.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import vacation.planner.cycle.dto.CycleInformationDto;
import vacation.planner.cycle.dto.UserDto;
import vacation.planner.cycle.exceptions.CycleSequenceException;
import vacation.planner.cycle.exceptions.InvalidDateException;

@ExtendWith(MockitoExtension.class)
class DateValidatorTest {
private static final int YEAR = 2025;
private static final int MONTH_JAN = 1;

    @Test
    void validateCycleInformation_withNullCycles_doesNotValidate() {
        UserDto user = new UserDto("user1", null, null);

        DateValidator.validateCycleInformation(user);
    }

    @Test
    void validateDatesForNewCycles_validCycles_doesNotThrowException() {
        List<CycleInformationDto> cycles = Arrays.asList(
                new CycleInformationDto(1L, LocalDate.of(YEAR, MONTH_JAN, 1), LocalDate.of(YEAR, MONTH_JAN, 5), null),
                new CycleInformationDto(2L, LocalDate.of(YEAR, MONTH_JAN, 6), LocalDate.of(YEAR, MONTH_JAN, 10), null)
        );
        assertDoesNotThrow(() -> DateValidator.validateDatesForNewCycles(cycles));
    }

    @Test
    void validateDatesForNewCycles_missingStartDate_throwsException() {
        List<CycleInformationDto> cycles = Arrays.asList(
                new CycleInformationDto(1L, null, LocalDate.of(YEAR, MONTH_JAN, 5), null),
                new CycleInformationDto(2L, LocalDate.of(YEAR, MONTH_JAN, 6), LocalDate.of(YEAR, MONTH_JAN, 10), null)
        );

        assertThrows(InvalidDateException.class, () -> DateValidator.validateDatesForNewCycles(cycles));
    }

    @Test
    void validateStartAndEndDate_invalidDates_throwsException() {
        LocalDate start = LocalDate.of(YEAR, MONTH_JAN, 5);
        LocalDate end = LocalDate.of(YEAR, MONTH_JAN, 1);

        assertThrows(InvalidDateException.class, () -> DateValidator.validateStartAndEndDate(start, end));
    }

    @Test
    void validateCycleSequence_validSequence_doesNotThrowException() {
        List<CycleInformationDto> cycles = Arrays.asList(
                new CycleInformationDto(1L, LocalDate.of(YEAR, MONTH_JAN, 1), LocalDate.of(YEAR, MONTH_JAN, 5), null),
                new CycleInformationDto(2L, LocalDate.of(YEAR, MONTH_JAN, 6), LocalDate.of(YEAR, MONTH_JAN, 10), null)
        );

        assertDoesNotThrow(() -> DateValidator.validateCycleSequence(cycles));
    }

    @Test
    void validateCycleSequence_invalidSequence_throwsException() {
        List<CycleInformationDto> cycles = Arrays.asList(
                new CycleInformationDto(1L, LocalDate.of(YEAR, MONTH_JAN, 1), LocalDate.of(YEAR, MONTH_JAN, 5), null),
                new CycleInformationDto(12L, LocalDate.of(YEAR, MONTH_JAN, 5), LocalDate.of(YEAR, MONTH_JAN, 10), null)
        );

        assertThrows(CycleSequenceException.class, () -> DateValidator.validateCycleSequence(cycles));
    }
}
