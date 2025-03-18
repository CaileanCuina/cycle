package vacation.planner.cycle.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import vacation.planner.cycle.dto.CycleInformationDto;
import vacation.planner.cycle.entities.CycleInformation;

@Mapper(componentModel = "spring")
public interface CycleInformationMapper {

    CycleInformationDto toDto(CycleInformation cycleInformation);

    List<CycleInformationDto> toDtoList(List<CycleInformation> cycleInformation);
}
