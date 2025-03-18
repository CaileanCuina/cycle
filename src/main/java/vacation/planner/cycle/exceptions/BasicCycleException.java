package vacation.planner.cycle.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BasicCycleException extends RuntimeException {

    private final ErrorCode errorCode;

    public BasicCycleException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
