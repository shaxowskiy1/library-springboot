package ru.shaxowskiy.NauJava.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Exception {

    private String message;

    private Long timestamp;

    public Exception(String message) {
        this.message = message;
    }

    public static Exception create(Throwable e)
    {
        return new Exception(e.getMessage());
    }
    public static Exception create(String message)
    {
        return new Exception(message);
    }

}
