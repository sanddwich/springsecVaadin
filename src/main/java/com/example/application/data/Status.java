package com.example.application.data;

public enum Status {
    ACTIVE, NOACTIVE;

    public static Status boolToEnum(boolean status) {
        return status ? Status.ACTIVE : Status.NOACTIVE;
    }

    public static boolean enumToBool(Status status) {
        return status != null && status.equals(Status.ACTIVE) ? true : false;
    }
}
