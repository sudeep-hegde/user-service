package com.shopperzaar.User_Service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {

    private final HttpStatus code;

    public AppException(String message, HttpStatus code){
        super(message);
        this.code = code;
    }

}
