package vacation.planner.cycle.dto;

import java.util.List;

public record UserDto(String userName, List<CycleInformationDto> cycleInformation, Double avgMensLen){}
