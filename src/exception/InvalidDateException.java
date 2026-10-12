package exception;

public class InvalidDateException extends RuntimeException {

    private final String operationContext;

    public InvalidDateException(String message, String operationContext) {
        super(message);
        this.operationContext = operationContext;
    }

    public InvalidDateException(String message, String operationContext, Throwable cause) {
        super(message, cause);
        this.operationContext = operationContext;
    }

    public String getOperationContext() {
        return operationContext;
    }

    @Override
    public String toString() {
        return String.format("InvalidDateException[%s]: %s", operationContext, getMessage());
    }
}