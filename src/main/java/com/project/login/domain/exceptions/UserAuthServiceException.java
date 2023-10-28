package com.project.login.domain.exceptions;

public class UserAuthServiceException extends RuntimeException{

    public UserAuthServiceException(String message){
        super(message);
    }
}
