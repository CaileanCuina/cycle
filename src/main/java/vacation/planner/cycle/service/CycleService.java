package vacation.planner.cycle.service;

import static vacation.planner.cycle.validation.DateValidator.validateEndDateAfterMensDate;
import static vacation.planner.cycle.validation.DateValidator.validateStartAndEndDate;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vacation.planner.cycle.calculation.PeriodForecastCalculator;
import vacation.planner.cycle.dto.CycleForeCastDto;
import vacation.planner.cycle.dto.CycleInformationDto;
import vacation.planner.cycle.entities.CycleEvent;
import vacation.planner.cycle.entities.CycleInformation;
import vacation.planner.cycle.exceptions.InvalidAmountOfCycleFoundException;
import vacation.planner.cycle.exceptions.NoCycleFoundException;
import vacation.planner.cycle.mapper.CycleInformationMapper;
import vacation.planner.cycle.repo.CycleRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class CycleService {

	private static final int FIRST = 0;

	private final UserCycleService userCycleService;

	private final CycleRepo cycleRepo;

	private final CycleInformationMapper mapper;

	public CycleInformationDto persistCycleWithNewEvents(CycleInformation cycleInformation, List<CycleEvent> events) {
		cycleInformation.setEvents(events);
		return mapper.toDto(cycleRepo.save(cycleInformation));
	}

	public CycleInformationDto getCurrentCycleInformation(String userName) {

		CycleInformation currentCycle = findCurrentCycleByUser(userName);

		return mapper.toDto(currentCycle);
	}

	public List<CycleInformationDto> getAllCycles(String userName) {

		List<CycleInformation> allCycles = userCycleService.getUserByName(userName).getCycleInformation();

		return mapper.toDtoList(allCycles);
	}

	public CycleInformation findCurrentCycleByUser(String userName) {
		List<CycleInformation> currentCycle = cycleRepo.findCyclesByDateAndUsername(LocalDate.now(), userName);
		validateCurrentCycleInformationList(currentCycle, userName);
		return currentCycle.get(FIRST);
	}

	private void validateCurrentCycleInformationList(List<CycleInformation> cycleInformationList, String userName) {
		if (cycleInformationList.isEmpty()) {
			String errorMsg = String.format("No ongoing cycle found for user %s", userName);
			log.error(errorMsg);
			throw new NoCycleFoundException(errorMsg);
		}
		if (cycleInformationList.size() > 1) {
			String errorMsg = String.format("More than one ongoing cycles found for user %s", userName);
			log.error(errorMsg);
			throw new InvalidAmountOfCycleFoundException(errorMsg);
		}
	}

	public CycleInformationDto updateEndDateOfLastCycle(String userName, LocalDate endDate) {
		log.info("Update ongoing cycle for user {}.", userName);
		var currentCycle = findCurrentCycleByUser(userName);
		currentCycle.setEndDate(endDate);
		validateStartAndEndDate(currentCycle.getStartDate(), endDate);
		validateEndDateAfterMensDate(currentCycle.getEvents().stream().map(CycleEvent::getOccurrenceDate).toList(), endDate);
		cycleRepo.save(currentCycle);
		log.info("Updated ongoing cycle for user {}.", userName);
		return mapper.toDto(currentCycle);
	}

	public void startNewCycle(String userName, LocalDate endDate) {
		var user = userCycleService.getUserByName(userName);
		CycleInformation cycleInformation = new CycleInformation();
		cycleInformation.setStartDate(endDate.plusDays(1L));
		cycleInformation.setUser(user);
		cycleRepo.save(cycleInformation);
	}

	public CycleForeCastDto getForeCastByDate(String userName, LocalDate startDate, LocalDate endDate) {
		validateStartAndEndDate(startDate, endDate);
		var user = userCycleService.getUserByName(userName);
		return PeriodForecastCalculator.calcForeCast(user, startDate, endDate);
	}
}
