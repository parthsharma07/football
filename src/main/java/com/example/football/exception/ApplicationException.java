package com.example.football.exception;

public class ApplicationException extends Exception{
    String errorCode;
    String errorMessage;

    public ApplicationException(String s, String message) {
        this.errorCode = s;
        this.errorMessage = message;
    }
}
