package ru.shaxowskiy.NauJava.exception;

public class UserNotAuthenticatedException extends RuntimeException{

    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
