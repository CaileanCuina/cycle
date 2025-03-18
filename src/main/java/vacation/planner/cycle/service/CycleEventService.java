package vacation.planner.cycle.service;

import static vacation.planner.cycle.validation.DateValidator.validateEndDateAfterMensDate;
import static vacation.planner.cycle.validation.DateValidator.validateStartAndEndDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vacation.planner.cycle.dto.CycleInformationDto;
import vacation.planner.cycle.entities.CycleEvent;
import vacation.planner.cycle.entities.CycleEventType;
import vacation.planner.cycle.entities.CycleInformation;

@Service
@RequiredArgsConstructor
@Slf4j
public class CycleEventService {

    private final CycleService cycleService;

    @Transactional
    public CycleInformationDto addMenstruationToCycle(String userName, LocalDate mensEndDate) {

        CycleInformation cycleInformation = cycleService.findCurrentCycleByUser(userName);
        validateStartAndEndDate(cycleInformation.getStartDate(), mensEndDate);
        LocalDate mensDay = cycleInformation.getStartDate();
        List<CycleEvent> events = new ArrayList<>();

        while (mensEndDate.isAfter(mensDay.minusDays(1L))) {
            events.add(createNewEvent(mensDay, CycleEventType.MENSTRUATION));
            mensDay = mensDay.plusDays(1L);
        }

        validateEndDateAfterMensDate(events.stream().map(CycleEvent::getOccurrenceDate).toList(), cycleInformation.getEndDate());
        return cycleService.persistCycleWithNewEvents(cycleInformation, events);
    }

    private CycleEvent createNewEvent(LocalDate mensDay, CycleEventType type) {
        CycleEvent event = new CycleEvent();
        event.setType(type);
        event.setOccurrenceDate(mensDay);
        return event;
    }
}
