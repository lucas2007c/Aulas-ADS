package exception;

public class PermissaoNegadaException extends Exception {

    public PermissaoNegadaException(String message) {
        super(message);
    }

    public PermissaoNegadaException(String message, Throwable cause) {
        super(message, cause);
    }
}
