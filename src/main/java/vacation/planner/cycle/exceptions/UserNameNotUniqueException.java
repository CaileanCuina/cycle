package vacation.planner.cycle.exceptions;

import lombok.Getter;

@Getter
public class UserNameNotUniqueException extends BasicCycleException {

    public UserNameNotUniqueException(String message) {
        super(message, ErrorCode.U0);
    }
}
