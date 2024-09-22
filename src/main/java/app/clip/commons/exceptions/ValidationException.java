package app.clip.commons.exceptions;

import java.util.List;

public class ValidationException extends ApplicationException {

    private List<Violation> violations;

    public ValidationException(List<Violation> violations) {
        super("Validation failed with " + violations.size() + " number of violations");
        this.violations = violations;
    }

    public ValidationException(List<Violation> violations, Throwable cause) {
        super("Validation failed with " + violations.size() + " number of violations", cause);
        this.violations = violations;
    }

    public List<Violation> getViolations() {
        return violations;
    }
}
