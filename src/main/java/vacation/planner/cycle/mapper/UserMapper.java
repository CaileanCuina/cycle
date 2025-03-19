package vacation.planner.cycle.mapper;

import org.mapstruct.Mapper;

import vacation.planner.cycle.dto.UserDto;
import vacation.planner.cycle.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

	User toEntity(UserDto userDto);
}
