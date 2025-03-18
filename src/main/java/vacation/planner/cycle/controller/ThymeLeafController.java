package vacation.planner.cycle.controller;

import static vacation.planner.cycle.calculation.PeriodForecastCalculator.calcForeCast;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vacation.planner.cycle.dto.CycleForeCastDto;
import vacation.planner.cycle.dto.CycleInformationDto;
import vacation.planner.cycle.entities.User;
import vacation.planner.cycle.exceptions.BasicCycleException;
import vacation.planner.cycle.exceptions.NoUserFoundException;
import vacation.planner.cycle.service.CycleEventService;
import vacation.planner.cycle.service.CycleService;
import vacation.planner.cycle.service.UserCycleService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class ThymeLeafController {

    private static final String DASHBOARD = "dashboard";

    private static final String USER = "user";

    private static final String FORECAST = "forecast";

    private static final String LOGIN = "login";

    private static final String CURRENT_CYCLE = "currentCycle";

    private static final String POST_LOGIN = "postlogin";

    private static final String ERROR_MESSAGE = "errorMsg";

    private final UserCycleService userCycleService;

    private final CycleService cycleService;

    private final CycleEventService cycleEventService;


    @GetMapping
    public String showLoginForm() {
        return LOGIN;
    }

    @PostMapping("/" + LOGIN)
    public String login(@RequestParam String username, Model model, HttpSession session) {
        try {
            User user = userCycleService.getUserByName(username);
            session.setAttribute(USER, user.getUserName());
            model.addAttribute(USER, user);
            return POST_LOGIN;

        } catch (NoUserFoundException ex) {
            return "redirect:/?error";
        }
    }

    @GetMapping("/" + DASHBOARD)
    public String getDashboardInfo(HttpSession session, Model model) {

        try {
            User user = userCycleService.getUserByName((String) session.getAttribute(USER));

            model.addAttribute(FORECAST, calculateForecast(user, LocalDate.now(),
                    LocalDate.now().plusDays(10L)));
            model.addAttribute(USER, user);
        } catch (BasicCycleException basicCycleException) {
            model.addAttribute(ERROR_MESSAGE, basicCycleException.getErrorCode().getDescription());
        } catch (Exception ex) {
            handleUnknownException(ex, model);
        }

        return DASHBOARD;

    }

    @PostMapping("/current-cycle")
    public String addEndDateAndStartNewCycle(HttpSession session, Model model, LocalDate endDate) {
        try {
            CycleInformationDto cycleInformationDto =
                    cycleService.updateEndDateOfLastCycle((String) session.getAttribute(USER), endDate);
            cycleService.startNewCycle((String) session.getAttribute(USER), endDate);
            model.addAttribute(CURRENT_CYCLE, cycleInformationDto);
        } catch (BasicCycleException basicCycleException) {
            model.addAttribute(ERROR_MESSAGE, basicCycleException.getErrorCode().getDescription());
        } catch (Exception ex) {
            handleUnknownException(ex, model);
        }
        return CURRENT_CYCLE;
    }

    @PostMapping("/current-cycle/mens")
    public String updateMensPerCycle(HttpSession session, Model model, LocalDate endDateMens) {
        try {
            CycleInformationDto cycleInformationDto =
                    cycleEventService.addMenstruationToCycle((String) session.getAttribute(USER), endDateMens);
            model.addAttribute(CURRENT_CYCLE, cycleInformationDto);
        } catch (BasicCycleException basicCycleException) {
            model.addAttribute(ERROR_MESSAGE, basicCycleException.getErrorCode().getDescription());
        } catch (Exception ex) {
            handleUnknownException(ex, model);
        }
        return CURRENT_CYCLE;

    }

    @GetMapping("/current-cycle")
    public String getCurrentCycleScreen(HttpSession session, Model model) {
        try {
            model.addAttribute(CURRENT_CYCLE,
                    cycleService.getCurrentCycleInformation((String) session.getAttribute(USER)));
        } catch (BasicCycleException basicCycleException) {
            model.addAttribute(ERROR_MESSAGE, basicCycleException.getErrorCode().getDescription());
        } catch (Exception ex) {
            handleUnknownException(ex, model);
        }
        return CURRENT_CYCLE;

    }

    @GetMapping("/all-cycles")
    public String getAllCyclesScreen(HttpSession session, Model model) {
        try {
            model.addAttribute("cycleList",
                    cycleService.getAllCycles((String) session.getAttribute(USER)));
        } catch (BasicCycleException basicCycleException) {
            model.addAttribute(ERROR_MESSAGE, basicCycleException.getErrorCode().getDescription());
        } catch (Exception ex) {
            handleUnknownException(ex, model);
        }
        return "allCycles";

    }

    @PostMapping("/calculate")
    public String calculateForecast(@RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr,
            Model model, HttpSession session) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        try {
            User user = userCycleService.getUserByName((String) session.getAttribute(USER));

            model.addAttribute(FORECAST, calculateForecast(user, startDate, endDate));
            model.addAttribute(USER, user);
        } catch (BasicCycleException basicCycleException) {
            model.addAttribute(ERROR_MESSAGE, basicCycleException.getErrorCode().getDescription());
        } catch (Exception ex) {
            handleUnknownException(ex, model);
        }
        return DASHBOARD;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(USER);
        return "redirect:/";
    }

    @GetMapping("/" + POST_LOGIN)
    public String goBack(HttpSession session, Model model) {
        User user = userCycleService.getUserByName((String) session.getAttribute(USER));
        session.setAttribute(USER, user.getUserName());
        model.addAttribute(USER, user);
        return POST_LOGIN;
    }

    private void handleUnknownException(Exception ex, Model model) {
        UUID errorId = UUID.randomUUID();
        String errorMessage =
                String.format("ErrorId: %s, ErrorMsg: %s. Please forward to helpDesk.", errorId, ex.getMessage());
        log.error("{}: {}", errorId, ex.getStackTrace());
        model.addAttribute(ERROR_MESSAGE, errorMessage);
    }

    private CycleForeCastDto calculateForecast(User user, LocalDate start, LocalDate end) {
        return calcForeCast(user, start, end);
    }

}
