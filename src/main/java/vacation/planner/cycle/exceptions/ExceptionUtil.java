package vacation.planner.cycle.exceptions;

import java.util.UUID;

import org.springframework.ui.Model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionUtil {
    private ExceptionUtil(){
    }
    public static final String ERROR_MESSAGE = "errorMsg";
    public static void handleUnknownException(Exception ex, Model model) {
        UUID errorId = UUID.randomUUID();
        String errorMessage =
                String.format("ErrorId: %s, ErrorMsg: %s. Please forward to helpDesk.", errorId, ex.getMessage());
        log.error("{}: {}", errorId, ex.getStackTrace());
        model.addAttribute(ERROR_MESSAGE, errorMessage);
    }
}
