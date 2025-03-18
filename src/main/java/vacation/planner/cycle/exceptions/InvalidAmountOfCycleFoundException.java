package vacation.planner.cycle.exceptions;

import lombok.Getter;

@Getter
public class InvalidAmountOfCycleFoundException extends BasicCycleException {

    public InvalidAmountOfCycleFoundException(String message) {
        super(message, ErrorCode.C1);
    }
}
