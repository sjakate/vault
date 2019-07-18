package com.patreon.vault;

public class InvalidAuthTokenException extends Exception{

    public InvalidAuthTokenException(String message) {
        super(message);
    }
}
