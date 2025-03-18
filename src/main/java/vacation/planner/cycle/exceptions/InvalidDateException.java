package vacation.planner.cycle.exceptions;

import lombok.Getter;

@Getter
public class InvalidDateException extends BasicCycleException {


    public InvalidDateException(String message) {
        super(message, ErrorCode.U2);
    }
}
