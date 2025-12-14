package io.github.amsatrio.dto.exception;

public class DataExistException extends RuntimeException {

    public DataExistException() {}

    public DataExistException(String message) {
        super(message);
    }
    
}
