package vacation.planner.cycle.dto;

import java.time.LocalDate;
import java.util.Map;

import vacation.planner.cycle.calculation.PeriodProbability;

public record CycleForeCastDto(Map<LocalDate, PeriodProbability> foreCast) {

}
