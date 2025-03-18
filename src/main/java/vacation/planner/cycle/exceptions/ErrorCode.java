package vacation.planner.cycle.exceptions;

import lombok.Getter;

@Getter
public enum ErrorCode {

    U0("Username not Unique."),
    U1("User not found."),
    U2("Invalid Dates. Check if Start Date is before End Date or if Start Date is given."),
    U3("Check the Cycle Sequence - maybe there are collision between the cycles."),
    C0("No cycle for user present."),
    C1("Invalid amount of cycles found.");

    private final String description;

    ErrorCode(String description) {
        this.description = description;
    }
}
