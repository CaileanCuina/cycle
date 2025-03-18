package vacation.planner.cycle.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vacation.planner.cycle.dto.CycleForeCastDto;
import vacation.planner.cycle.dto.CycleInformationDto;
import vacation.planner.cycle.service.CycleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
@Tag(name = "Cycle Controller", description = "Detailierte Informationen über den Zyklus")
public class CycleController {

    private final CycleService cycleService;

    @GetMapping("/{userName}/current-cycle")
    @Operation(summary = "Aktuelle Zyklus Daten laden")
    public ResponseEntity<CycleInformationDto> getCurrentCycle(@PathVariable String userName) {
        return ResponseEntity.ok(cycleService.getCurrentCycleInformation(userName));
    }

    @GetMapping("/{userName}/period-forecast")
    @Operation(summary = "ForeCast mit Wahrscheinlichkeit für Periode")
    public ResponseEntity<CycleForeCastDto> getUserByName(@PathVariable String userName, @RequestParam
    LocalDate startDate, @RequestParam LocalDate endDate) {
        log.info("Fetching period forecast for user: {} from {} to {}.", userName, startDate, endDate);

        return ResponseEntity.ok(cycleService.getForeCastByDate(userName, startDate, endDate));
    }
}
