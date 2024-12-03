package ru.shaxowskiy.NauJava.exception;

public class CustomOptimisticLockException extends RuntimeException {

    public CustomOptimisticLockException(String message) {
        super(message);
    }
}
