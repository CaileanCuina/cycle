package vacation.planner.cycle.exceptions;

import static vacation.planner.cycle.exceptions.ErrorCode.U3;

import lombok.Getter;

@Getter
public class CycleSequenceException extends BasicCycleException {

    public CycleSequenceException(String message) {
        super(message, U3);
    }
}