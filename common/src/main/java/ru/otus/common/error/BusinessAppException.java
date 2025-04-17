package ru.otus.common.error;

import lombok.Data;

@Data
public class BusinessAppException extends RuntimeException {
    private String code;
    private String httpStatus; //todo to HttpStatus

    public BusinessAppException(String code, String message, String status) {
        super(message);
        this.code = code;
        this.httpStatus = status;
    }

    public BusinessAppException(String code, String message) {
        super(message);
        this.code = code;
    }
}
