package vacation.planner.cycle.exceptions;

import lombok.Getter;

@Getter
public class NoCycleFoundException extends BasicCycleException {

    public NoCycleFoundException(String message) {
        super(message, ErrorCode.C0);
    }
}
