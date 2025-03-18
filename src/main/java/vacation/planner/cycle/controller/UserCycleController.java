package vacation.planner.cycle.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vacation.planner.cycle.dto.UserDto;
import vacation.planner.cycle.service.UserCycleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
@Tag(name = "User Controller", description = "Verwaltung von User Daten")
public class UserCycleController {

    private final UserCycleService userCycleService;

    @PostMapping
    @Operation(summary = "Eine neuen User anlegen")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        log.info("Start persisting user with name {}", userDto.userName());
        return ResponseEntity.ok(userCycleService.persistUser(userDto));
    }

    @PutMapping
    @Operation(summary = "User update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        log.info("Start updating user with name {}", userDto.userName());
        return ResponseEntity.ok(userCycleService.updateUser(userDto));
    }

    @GetMapping("/{userName}")
    @Operation(summary = "User laden")
    public ResponseEntity<UserDto> getUserByName(@PathVariable String userName) {
        var user = userCycleService.getUserDtoByName(userName);
        log.info("Return user with name {}", userName);
        return ResponseEntity.ok(user);
    }
}
