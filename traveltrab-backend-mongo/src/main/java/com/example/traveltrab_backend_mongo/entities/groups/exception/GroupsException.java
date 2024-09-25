package com.example.traveltrab_backend_mongo.entities.groups.exception;

public class GroupsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GroupsException(String message) {
        super(message);
    }

    public GroupsException(String message, Throwable cause) {
        super(message, cause);
    }
}
