package ru.ifmo.rain.serdiukov.reactive.ex;

public class LoginIsOccupied extends RuntimeException {
    public LoginIsOccupied() {
    }

    public LoginIsOccupied(String message) {
        super(message);
    }

    public LoginIsOccupied(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginIsOccupied(Throwable cause) {
        super(cause);
    }
}
