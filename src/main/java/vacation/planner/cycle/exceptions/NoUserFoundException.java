package vacation.planner.cycle.exceptions;

import lombok.Getter;

@Getter
public class NoUserFoundException extends BasicCycleException {

    public NoUserFoundException(String message) {
        super(message, ErrorCode.U1);
    }
}
