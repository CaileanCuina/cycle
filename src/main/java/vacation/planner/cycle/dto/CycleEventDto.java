package vacation.planner.cycle.dto;

import java.time.LocalDate;

import vacation.planner.cycle.entities.CycleEventType;

public record CycleEventDto(LocalDate occurrenceDate, CycleEventType type) {
}
