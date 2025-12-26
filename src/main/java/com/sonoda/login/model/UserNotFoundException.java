package com.sonoda.login.model;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
        super(message);

    }
}
