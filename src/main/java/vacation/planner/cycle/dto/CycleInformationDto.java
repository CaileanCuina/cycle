package vacation.planner.cycle.dto;

import java.time.LocalDate;
import java.util.List;

public record CycleInformationDto(Long id, LocalDate startDate, LocalDate endDate, List<CycleEventDto> events){
}
